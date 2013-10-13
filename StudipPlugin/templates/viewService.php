<?php 
/*
*
*	viewService.php - Anzeige der durch CASA bereitgestellten Dienste im Stud.IP
*
*/
//  Beginn der Ausgabe der Dienste
//var_dump($services);
//var_dump(Seminar::GetInstance($GLOBALS['SessSemName'][1]));
$scount = sizeof($services);
if ($scount==1 && is_null($services[0])){
    echo MessageBox::info('Leider sind aktuell keine Dienste mit dieser Veranstaltung oder mit diesem Ort verknüpft', array('Name der Veranstaltung: '.Seminar::GetInstance($GLOBALS['SessSemName'][1])->getName(), 'Orte: '.$locations[0].', '.$locations[1]), true);
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
        </script>
        <table class="index_box"  style="width: 100%;">
    ';
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
