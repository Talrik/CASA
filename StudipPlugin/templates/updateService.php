<?php
$settings = CasaSettings::getCasaSettings();
//var_dump($_REQUEST);
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

function createNewService(){
$query = "SELECT *
FROM `casa_services`
WHERE (`serviceID` = :oldServiceID)";
$statement = DBManager::get()->prepare($query); 
$statement->bindValue(':oldServiceID', Request::get("service_id"));
$statement->execute();
$result = $statement->fetch(PDO::FETCH_ASSOC);
$oldService = Service::createFromDBEntry($result);
//
if(strlen(Request::get("service_name"))!= 0){
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
	$restriction=implode(";",$oldService->restrictions);	
}

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

if($settings['useCASA'] == 1){
	try{	
		$wsdl = $settings['broker'];
		$options = array();
        $client = new SoapClient($wsdl,$options);
		$newService = createNewService();
		echo("TEST0");
//		var_dump($newService);
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
if (is_null($array)){
		echo MessageBox::error('Beim Ändern des Dienstes ist ein Fehler aufgetreten', array('Der Dienst wurde nicht geändert.'), true);
		
}else{
	$newService = Service::createFromCASA($array);
//	var_dump($newService);
	echo("TEST1");
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
}
}else{
	$newService = createNewService();
//	var_dump($newService);
//	echo("TEST2");
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
	
	
	
}
?>
