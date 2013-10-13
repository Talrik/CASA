<?php
//var_dump($_REQUEST);
$settings = CasaSettings::getCasaSettings();
if($settings['useCASA']){
	try{
		$wsdl = $settings['broker'];
        $options = array();
        $client = new SoapClient($wsdl,$options);
	if ($location == NULL)
	{
        	$ParamList = array(
			"PropertyKey" => "ID",
			"PropertyValue" => Request::get("service_id"));
        	$response = $client->removeService($ParamList);
        	$array = $response->return;
	}
}
catch (SoapFault $E){
	echo $E;
}
}

$query = "DELETE FROM `casa_services`
WHERE `serviceID` = :service_id ";
        $statement = DBManager::get()->prepare($query);
        $statement->bindValue(':service_id', Request::get("service_id"));
        $statement->execute();

?>
