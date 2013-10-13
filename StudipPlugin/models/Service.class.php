<?php
class Service
{
	var $title; 
	var $description;
	var $provider;
	var $restrictions = array();
	var $targetURL;
	var $serviceID;
	var $lecture;
	var $location;

	static function createFromValues($title,$description,$provider,$restrictions,$targetURL,$serviceID,$lecture,$location )
	{	
		$service = new self($title,$description,$provider,$restrictions,$targetURL,$lecture,$location);
		if ($serviceID == $service->serviceID || strlen($serviceID) == 0 || $serviceID == 0 || $serviceID == NULL){
			return $service;
		}
		else{
			$service->serviceID = $serviceID;
			return $service;
		}
	}
	
	static function createFromCASA($WsResult){
		$title  = utf8_decode($WsResult->title);
		$description = utf8_decode($WsResult->description);
		$provider = $WsResult->provider;
		if (is_array($WsResult->restrictions)){
		$restrictions = $WsResult->restrictions;
		}else{
			$restrictions = array($WsResult->restrictions);	
		}
		$targetURL = $WsResult->targetURL;
		$entry = $WsResult->properties->entry;
		for ($i=0; $i<sizeof($entry);$i++){
			if ($entry[$i]->key == 'ID'){
			$serviceID = $entry[$i]->value;
			}
			if ($entry[$i]->key == 'StudIP_ID'){
			$lecture = $entry[$i]->value;
			}
			if ($entry[$i]->key == 'StudIP_Location'){
			$location = utf8_decode($entry[$i]->value);
			}
		}
		$service = self::createFromValues($title,$description,$provider,$restrictions,$targetURL,$serviceID,$lecture,$location);
		return $service;
	}
    
	static function createFromDBEntry($DBEntry)
    {
        $title = $DBEntry["title"];
		$description = $DBEntry["description"];
		$targetURL = $DBEntry["url"];
		$restrictions = explode(";",$DBEntry["userrole"]);
		$provider = $DBEntry["createdBy"];
		$serviceID = $DBEntry["serviceID"];
		$lecture = $DBEntry["lecture"];
		$location = $DBEntry["location"];
		$service = new self($title,$description,$provider,$restrictions,$targetURL,$lecture,$location);
		if ($serviceID == $service->serviceID){
			return $service;
		}else 
		{
			$service->serviceID = $serviceID;
			return $service;
		}
	}
	
	private function __construct($title,$description,$provider,$restrictions,$targetURL,$lecture,$location ){
        $this->title = $title;
		$this->description = $description;
		$this->targetURL = $targetURL;
		$this->restrictions = $restrictions;
		$this->provider = $provider;
		$this->serviceID = $serviceID;
		$this->lecture = $lecture;
		$this->location = $location;
		$this->serviceID = $this->createServiceID($this);
	
	}
	private function createServiceID($service){
		return hash('ripemd160', 
			$service->title
			.$service->description
			.$service->provider
			.$service->restrictions
			.$service->targetURL
			.$service->lecture
			.$service->location);
	}

}
?>