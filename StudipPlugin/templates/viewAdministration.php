<?php 
/*
*
*	broker4gui.php - Anzeige der durch den Broker4GUI bereitgestellten Dienste im Stud.IP
*
*/
//  Beginn der Ausgabe der Dienste
//var_dump($services);
//echo PluginEngine::getURL("CasaPlugin",array(),"show",false);

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
$scount = sizeof($services);
if ($scount == 1 && is_null($services[0])){
    echo MessageBox::info('Leider sind aktuell keine Dienste mit dieser Veranstaltung oder mit diesem Ort verknüpft');
}
else{



    echo'
	<script type="text/javascript">
	links = new Array('.sizeof($serviceurl).');
    ';
    //  links[] wird mit den URLs gefüllt
       if (sizeof($services)>=1){
        for ($z =0; $z<sizeof($services); $z++){
        echo 'links['._($z).']="'._($services[$z]->targetURL).'";';
        }
    }
    //  Definition der in JavaScript verwendeten Funktion
    echo'
        function toggle(control){
            var elem = document.getElementById("block"+control);
            if(elem.style.display == "none"){
                document.getElementById("klappen"+control).childNodes[2].nodeValue = "ausblenden";
                document.getElementById("klappenImg"+control).src = "./../../assets/images/forumgraurunt2.png";
                elem.style.display = "block";
                elem.src = links[control];
            }
            else{
                elem.style.display = "none";
                document.getElementById("klappen"+control).childNodes[2].nodeValue = "anzeigen";
                document.getElementById("klappenImg"+control).src = "./../../assets/images/forumgrau2.png";
            }
        }
	function viewEdit(control){
	
	}
    //
        </script>';
		
echo '<div style="text-align:center" id="settings" class="steel1">
		<h2 id="bd_basicsettings" class="steelgraulight">Dienste zu dieser Veranstaltung und diesem Ort</h2></div><table><tr>';
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
//    for ($j = 0; $i<sizeof($services[i]->restrictions);$j++){echo $services[$i]->restrictions[$j];}
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

echo '<table class="index_box"  style="width: 100%;">';
    //  Ausgabe der Dienstnamen in eigenen Zeilen mit Text zum Anzeigen und im neuen Fenster oeffnen
    for ($i = 0; $i < $scount; $i++){
        echo'
            <tr><td class="topic" colspan="2">
            <img src="./../../assets/images/icons/16/white/admin.png" border="0" alt="Dienste"  title="Dienste">
        ';
        echo'   <b>'._($services[$i]->title).'</b>';
        echo'
            </td></tr>
            <tr><td class="steel1" colspan="3">
            <a id="klappen'._($i).'" href="javascript:toggle('._($i).')">
            <img id="klappenImg'._($i).'" src="./../../assets/images/forumgrau2.png" alt="Objekt aufklappen">
                anzeigen</a> / 
        ';
            echo '<a href="'._(urldecode($services[$i]->targetURL)).'" target="_blank">Neu &ouml;ffnen</a>';
        echo'
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
        <script type="text/javascript">
        </script>
        </tr></td></table>
    ';
}
?>
