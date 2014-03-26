<?php
/**
 * This file contains the CasaPlugin
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
	
require_once 'models/Service.class.php';
require_once 'CasaSettings.php';
	
/**
 *
 * The CasaPlugin acts as central handler for the service related actions 
 * It is embedded into the events sites if it is activated.
 * 
 * @author  Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 */	
class CasaPlugin extends AbstractStudIPStandardPlugin
{
	/**
	*   Basic constructor for the plugin
	*/	
	function __construct() {

		if(Request::get('request')!= NULL){
			log_event("CASA_SERVICE_USED",Request::get("ID"),'','');
			exit();
		}
		
		parent::__construct();


		# this plugin wants an own icon
        $this->setPluginiconname('images/casa_16_sw.png');

		# create the navigation
		$navigation = new PluginNavigation();
		$navigation->setDisplayname(_("Dienste"));
		$navigation->setLinkParam('viewServices');
		
		# get the settings for the plugin
		global $perm;
		$sem = Seminar::GetInstance($GLOBALS['SessSemName'][1]);
		$sem_id = $sem->id;                               
		$settings = CasaSettings::getCasaSettings();
		
		# if the user has the role to add services add the tab 
		if ($perm->have_studip_perm($settings['addRole'], $sem_id)) {	
			$subNavigation2 = new PluginNavigation();
			$subNavigation2->setDisplayname("Dienste hinzufügen");
			$subNavigation2->setLinkParam('viewAddService');
			$navigation->addSubMenu($subNavigation2);
		}
		
		# if the user has the role to modify services add the tab 
		if ($perm->have_studip_perm($settings['admRole'], $sem_id)) {
		    $subNavigation3 = new PluginNavigation();
            $subNavigation3->setDisplayname("Dienste verwalten");
            $subNavigation3->setLinkParam('viewAdministration');
            $navigation->addSubMenu($subNavigation3);
		}
		
		# add the help page tab
		$subNavigation4 = new PluginNavigation();
		$subNavigation4->setDisplayname("Hilfe");
		$subNavigation4->setLinkParam('viewDisclaimer');
		$navigation->addSubMenu($subNavigation4);

		$this->setNavigation($navigation);

    }

	/**
	*   Main handler for the different user interfaces
	*/	
	function actionShow ()
	{
		# handle the tabs
		switch($_REQUEST['plugin_subnavi_params']){
			# administration of services tab
           	case('viewAdministration'):
				$this->actionViewAdministration();
				break;
			# add service tab
			case('viewAddService'):
				$this->actionViewAddServices();
				break;
			# help tab
			case('viewDisclaimer'):
				$this->actionViewDisclaimer();
				break;
			# do nothing 	
			default: 
				break;
			# view services tab	
			case('viewServices'): 
				$this->actionViewServices();
                break;
        }
		# handle the requests
		# save a service - from save button on the add service page
		if (Request::submitted('save')){
			$this->actionSaveService();
		}
		# update a service - from update button on the edit service page
        if (Request::submitted('updateService')){
			$this->actionUpdateService();
		}
		# edit a service - from edit button on the administration page
 		if (Request::submitted('editService')){
			$this->actionEditService();
		}
		# verify removal of this service - from remove button on the administration page
		if (Request::submitted('verifyServiceRemoval')){
			$this->actionVerifyServiceRemoval();
        }
		# remove the service - from remove button on the verify removal page
        if (Request::submitted('removeService')){
			$this->actionRemoveService();
        }
	}
	
