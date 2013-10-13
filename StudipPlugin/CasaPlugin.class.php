<?php

# Copyright (c)  2013  <philipp.lehsten@gmail.com>
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
	
require_once 'models/Service.class.php';
require_once 'CasaSettings.php';
	
class CasaPlugin extends AbstractStudIPStandardPlugin
{
	function __construct() {

		parent::__construct();

		# this plugin wants an own icon
                $this->setPluginiconname('images/casa_16_sw.png');

		# navigation

		$navigation = new PluginNavigation();
		$navigation->setDisplayname(_("Dienste"));
		$navigation->setLinkParam('viewServices');
		
		global $perm;
		$sem = Seminar::GetInstance($GLOBALS['SessSemName'][1]);
		$sem_id = $sem->id;                               
		$settings = CasaSettings::getCasaSettings();
		
		if ($perm->have_studip_perm($settings['addRole'], $sem_id)) {
		
		$subNavigation2 = new PluginNavigation();
		$subNavigation2->setDisplayname("Dienste hinzufügen");
		$subNavigation2->setLinkParam('viewAddService');
		$navigation->addSubMenu($subNavigation2);
		}
		
		if ($perm->have_studip_perm($settings['admRole'], $sem_id)) {
		        $subNavigation3 = new PluginNavigation();
                $subNavigation3->setDisplayname("Dienste verwalten");
                $subNavigation3->setLinkParam('viewAdministration');
                $navigation->addSubMenu($subNavigation3);
		}
		
		$subNavigation4 = new PluginNavigation();
		$subNavigation4->setDisplayname("Hilfe");
		$subNavigation4->setLinkParam('viewDisclaimer');
		$navigation->addSubMenu($subNavigation4);

		$this->setNavigation($navigation);

        }

	function actionViewAddServices(){
		$template_path = $this->getPluginPath().'/templates';
                $this->factory = new Flexi_TemplateFactory($template_path);
                $template = $this->factory->open('addServiceForm');
                $template->set_attribute('scount', 1);
		$template->set_attribute('locations', $this->getLocations());
                $template->set_attribute('serviceurl', $url);
                $template->set_attribute('servicetitle', $title);
                echo $template->render();
	}
	function actionViewDisclaimer(){
			
	}
	public function editService($service){
//		var_dump($service);
	}

