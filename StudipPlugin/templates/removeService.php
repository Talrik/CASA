<?php
/**
 * This file contains the functionality to remove services from the system
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
	
// get the settings
$settings = CasaSettings::getCasaSettings();

// if casa server is used remove the service there first
if($settings['useCASA']){
	try{
		$wsdl = $settings['broker'];
        $options = array();
        $client = new SoapClient($wsdl,$options);
//	if ($location == NULL){
        	$ParamList = array(
			"PropertyKey" => "ID",
			"PropertyValue" => Request::get("service_id"));
        	$response = $client->removeService($ParamList);
        	$array = $response->return;
//	}
}
catch (SoapFault $E){
	echo $E;
}
}
// delete the service from the data base
$query = "DELETE FROM `casa_services`
WHERE `serviceID` = :service_id ";
        $statement = DBManager::get()->prepare($query);
        $statement->bindValue(':service_id', Request::get("service_id"));
        $statement->execute();
log_event("CASA_SERVICE_REMOVED",Request::get("service_id"),'','');

?>
