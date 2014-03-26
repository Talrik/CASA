<?php
/**
 * This file contains the functionality to update services on the server and on the data base
 *
 * Copyright (c)  2013  <philipp.lehsten@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @category   StudIP_Plugin
 * @package    de.lehsten.casa.studip.plugin
 * @author     Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 * @copyright  2013 Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 * @since      File available since Release 1.0
 */
	
$settings = CasaSettings::getCasaSettings();
$oldService = createOldService();

/**
 * retrieve or create the service ID from a given service
 * @param Service $service 
 * @return string serviceID
 */
function getServiceID($service){
	$entry = $service->properties->entry;
	for ($i=0; $i<sizeof($entry);$i++){
		if ($entry[$i]->key == 'ID'){
		return $entry[$i]->value;
		}
	}
	return hash('ripemd160', 
		$service->title
		.$service->description
		.$service->provider
		.$service->restrictions
		.$service->targetURL
		.$service->lecture
		.$service->location);
}

/**
 * returns the old service which is already in the data base
 * @return Service new service
 */
function createOldService(){
	// get the old one from the data base by its ID	and store it in $oldService
	$query = "SELECT *
	FROM `casa_services`
	WHERE (`serviceID` = :oldServiceID)";
	$statement = DBManager::get()->prepare($query); 
	$statement->bindValue(':oldServiceID', Request::get("service_id"));
	$statement->execute();
	$result = $statement->fetch(PDO::FETCH_ASSOC);
	$oldService = Service::createFromDBEntry($result);
	return $oldService;
}

/**
 * creates the new service by getting the old one and comparing both - the new 
 * one so gets all the changes without submitting the unchaged values
 * @return Service new service
 */