	/**
	*   Sends the specified service attributes from add service page to the save service page
	*/	
	function actionSaveService(){
		$template_path = $this->getPluginPath().'/templates';
    	$this->factory = new Flexi_TemplateFactory($template_path);
    	$template = $this->factory->open('saveService');
    	$template->set_attribute('username', $this->getUser()->username);
        $template->set_attribute('lecture_id', Request::get('cid'));
        $template->set_attribute('service_name', Request::get('service_name'));
		$template->set_attribute('service_address', Request::get('service_address'));
		$template->set_attribute('service_description', Request::get('service_description'));
        $template->set_attribute('service_restrictions', Request::get('service_restrictions'));
		$template->set_attribute('location', Request::get('location'));
		echo $template->render();
	}
	/**
	*   Sends the specified service attributes from edit service page to the update service page
	*/
	function actionUpdateService(){
        $template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('updateService');
        $template->set_attribute('username', $this->getUser()->username);
        $template->set_attribute('lecture_id', Request::get('cid'));
        $template->set_attribute('service_name', Request::get('service_name'));
        $template->set_attribute('service_address', Request::get('service_address'));
        $template->set_attribute('service_description', Request::get('service_description'));
        $template->set_attribute('service_restrictions', Request::get('service_restriction'));
        $template->set_attribute('location', Request::get('location'));
        echo $template->render();
	}
	/**
	*   Sends the specified service attributes from administrate service page to the edit service page
	*/
	function actionEditService(){
        $template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('editServiceForm');
        $template->set_attribute('service_author', Request::get('service_author'));
        $template->set_attribute('service_name', Request::get('service_name'));
        $template->set_attribute('service_address', Request::get('service_address'));
        $template->set_attribute('service_description', Request::get('service_description'));
        $template->set_attribute('service_restrictions', Request::get('service_restrictions'));
        $template->set_attribute('location', Request::get('location'));
        echo $template->render();
	}
	/**
	*   Sends the specified service attributes from verify service removal page to the remove service page
	*/
	function actionRemoveService(){
        $template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('removeService');
        $template->set_attribute('username', $this->getUser()->username);
        $template->set_attribute('service_id', Request::get('service_id'));
        $template->set_attribute('service_name', Request::get('service_name'));
        $template->set_attribute('service_address', Request::get('service_address'));
        $template->set_attribute('service_description', Request::get('service_description'));
        $template->set_attribute('service_restrictions', Request::get('service_restriction'));
        $template->set_attribute('location', Request::get('location'));
        echo $template->render();
	}
	/**
	*   Sends the specified service attributes from administrate service page to the verify service removal page
	*/
	function actionVerifyServiceRemoval(){
    	$template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('removeServiceForm');
        $template->set_attribute('username', $this->getUser()->username);
        $template->set_attribute('lecture_id', Request::get('cid'));
        $template->set_attribute('service_name', Request::get('service_name'));
        $template->set_attribute('service_address', Request::get('service_address'));
        $template->set_attribute('service_description', Request::get('service_description'));
        $template->set_attribute('service_restrictions', Request::get('service_restrictions'));
        $template->set_attribute('location', Request::get('location'));
        echo $template->render();
	}
	/**
	*   Views the administartion page
	*/
	function actionViewAdministration(){
        $template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('viewAdministration');
		$array = $this->getServices();
		$template->set_attribute('services', $array);
        echo $template->render();
	}

	/**
	*   Views the add service page
	*/
	function actionViewAddServices(){
		$template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('addServiceForm');
        $template->set_attribute('scount', 1);
		$template->set_attribute('locations', $this->getAllLocations());
        $template->set_attribute('serviceurl', $url);
        $template->set_attribute('servicetitle', $title);
        echo $template->render();
	}
	
	/**
	*   Views the help page
	*/
	function actionViewDisclaimer(){
        $template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('hilfe');
        echo $template->render();
	}
	

