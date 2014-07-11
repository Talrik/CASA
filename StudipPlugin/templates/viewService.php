<?php 
/**
 * This file contains the interface to view services
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

 /**
 * returns the full name (first name + last name) of the user for the user name
 * @param string $userID user name in the studip system
 */
function getFullUserName($userID){
$db = DBManager::get();
$resourceIdSearch = $db->query("SELECT Vorname, Nachname FROM auth_user_md5 WHERE username = '$userID'");
$fetchedSearched = $resourceIdSearch->fetch();		// Suche sortieren
return $fetchedSearched[0].' '.$fetchedSearched[1];
}

// if there are no services - view an info box
$scount = sizeof($services);
if ($scount==1 && is_null($services[0])){
    echo MessageBox::info('Leider sind aktuell keine Dienste mit dieser Veranstaltung oder mit diesem Ort verknüpft', array('Name der Veranstaltung: '.Seminar::GetInstance($GLOBALS['SessSemName'][1])->getName(), 'Orte: '.implode(",",$locations)), true);
}
else{
    echo'
	<script type="text/javascript">
	links = new Array('.sizeof($serviceurl).');
    ';
    //  we need an array with the urls
    if (sizeof($services)>=1){
        for ($z =0; $z<sizeof($services); $z++){
	echo 'links['._($z).']= new Object();';		
	echo 'links['._($z).']["URL"]="'._($services[$z]->targetURL).'";';
	echo 'links['._($z).']["ID"]="'._($services[$z]->serviceID).'";';
        }
    }
    //  java script function for the toggeling
    echo'
		function toggle(control){
            var elem = document.getElementById("block"+control);
            if(elem.style.display == "none"){
                document.getElementById("klappentext"+control).childNodes[0].nodeValue = "ausblenden";
			    document.getElementById("klappenImg"+control).src = "./../../assets/images/forumgraurunt2.png";
                elem.style.display = "block";
                elem.src = links[control]["URL"];
				$.post( "'.PluginEngine::getURL('CasaPlugin').'", { request: "viewService", ID: links[control]["ID"] } );
				}
            else{
                elem.style.display = "none";
                document.getElementById("klappentext"+control).childNodes[0].nodeValue = "anzeigen";
                document.getElementById("klappenImg"+control).src = "./../../assets/images/forumgrau2.png";
            }
        }
        </script>
        <table class="index_box"  style="width: 100%;">
    ';
    //  view the services
    for ($i = 0; $i < $scount; $i++){
        echo'
            <tr><td class="topic" colspan="3">
            <img src="./../../assets/images/icons/16/white/admin.png" border="0" alt="Dienste"  title="Dienste">
        ';
		$userName = getFullUserName($services[$i]->provider); 
		$userLink = URLHelper::getLink('about.php?username='.$services[$i]->provider);
	   echo'   <b>'._($services[$i]->title).'</b> - <small>geteilt von <a href='._($userLink).' >'._($userName).'</a></small>';
        echo'
            </td></tr>
            <tr><td class="steel1" width="16">
            <a id="klappen'._($i).'" href="javascript:toggle('._($i).')">
            <img id="klappenImg'._($i).'" src="./../../assets/images/forumgrau2.png" alt="Objekt aufklappen">
            </a></td>	
        ';
        echo '<td width="150" class="steel1"><a id="klappen'._($i).'" href="javascript:toggle('._($i).')"><span id="klappentext'._($i).'">anzeigen</span></a> / <a 					href="'._(urldecode($services[$i]->targetURL)).'" target="_blank">Neu &ouml;ffnen</a>';
		echo '<td class="steel1">'._($services[$i]->description).'</td></tr>';

		echo'<tr><td colspan="3">
            <br /><iframe id="block'._($i).'" style="display: none" src="" width="98%" height="500" name="'._("Dienste").'" frameborder="0">
            <p>Ihr Browser kann leider keine eingebetteten Frames anzeigen:
            Sie k&ouml;nnen die eingebettete Seite &uuml;ber den folgenden Verweis
            aufrufen: <a href="'._(urldecode($serviceurl[$i])).'">"'._("Dienst").'"</a></p>
            </iframe>
        ';
        echo'</td></tr>
        ';
    };
    echo'
        </tr></td></table>
    ';
}

?>
