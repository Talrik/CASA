<?php

/**
 * This file contains the CasaAdminPlugin
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

require_once 'CasaSettings.php';
require_once 'models/Service.class.php';

/**
 *
 * The CasaAdminPlugin handles the configuration for the CasaPlugin. 
 * It is accessible by root in the plugin section.
 * 
 * @author  Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 */
class CasaAdminPlugin extends StudipPlugin implements SystemPlugin
{
	/**
	* Setup plugin an create navigation
	*/
    function __construct()
    {
        parent::__construct();
        $this->setupNavigation();
    }
	
	
	/**
	* Show the configuration site and load the current settings
	*/
    function show_action()
    {
        $this->requireRoot();

        Navigation::activateItem("/admin/config/casaadmin");
		
        $parameters = array(
           'plugin' => $this
         , 'settings' => CasaSettings::getCasaSettings()
             );

        $factory = new Flexi_TemplateFactory(dirname(__FILE__).'/templates');
        echo $factory->render('configPlugin'
                            , $parameters
                            , $GLOBALS['template_factory']->open('layouts/base_without_infobox')
        );
		
    }
	/**
	* Create the navigation tab 
	*/
    private function setupNavigation()
    {
        global $perm;
        if (!$perm->have_perm("root")) {
            return;
        }

        $url = PluginEngine::getURL('casaadminplugin/show');
        $navigation = new Navigation(_('CASA-Admin'), $url);
        $navigation->setImage(Assets::image_path('icons/16/white/test.png'));
        $navigation->setActiveImage(Assets::image_path('icons/16/black/test.png'));

        Navigation::addItem('/admin/config/casaadmin', $navigation);
    }
	/**
	* Check the new settings and store them 
	*/
    function settings_action()
    {
        $this->requireRoot();

        if (Request::method() !== 'POST') {
            throw new AccessDeniedException();
        }

        # get settings
        $settings = Request::getArray("casaConfig");

        # validate them
        list($valid, $err) = $this->validateSettings($settings);
        if (!$valid) {
            $this->redirect('show', compact('err'));
            return;
        }

        # store them
        CasaSettings::setCasaSettings($settings);
	
		# inform about success
        $this->redirect('show', array('info' => _('Casa-Einstellungen aktualisiert.')));
    }
	
	/**
	* Download of all existing services
	*/
    function download_services_action()
    {
        $this->requireRoot();
		
		$query = "SELECT *
			FROM `casa_services`";
		$statement = DBManager::get()->prepare($query);
		$statement->execute();
		$result = $statement->fetchall(PDO::FETCH_ASSOC);
//		var_dump($statement);
//		var_dump($result);	
		$services = array();
		foreach($result as $service){
//			echo json_encode($service, JSON_FORCE_OBJECT);		
			array_push($services, $service /*Service::createFromDBEntry($service)*/);
		}
	    $var = json_encode($services, JSON_FORCE_OBJECT);	;    //  $var enthält einfach alles, was später in der Datei stehen soll, die der User runterlädt

		$date = getdate();
		
	    header('Content-Type: application/json');    //  möglich, dass du hier auch text/plain wählen kannst
	    header('Content-Length: ' . strlen($var));
	    header('Content-Disposition: attachment; filename="services-'.$date[0].'.json"');

	print $var; 
    }
	
	/**
	* Upload of new services
	*/
    function upload_services_action()
    {
        $this->requireRoot();
		$filecontent = (array)json_decode(file_get_contents($_FILES['userfile']['tmp_name']));
		var_dump($filecontent);
		$services = array();
		foreach($filecontent as $service){
//			echo json_encode($service, JSON_FORCE_OBJECT);
			array_push($services,Service::createFromValues($service->title,$service->description,$service->createdBy,$service->userrole,$service->url,$service->serviceID,$service->lecture,$service->location ));
		}
				var_dump($services);
        $parameters = array(
           'plugin' => $this
         , 'settings' => CasaSettings::getCasaSettings()
		 , 'services' => $services	   
             );
	         $factory = new Flexi_TemplateFactory(dirname(__FILE__).'/templates');
	         echo $factory->render('importServiceView'
	                             , $parameters
	                             , $GLOBALS['template_factory']->open('layouts/base_without_infobox')
	         );	
//		var_dump($services);
		
    }
	
	
	/**
	* Checks the settings and returns errors
	* @todo connection test to the server
	* @todo WSDL validation 
	* 
	* @param array $settings Associative array with the configuration parameters
	* @return array 
	*/
    private function validateSettings($settings)
    {
        $errors = array();

        # Server is reachable
			//TODO
        # Broker has a valid WSDL
			//TODO
		# useCASA has to be either true or false		
        if (($settings['useCASA'] != 1) && ($settings['useCASA'] != 0))  {
            $errors[] = _("CASA Modus ist nicht spezifiziert");
        }

        return array(sizeof($errors) === 0, $errors);
    }
	/**
	* Redirect to the specified $action
	* @param string $action specified action (e.g show) 
	*/
    private function redirect($action)
    {
        header("Location: " . PluginEngine::getURL("casaadminplugin/$action"));
    }
	
	/**
	* Make sure the user is root  
	*/
    private function requireRoot()
    {
        global $perm;
        if (!$perm->have_perm("root")) {
            throw new AccessDeniedException();
        }
    }
	
}