	/**
	*   Views the service page
	*/
	function actionViewServices(){
        $template_path = $this->getPluginPath().'/templates';
        $this->factory = new Flexi_TemplateFactory($template_path);
        $template = $this->factory->open('viewService');
		$services = $this->getServices();
		$locations = $this->getNextLocations();
		$template->set_attribute('settings', CasaSettings::getCasaSettings());
		$template->set_attribute('services', $services);
		$template->set_attribute('locations', $locations);
  		echo $template->render();
	}
	/**
	*   Retrieves the Services for the current location and event according to the user role
	*   @see Service
	*   @return array<Service>
	*/	
	function getServices(){
			//  get RoomID 
			$sem = Seminar::GetInstance($GLOBALS['SessSemName'][1]);
			$uniqueSemId = $sem->id;                                // SeminarID
			$nextDateDB = SeminarDB::getNextDate($uniqueSemId);     // next date
			$uniqueDateId = $nextDateDB['termin'][0];               // next date ID
			//  RoomID if there is no upcomming event
			if (is_null($uniqueDateId)){
			//  RoomID for the first event
				$firstDateDB = SeminarDB::getFirstDate($uniqueSemId);
				$uniqueDateId = $firstDateDB[0];
			}
			//  if there is no location for this event at all cancel location-based search
			if (is_null($uniqueDateId)){
				$locationName = NULL;
				$roomName = NULL;
			}
			//  get real names for room and location
			else{
				$db = DBManager::get();
				//  search for RoomID
				$resourceIdSearch = $db->query("SELECT resource_id FROM resources_assign WHERE assign_user_id = '$uniqueDateId'");
				$fetchedSearched = $resourceIdSearch->fetch();		// sort search
				$uniqueResourceId = $fetchedSearched[0];	        // RoomID of the date
				//  search for BuildingID 
				$parentIdSearch = $db->query("SELECT `parent_id` FROM `resources_objects` WHERE `resource_id` = '$uniqueResourceId'");
				$fetchedSearched = $parentIdSearch->fetch();		// sort search
				$parentId = $fetchedSearched[0];			// BuildingID
				//  get the real names
				$nameSearch = $db->query("SELECT `name` FROM `resources_objects` WHERE `resource_id` = '$parentId' OR `resource_id` = '$uniqueResourceId'");
				$fetchedSearched = $nameSearch->fetch();
				$roomName =  $fetchedSearched[0];			// room name
				$fetchedSearched = $nameSearch->fetch();
				$locationName =  $fetchedSearched[0];			// building name
			}
			//  get the user 
			$this->user = $this->getUser();
			$this->permission = $this->user->getPermission();
			//  get the user role
			if($this->permission->hasStudentPermission()){
				$role = 'autor';
			}
			if($this->permission->hasTutorPermission()){ 
				if ($role != null){
					$role = $role._(";").'tutor';
				}
				else{
					$role = 'tutor';
				}
			}
			if($this->permission->hasTeacherPermission()){
				if ($role != null){
                             		$role = $role._(";").'dozent';
                              	}
                                else{
                                       	$role = 'dozent';
                                }
			}
			if($this->permission->hasAdminPermission()){
				if ($role != null){
                                	$role = $role._(";").'admin';
                                }
                                else{
                                	$role = 'admin';
                               	}
			}
			//  if the CASA remote server is used try to get the services from the server
			$settings = CasaSettings::getCasaSettings();
			if($settings['useCASA'] == 1){
			try{
				$wsdl = $settings['broker'];
				$options = array('cache_wsdl' => WSDL_CACHE_NONE );		// practical as long as the interface can change
				$client = new SoapClient($wsdl,$options);
				// cancel location related search if no location information was found
				if(is_null($loctaionName) && is_null($roomName)){
					$location = NULL;
				}
				else{
					$location = $roomName._(";").$locationName;
				}
				// create the request
				$ParamList = array(
                                //"userID" => $username,
					"lecture" => $uniqueSemId,
					"userRole" => $role,
					"location" => utf8_encode($location));
				// send request	
				$response = $client->getGUI($ParamList);
				$array = $response->return;
				//var_dump($array);
				// multiple service results
				if (is_array($array)){
					$services = array();
					foreach($array as $service){
						array_push($services, Service::createFromCasa($service));
					}
			     return $services;	
				}
				// only one service result
				elseif(sizeof($array) == 1){
					return  array(Service::createFromCasa($array));
				}
				// no services found
				else {
					$newArray[0] = $array;
					return $newArray;
				}
			}
			// catch errors
			catch (SoapFault $E){
               //         	echo $E;
                        }
			}
			//  if the CASA remote server is not used - get services from local data base
			else{
				// if there is no location information leave it blank
				if(is_null($locationName) && is_null($roomName)){
					$locationString = "";
				}
				else{
					$locationString = "OR `location` = '{$locationName}' OR `location` = '{$roomName}'";
				}
				// collect the user roles
				$roleParts = array();
				$userRole = explode ( ";" ,$role);
				foreach ($userRole as $val) {
					$roleParts[] = "'%".$val."%'";
				}
				$partsString = implode(' OR `userrole` LIKE ', $roleParts);
				// create SQL query
				$query = "SELECT *
					FROM `casa_services`
					WHERE (`lecture` = :lecture {$locationString}) AND (
							`userrole` LIKE {$partsString})";
				$statement = DBManager::get()->prepare($query);
				$statement->bindValue(':lecture', $uniqueSemId);
				// get query results
				$statement->execute();
				$result = $statement->fetchall(PDO::FETCH_ASSOC);
				//var_dump($statement);
				// if nothing is found return empty array
				if ($result == false || sizeof($result) == 0){
					$newArray[0] = $array;
					return $newArray;
				}
				// return array cointaining Service objects
				else {
					$services = array();
					foreach($result as $service){
						array_push($services, Service::createFromDBEntry($service));
					}
					return $services;				
				}
			}
		}
	/**
	* Retrieves the location names for the next event of the current course
	*   @return array string
	*/		
	function getNextLocations(){
  		$sem = Seminar::GetInstance($GLOBALS['SessSemName'][1]);
		$uniqueSemId = $sem->id;                                // 
		$nextDateDB = SeminarDB::getNextDate($uniqueSemId);     // next date
                $uniqueDateId = $nextDateDB['termin'][0];       // ID of next date
				//  RoomID if there is no upcomming event
				if (is_null($uniqueDateId)){
				//  RoomID for the first event
		    		$firstDateDB = SeminarDB::getFirstDate($uniqueSemId);
                	$uniqueDateId = $firstDateDB[0];
               	}
				//  if there is no location for this event at all cancel location-based search
				if (is_null($uniqueDateId)){
                	$locationName = NULL;
                	$roomName = NULL;
                }
				//  get real names for room and location
		            else{
                                $db = DBManager::get();
                                //  search for RoomID
                                $resourceIdSearch = $db->query("SELECT resource_id FROM resources_assign WHERE assign_user_id = '$uniqueDateId'");
                                $fetchedSearched = $resourceIdSearch->fetch();          // sort search
                                $uniqueResourceId = $fetchedSearched[0];                // RoomID of date
                                //  search for BuildingID
                                $parentIdSearch = $db->query("SELECT `parent_id` FROM `resources_objects` WHERE `resource_id` = '$uniqueResourceId'");
                                $fetchedSearched = $parentIdSearch->fetch();            // sort search
                                $parentId = $fetchedSearched[0];                        // BuildingID
                                //  search for real names
                                $nameSearch = $db->query("SELECT `name` FROM `resources_objects` WHERE `resource_id` = '$parentId' OR `resource_id` = 									'$uniqueResourceId'");
                                $fetchedSearched = $nameSearch->fetch();
                                $roomName =  $fetchedSearched[0];                       // RoomID
                                $fetchedSearched = $nameSearch->fetch();
                                $locationName =  $fetchedSearched[0];                   // BuildingID
                        }
		return array($locationName, $roomName);
	}
	