	function actionShow ()
	{
//	var_dump($_REQUEST);
		if (Request::submitted('save')){
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
                if (Request::submitted('updateService')){
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

 		if (Request::submitted('editService')){
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
                if (Request::submitted('verifyServiceRemoval')){
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
                if (Request::submitted('removeService')){
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

		switch($_REQUEST['plugin_subnavi_params']){
                        case('viewAdministration'):
                                $template_path = $this->getPluginPath().'/templates';
                                $this->factory = new Flexi_TemplateFactory($template_path);
                                $template = $this->factory->open('viewAdministration');
				$array = $this->getServices();
				$template->set_attribute('services', $array);
                                echo $template->render();
                                break;
			case('viewAddService'):
				$this->actionViewAddServices();
				break;
			case('viewDisclaimer'):
                                $template_path = $this->getPluginPath().'/templates';
                                $this->factory = new Flexi_TemplateFactory($template_path);
                                $template = $this->factory->open('test');
                                echo $template->render();
				break;
			default: 
				break;
			case('viewServices'): 
                                $template_path = $this->getPluginPath().'/templates';
                                $this->factory = new Flexi_TemplateFactory($template_path);
                                $template = $this->factory->open('viewService');
				$services = $this->getServices();
				$locations = $this->getLocations();
				$template->set_attribute('settings', CasaSettings::getCasaSettings());
				$template->set_attribute('services', $services);
				$template->set_attribute('locations', $locations);
                  		echo $template->render();
                        	break;
                }

	}

	function getServices(){
			//  RaumID ermitteln
			$sem = Seminar::GetInstance($GLOBALS['SessSemName'][1]);
			$uniqueSemId = $sem->id;                                // SeminarID
			$nextDateDB = SeminarDB::getNextDate($uniqueSemId);     // naechster Termin vom Se$
			$uniqueDateId = $nextDateDB['termin'][0];               // ID vom naechsten Termin
			//  RaumID für den Fall das kein zukünftiger Termin existiert
			if (is_null($uniqueDateId)){
			//  RaumID für den ersten Termin ermitteln
				$firstDateDB = SeminarDB::getFirstDate($uniqueSemId);
				$uniqueDateId = $firstDateDB[0];
			}
			//  Wenn kein Raum genutzt wird entfaellt die ortsbezogene Suche
			if (is_null($uniqueDateId)){
				$locationName = NULL;
				$roomName = NULL;
			}
			//  Ermitteln der Klarnamen des Raumes und des Gebaeudes
			else{
				$db = DBManager::get();
				//  Suche nach der RaumID
				$resourceIdSearch = $db->query("SELECT resource_id FROM resources_assign WHERE assign_user_id = '$uniqueDateId'");
				$fetchedSearched = $resourceIdSearch->fetch();		// Suche sortieren
				$uniqueResourceId = $fetchedSearched[0];	        // RaumID des Termins
				//  Suche nach der GebaeudeID
				$parentIdSearch = $db->query("SELECT `parent_id` FROM `resources_objects` WHERE `resource_id` = '$uniqueResourceId'");
				$fetchedSearched = $parentIdSearch->fetch();		// Suche sortieren
				$parentId = $fetchedSearched[0];			// GebauedeID
				//  Suche der Klarnamen
				$nameSearch = $db->query("SELECT `name` FROM `resources_objects` WHERE `resource_id` = '$parentId' OR `resource_id` = '$uniqueResourceId'");
				$fetchedSearched = $nameSearch->fetch();
				$roomName =  $fetchedSearched[0];			// Raumname
				$fetchedSearched = $nameSearch->fetch();
				$locationName =  $fetchedSearched[0];			// Gebaeudename	
			}
			//    	$username = get_username($auth->auth["uid"]);
			$this->user = $this->getUser();
			$this->permission = $this->user->getPermission();
			//  Ermitteln der Nutzerrolle
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
			//  Anfrage an den Webservice stellen
			$settings = CasaSettings::getCasaSettings();
			if($settings['useCASA'] == 1){
			try{
				$wsdl = $settings['broker'];
				$options = array('cache_wsdl' => WSDL_CACHE_NONE );		//notwendig solange sich der WS aendern kann - sonst unnoetig
				$client = new SoapClient($wsdl,$options);
				//  Wenn keine Angaben zum Veranstaltungsort gefunden wurden wird dieser auf NULL gesetzt - die Suche erfolgt dann ohne Ortsbezug 
				if(is_null($loctaionName) && is_null($roomName)){
					$location = NULL;
				}
				else{
					$location = $roomName._(";").$locationName;
				}
				$ParamList = array(
                                //"userID" => $username,
					"lecture" => $uniqueSemId,
					"userRole" => $role,
					"location" => utf8_encode($location));
				$response = $client->getGUI($ParamList);
				$array = $response->return;
				//var_dump($array);
				if (is_array($array)){
					$services = array();
					foreach($array as $service){
						array_push($services, Service::createFromCasa($service));
					}
			     return $services;	
				}
				elseif(sizeof($array) == 1){
					return  array(Service::createFromCasa($array));
				}
				else {
					$newArray[0] = $array;
					return $newArray;
				}
			}
			catch (SoapFault $E){
                        	echo $E;
                        }
			// Anfrage an die datenbank stellen	
			
			}else{
						if(is_null($locationName) && is_null($roomName)){
							$locationString = "";
						}
						else{
							$locationString = "OR `location` = '{$locationName}' OR `location` = '{$roomName}'";
						}
						$roleParts = array();
						$userRole = explode ( ";" ,$role);
						foreach ($userRole as $val) {
						    $roleParts[] = "'%".$val."%'";
						}
						$partsString = implode(' OR `userrole` LIKE ', $roleParts);
				        $query = "SELECT *
						FROM `casa_services`
						WHERE (`lecture` = :lecture {$locationString}) AND (
							`userrole` LIKE {$partsString})";
				        $statement = DBManager::get()->prepare($query);
				        $statement->bindValue(':lecture', $uniqueSemId);
					    $statement->execute();
						$result = $statement->fetchall(PDO::FETCH_ASSOC);
						//var_dump($statement);
						if ($result == false || sizeof($result) == 0){
							$newArray[0] = $array;
							return $newArray;
						}
					
						else {
							$services = array();
							foreach($result as $service){
								array_push($services, Service::createFromDBEntry($service));
							}
					     return $services;				
						}
				}
			}

	function getLocations(){
  		$sem = Seminar::GetInstance($GLOBALS['SessSemName'][1]);
		$uniqueSemId = $sem->id;                                // SeminarID
		$nextDateDB = SeminarDB::getNextDate($uniqueSemId);     // naechster Termin vom Se$
                $uniqueDateId = $nextDateDB['termin'][0];               // ID vom naechsten Termin
                //  RaumID für den Fall das kein zukünftiger Termin existiert
                if (is_null($uniqueDateId)){
                //  RaumID für den ersten Termin ermitteln
              		$firstDateDB = SeminarDB::getFirstDate($uniqueSemId);
                	$uniqueDateId = $firstDateDB[0];
               	}
                //  Wenn kein Raum genutzt wird entfaellt die ortsbezogene Suche
                if (is_null($uniqueDateId)){
                	$locationName = NULL;
                	$roomName = NULL;
                }
                //  Ermitteln der Klarnamen des Raumes und des Gebaeudes
                        else{
                                $db = DBManager::get();
                                //  Suche nach der RaumID
                                $resourceIdSearch = $db->query("SELECT resource_id FROM resources_assign WHERE assign_user_id = '$uniqueDateId'");
                                $fetchedSearched = $resourceIdSearch->fetch();          // Suche sortieren
                                $uniqueResourceId = $fetchedSearched[0];                // RaumID des Termins
                                //  Suche nach der GebaeudeID
                                $parentIdSearch = $db->query("SELECT `parent_id` FROM `resources_objects` WHERE `resource_id` = '$uniqueResourceId'");
                                $fetchedSearched = $parentIdSearch->fetch();            // Suche sortieren
                                $parentId = $fetchedSearched[0];                        // GebauedeID
                                //  Suche der Klarnamen
                                $nameSearch = $db->query("SELECT `name` FROM `resources_objects` WHERE `resource_id` = '$parentId' OR `resource_id` = '$uniqueResourceId'");
                                $fetchedSearched = $nameSearch->fetch();
                                $roomName =  $fetchedSearched[0];                       // Raumname
                                $fetchedSearched = $nameSearch->fetch();
                                $locationName =  $fetchedSearched[0];                   // Gebaeudename
                        }
		return array($locationName, $roomName);
	}
}
?>