function createNewService(){
	$oldService = createOldService();
// compare the old service with the values from the Request - if they are different store the ones from the Request
if(strlen(Request::get("service_name"))!= 0 && !is_null(Request::get("service_name"))){
	$title=Request::get("service_name");
}else{
	$title=$oldService->title;
}  
if(strlen(Request::get("service_description"))!= 0 && !is_null(Request::get("service_description"))){
	$description=Request::get("service_description");
}else{
	$description=$oldService->description;
}  
if(strlen(Request::get("service_address"))!= 0 && !is_null(Request::get("service_address"))){
	$url=Request::get("service_address");
}else{
	$url=$oldService->targetURL;	
}
if(strlen(Request::get("location"))!= 0 && !is_null(Request::get("location"))){
	$newLocation=Request::get("location");
}else{
	$newLocation=$oldService->location;	
}
if(strlen(Request::get("service_restrictions"))!= 0 && !is_null(Request::get("service_restrictions"))){
	$restriction=Request::get("service_restrictions");
}else{
	if(is_array($oldService->restrictions)){
		$restriction=implode(";",$oldService->restrictions);	
	}else{
		$restriction=$oldService->restrictions;
	}
}
// create the new service
$newService = Service::createFromValues($title,
										$description,
										$oldService->provider,
										$restriction,
										$url,
										$serviceID,
										$oldService->lecture,
										$newLocation);										
return $newService;									
}
// if casa server is used first update the service on the server
if($settings['useCASA'] == 1){
	try{	
		$wsdl = $settings['broker'];
		$options = array();
        $client = new SoapClient($wsdl,$options);
		$newService = createNewService();
	$title=utf8_encode($newService->title);
    $description=iconv('windows-1252','UTF-8',$newService->description);
	if ($location == NULL)
	{
        $ParamList = array(
		"PropertyKey" => "ID",
		"PropertyValue" => Request::get("service_id"),
        	"userRole" => $newService->restrictions,
 		"url" => $newService->targetURL,
                "title" => $title,
		"description" => $description,
 		"location" => '',
		"author" => $username);
        $response = $client->updateService($ParamList);
        $array = $response->return;
	}
	elseif ($location != NULL)
	{
        $ParamList = array(
		"PropertyKey" => "ID",
		"PropertyValue" => Request::get("service_id"),
        	"userRole" => $newService->resrictions,
 		"url" => $newService->targetURL,
                "title" => $title,
		"description" => $description,
 		"location" => utf8_encode($newService->location),
		"author" => $username);
        $response = $client->updateService($ParamList);
        $array = $response->return;
	}
	
                       }
                        catch (SoapFault $E){
                                echo $E;
                        }
// casa server will return the changed service if everything went well - if not view an error message 
if (is_null($array)){
		echo MessageBox::error('Beim Ändern des Dienstes ist ein Fehler aufgetreten', array('Der Dienst wurde nicht geändert.'), true);
}
// if it worked update the service in the data base
else{
	$newService = Service::createFromCASA($array);
		$query = "UPDATE `casa_services`
					SET `title` = :title, 
						`description` = :description, 
						`url` = :url, 
						`userrole` = :userrole, 
						`location` = :location, 
						`serviceID` = :newServiceID, 
						`modified` = NOW()
					WHERE `serviceID` = :oldServiceID";
        $statement = DBManager::get()->prepare($query);
        $statement->bindValue(':title', $newService->title);
        $statement->bindValue(':description', $newService->description);
        $statement->bindValue(':url', $newService->targetURL);
        $statement->bindValue(':userrole', implode(";",$newService->restrictions));
       $statement->bindValue(':location', $newService->location);
       $statement->bindValue(':newServiceID', getServiceID($array));
        $statement->bindValue(':oldServiceID', Request::get("service_id"));
       $statement->execute();
	
	echo MessageBox::success('Dienst erfolgreich geändert', array('Der Dienst' . $ParamList->title . ' wurde erfolgreich geändert.'), true);
(is_array($newService->restrictions)  ? $newRestriction = implode(";",$newService->restrictions) : $newRestriction = $newService->restrictions);
(is_array($oldService->restrictions)  ? $oldRestriction = implode(";",$oldService->restrictions) : $oldRestriction = $oldService->restrictions);
		log_event("CASA_SERVICE_CHANGED",getServiceID($array),'','NEU: '.$newService->title.', '.$newService->description.', '.$username.', '.$newRestriction.', '.$newService->targetURL.', '.getServiceID($array).', '.$newService->location.' ALT: '.$oldService->title.', '.$oldService->description.', '.$username.', '.$oldRestriction.', '.$oldService->targetURL.', '.Request::get("service_id").', '.$oldService->location);
}
}
// if the casa server is not used only update the service in the data base
else{
	$newService = createNewService();
		$query = "UPDATE `casa_services`
					SET `title` = :title, 
						`description` = :description, 
						`url` = :url, 
						`serviceID` = :newServiceID, 
						`userrole` = :userrole, 
						`location` = :location, 
						`modified` = NOW()
					WHERE `serviceID` = :oldServiceID";
        $statement = DBManager::get()->prepare($query);
        $statement->bindValue(':title', $newService->title);
        $statement->bindValue(':description', $newService->description);
        $statement->bindValue(':url', $newService->targetURL);
		if (is_array($newService->restrictions)){
        $statement->bindValue(':userrole', implode(";",$newService->restrictions));
		}
		else{
	        $statement->bindValue(':userrole', $newService->restrictions);	
		}
       $statement->bindValue(':location', $newService->location);
       $statement->bindValue(':newServiceID', $newService->serviceID);
        $statement->bindValue(':oldServiceID', Request::get("service_id"));
       $statement->execute();
	
	echo MessageBox::success('Dienst erfolgreich geändert', array('Der Dienst' . $ParamList->title . ' wurde erfolgreich geändert.'), true);
	(is_array($newService->restrictions)  ? $newRestriction = implode(";",$newService->restrictions) : $newRestriction = $newService->restrictions);
	(is_array($oldService->restrictions)  ? $oldRestriction = implode(";",$oldService->restrictions) : $oldRestriction = $oldService->restrictions);
		
	log_event("CASA_SERVICE_CHANGED",$newService->serviceID,'','NEU: '.$newService->title.', '.$newService->description.', '.$username.', '.$newRestriction.', '.$newService->targetURL.', '.$newService->serviceID.', '.$newService->location.' ALT: '.$oldService->title.', '.$oldService->description.', '.$username.', '.$oldRestriction.', '.$oldService->targetURL.', '.$oldService->serviceID.', '.$oldService->location);
}
?>