	/**
	* Retrieves the location names for all locations that are related to this course
	*/
	function getAllLocations(){                               
        $db = DBManager::get();
		$course_id = Request::get('cid');// SeminarID
        //  search for all related RoomIDs
        $resourceIdSearch = $db->query("SELECT DISTINCT `resources_assign`.`resource_id`
										FROM `termine` INNER JOIN `resources_assign`
										WHERE ((
										`termine`.`range_id` LIKE '$course_id') 
										AND (`resources_assign`.`assign_user_id` LIKE `termine`.`termin_id`))");
		$fetchedSearched = $resourceIdSearch->fetch();
		if($fetchedSearched){          
		$i = 0;					
		while ($fetchedSearched){
			$resource_ids[$i] = $fetchedSearched[0];
			$i++;
            $fetchedSearched = $resourceIdSearch->fetch();           
		}
		// create request string for parentID search
		foreach ($resource_ids as $val) {
			$resourceParts[] = "'%".$val."%'";
		}
		$resourceString = implode(' OR `resource_id` LIKE ', $resourceParts);
		// search for parentIDs
        $parentIdSearch = $db->query(	"SELECT DISTINCT `parent_id`
										FROM `resources_objects`
										WHERE (
										`resource_id` LIKE {$resourceString})");
		$fetchedSearched = $parentIdSearch->fetch();         
		$i = 0;					
		while ($fetchedSearched){
			$parent_ids[$i] = $fetchedSearched[0];
			$i++;
			$fetchedSearched = $parentIdSearch->fetch();          
		}
		
		$ids = array_merge($parent_ids, $resource_ids);
		// create request dtring with all related reseourceIDs (rooms + buildings)
		foreach ($ids as $val) {
			$nameParts[] = "'%".$val."%'";
		}
		$namesString = implode(' OR `resource_id` LIKE ', $nameParts);
		// search for names
        $nameSearch = $db->query(	"SELECT DISTINCT `name`
										FROM `resources_objects`
										WHERE (
										`resource_id` LIKE {$namesString})");
		$fetchedSearched = $nameSearch->fetch();          
		$i = 0;					
		while ($fetchedSearched){
			$names[$i] = $fetchedSearched[0];
			$i++;
			$fetchedSearched = $nameSearch->fetch();          
		}								
		return $names;
		}
		return array();
	}
}
?>
