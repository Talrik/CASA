<?php 
/**
 * This file views the import interface for admins to handle add imported services
 *
 * Copyright (c)  2014  <philipp.lehsten@gmail.com>
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
 * @copyright  2014 Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 * @since      File available since Release 1.3
 */
	
/**
 * returns the serviceID of a given service
 * @param Service $service Service to retrieve the ID from
 * @return string serviceID or NULL if there is none
 */
function getServiceID($service){
	if ($service->serviceID != NULL){
		return $service->serviceID;
	}else{
	$entry = $service->properties->entry;
	for ($i=0; $i<sizeof($entry);$i++){
		if ($entry[$i]->key == 'ID'){
		return $entry[$i]->value;
		}
	}
}
}

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



// view message box if there are no services
$scount = sizeof($services);
if ($scount == 1 && is_null($services[0])){
    echo MessageBox::info('Leider sind aktuell keine Dienste mit dieser Veranstaltung oder mit diesem Ort verknüpft');
}
// else view services
else{



    echo'
	<script type="text/javascript">
	links = new Array('.sizeof($serviceurl).');
    ';
    //  array for the urls
       if (sizeof($services)>=1){
        for ($z =0; $z<sizeof($services); $z++){
        echo 'links['._($z).']="'._($services[$z]->targetURL).'";';
        }
    }
    //  toggle function in java script
    echo'
        function toggle(control){
            var elem = document.getElementById("block"+control);
            if(elem.style.display == "none"){
                document.getElementById("klappentext"+control).childNodes[0].nodeValue = "ausblenden";
				document.getElementById("klappenImg"+control).src = "./../../assets/images/forumgraurunt2.png";
                elem.style.display = "block";
                elem.src = links[control];
            }
            else{
                elem.style.display = "none";
                document.getElementById("klappentext"+control).childNodes[0].nodeValue = "anzeigen";
                document.getElementById("klappenImg"+control).src = "./../../assets/images/forumgrau2.png";
            }
        }
        </script>';
// view the services in the table		
echo '<div style="text-align:center" id="settings" class="steel1">
		<h2 id="bd_basicsettings" class="steelgraulight">Dienste importieren</h2></div><table><tr>';
        printf("<td class=\"steel\" width=\"30%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\"><b>%s</b></font></td>", _("Titel"));
    printf("<td class=\"steel\" width=\"10%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\"><b>%s</b></font></td>", _("Autor"));
    printf("<td class=\"steel\" width=\"50%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\"><b>%s</b></font></td>", _("Beschreibung"));
    printf("<td class=\"steel\" width=\"9%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\"><b>%s</b></font></td>", _("URL"));
    printf("<td class=\"steel\" width=\"9%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\"><b>%s</b></font></td>", _("Beschränkung"));
    printf("<td class=\"steel\" width=\"9%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\"><b>%s</b></font></td>", _("Aktion"));
echo"</tr>";
for ($i=0;$i < $scount;$i++ ){
echo"<tr>";
    printf("<td class=\"steel\" width=\"30%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\">%s</font></td>", _($services[$i]->title));
    printf("<td class=\"steel\" width=\"10%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\">%s</font></td>", _($services[$i]->provider));
    printf("<td class=\"steel\" width=\"30%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\">%s</font></td>", _($services[$i]->description));
    printf("<td class=\"steel\" width=\"30%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\">%s</font></td>", _($services[$i]->targetURL));
    echo   '<td class="steel" width="30%" align="center" valign="bottom"><font size="-1">';
if (is_array($services[$i]->restrictions)){ 
       foreach($services[$i]->restrictions as $value){
                echo $value."\n";
        };
}
else{ 
echo $services[$i]->restrictions;
}
   echo '</font></td>';
    printf("<td class=\"steel\" width=\"30%%\" align=\"center\" valign=\"bottom\"><font size=\"-1\">
		<a  href=\"javascript:toggle(%s)\">
			<img id=\"klappenImg\" src=\"./../../assets/images/icons/16/black/search.png\" alt=\"Objekt aufklappen\">
		</a>",_($i));

	echo'<form id="edit_service" action="'. URLHelper::getLink("#editService").'" method="POST">
		 <input type="hidden" name="service_id" value="'.getServiceID($services[$i]).'">
		<input type="hidden" name="service_name" value="'.$services[$i]->title.'">
                <input type="hidden" name="service_address" value="'.$services[$i]->targetURL.'">
                <input type="hidden" name="service_description" value="'.$services[$i]->description.'">
                <input type="hidden" name="service_author" value="'.$services[$i]->provider.'">
                <input type="hidden" name="location" value="'.$services[$i]->location.'">
	';
if (is_array($services[$i]->restrictions)){
	foreach($services[$i]->restrictions as $value){
		echo'<input type="hidden" name="service_restrictions[]" value="'.$value.'">';
	};
}
else{
                echo'<input type="hidden" name="service_restrictions[]" value="'.$services[$i]->restrictions.'">';
}
	echo'<input type="image" src="./../../assets/images/icons/16/black/edit.png" name="editService"></form>';

 echo'<form id="remove_service" action="'. URLHelper::getLink("#verifyServiceRemoval").'" method="POST">
                 <input type="hidden" name="service_id" value="'.getServiceID($services[$i]).'">
                <input type="hidden" name="service_name" value="'.$services[$i]->title.'">
                <input type="hidden" name="service_address" value="'.$services[$i]->targetURL.'">
                <input type="hidden" name="service_description" value="'.$services[$i]->description.'">
                <input type="hidden" name="service_author" value="'.$services[$i]->provider.'">
                <input type="hidden" name="location" value="'.$services[$i]->location.'">
        ';
if (is_array($services[$i]->restrictions)){
        foreach($services[$i]->restrictions as $value){
                echo'<input type="hidden" name="service_restrictions[]" value="'.$value.'">';
        };
}else{
 echo'<input type="hidden" name="service_restrictions[]" value="'.$services[$i]->restrictions.'">';
}
        echo'<input type="image" src="./../../assets/images/icons/16/black/trash.png" name="verifyServiceRemoval"></form>';

    echo '</font></td>';


}
echo"</tr></table>";    
// view services one below the other
echo '<table class="index_box"  style="width: 100%;">';
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
		echo '<tr><td colspan="3">
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
