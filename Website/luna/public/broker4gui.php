<?php
	/*
	 *	CHANGED!!!
	 *	broker4gui.php - Anzeige des durch den Broker4GUI bereitgestellten Dienst im Stud.IP
	 *	Path:	.../studip/public
	 */
	
	
	require '../lib/bootstrap.php';
	
    page_open(array("sess" => "Seminar_Session", "auth" => "Seminar_Default_Auth", "perm" => "Seminar_Perm", "user" => "Seminar_User"));
	$auth->login_if($again && ($auth->auth["uid"] == "nobody"));
	
	include ('lib/seminar_open.php'); // initialise Stud.IP-Session
	
	$sem = Seminar::GetInstance($SessSemName[1]);
	
	checkObject(); // do we have an open object?
	checkObjectModule("Broker4GUI");
	object_set_visit_module("Broker4UI");
	
	mark_public_course();
	
	PageLayout::setHelpKeyword("Basis.Broker4GUI");
	PageLayout::setTitle($SessSemName["header_line"]. " - " ."Dienste");
//	if ($scount > 1){
	Navigation::activateItem('/course/Dienste/viewService');
//	}
//	else{
//	Navigation::activateItem('/course/'.$servicetitle.'/viewService');
//	}
	// Start of Output
	include ('lib/include/html_head.inc.php'); // Output of html head
	include ('lib/include/header.php');   // Output of Stud.IP head

    $next_date = $sem->getNextDate();	
	if ($next_date) {
		$uniqueSemId = $sem->id;							// SeminarID
		$nextDateDB = SeminarDB::getNextDate($uniqueSemId);	// nächster Termin vom Seminar
		$uniqueDateId = $nextDateDB['termin'][0];			// ID vom nächsten Termin
		$db = DBManager::get();
		$resourceIdSearch = $db->query("SELECT resource_id FROM resources_assign WHERE assign_user_id = '$uniqueDateId'");
		$fetchedSearched = $resourceIdSearch->fetch();		// Suche sortieren
		$uniqueResourceId = $fetchedSearched[0];			// RaumID vom nächsten Termin
		
/*		
         ehemals im Stud.IP: Test ob Raumsteuerung für Raum vorhanden
         Jetzt:	Übergabe der Verantwortlichkeit an den Broker
*/         
            $uniqueLink = $serviceurl[1];
			echo '
			<script type="text/javascript">
			links = new Array('.sizeof($serviceurl).');
			';
			if (sizeof($serviceurl)>1){
				for ($z =0; $z<sizeof($serviceurl); $z++){
					echo 'links['._($z).']="'._(urldecode($serviceurl[$z])).'";';
				}
			}
			else {
			echo 'links[0]="'._(urldecode($serviceurl)).'";';
			}
			echo'
			function setLink (value){
			document.getElementById("unique").src = links[setLink.arguments[0]];
			return;
			}
			var klappstatus = 0;
                        function setLink (){
                                document.getElementById("unique").src = "'._($uniqueLink).'";
                                return;
                        }
                        function klappen(value){
                        if (klappstatus == 0){
                                klappstatus = 1;
                                document.getElementById("klappen"+klappen.arguments[0]).childNodes[2].nodeValue = "ausblenden";
                                document.getElementById("klappenImg"+klappen.arguments[0]).src = "assets/images/forumgraurunt2.png";
				document.getElementById("unique").src = links[klappen.arguments[0]];
                                document.getElementById("unique").style.display = \'\';
                        } else {
                                klappstatus = 0;
                                document.getElementById("klappen"+klappen.arguments[0]).childNodes[2].nodeValue = "anzeigen";
                                document.getElementById("klappenImg"+klappen.arguments[0]).src = "assets/images/forumgrau2.png";
                                document.getElementById("unique").style.display = \'none\';
                        }
                        return;
                        }
			</script>
			 <table class="index_box"  style="width: 100%;">
			';
			for ($i = 0; $i < $scount; $i++)
			{
			echo'
				<tr><td class="topic" colspan="2">
                        	<img src="assets/images/icons/16/white/admin.png" border="0" alt="Dienste"  title="Dienste">
			';
			if ($scount>1){
			echo'	<b>'._(utf8_decode($servicetitle[$i])).'</b>';
			}
			else{
			echo'	<b>'._(utf8_decode($servicetitle)).'</b>';
			}
			echo'
                        	</td></tr>
                        	<tr><td class="steel1" colspan="3">
                        	<a id="klappen'._($i).'" href="javascript:klappen('._($i).')">
                        	<img id="klappenImg'._($i).'" src="assets/images/forumgrau2.png" alt="Objekt aufklappen" title="Objekt aufklappen">
                        	anzeigen</a> / ';
				if ($scount >1){
					echo '<a href="'._(urldecode($serviceurl[$i])).'" target="_blank">Neu &ouml;ffnen</a>';
				}else{
					echo '<a href="'._(urldecode($serviceurl)).'" target="_blank">Neu &ouml;ffnen</a>';
				}

			echo'	</td></tr>
			';};
			echo'         
			<tr>
			<td class="steel1" colspan="3">
			<iframe id="unique" src="" width="98%" height="500" name="'._("Dienste").'" frameborder="0">
			<p>Ihr Browser kann leider keine eingebetteten Frames anzeigen:
			Sie k&ouml;nnen die eingebettete Seite &uuml;ber den folgenden Verweis
			aufrufen: <a href="'._($uniqueLink).'">"'._("Dienste").'"</a></p>
			</iframe>
			<script type="text/javascript">
			</script>
			</tr></td></table>
			';

		//}
	}
	    
	
include ('lib/include/html_end.inc.php');
page_close();
