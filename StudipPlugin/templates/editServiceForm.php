<script type="text/javascript" language="javascript">
function modify(form, checked, field){
if(checked){
	form[field].disabled=false;
	form[field].style.color='#000000';
}
else{
	form[field].disabled = true;
	form[field].style.color='#808080';
}
}
</script>        
<?php $locations = CasaPlugin::getLocations(); ?>
<!-- ><?php var_dump($_REQUEST);?> -->
<form id="edit_box" action="<?= URLHelper::getLink('#save') ?>" method="POST">
    <div style="text-align:center" id="settings" class="steel1">
		<h2 id="bd_basicsettings" class="steelgraulight">Dienst bearbeiten</h2>
  		<div><table width="100%">
            	<tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Name des Dienstes
			
			</td>
             		<td style="text-align: left" width="75%">   
						<input type="checkbox" onClick="modify(this.form, this.checked, 'service_name');"> 
						<input  type="text" name="service_name" value="<?= htmlReady($service_name) ?>" style="width: 80%; color:gray" disabled=true >
			<td style="align: left" width= "5%" ></td>
			</td>
          	</tr>
                <tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Adresse des Dienstes
			 
			</td>
             		<td style="text-align: left" width="80%">  <input type="checkbox" onClick="modify(this.form, this.checked, 'service_address');">   
						<input  type="text" pattern="^http://(.*)" name="service_address" value="<?= htmlReady($service_address) ?>" style="width: 80%; color:gray" disabled=true>
                       
			</td>
          	</tr>
                <tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Zielgruppe
			
			</td>
             		<td style="text-align: left" width="80%">
						 <input type="checkbox" onClick="modify(this.form, this.checked, 'service_restrictions');">
				<select  name="service_restrictions" style="width: 80%; color:gray" disabled=true>
            				<option 
					<?php
						if (in_array("autor",$_REQUEST["service_restrictions"]) && !in_array("dozent", $_REQUEST["service_restrictions"])){
							echo ' selected=true ';
						};
					?>
  					value="autor">Studenten</option>
           				<option 
					<?php
						if (in_array("dozent",$_REQUEST["service_restrictions"]) && !in_array("autor", $_REQUEST["service_restrictions"])){
							echo ' selected=true ';
						};
					?>
					value="dozent">Dozenten</option>
					<option 
					<?php
						if (in_array("autor",$_REQUEST["service_restrictions"]) && in_array("dozent", $_REQUEST["service_restrictions"])){
							echo ' selected=true ';
						};
					?>
					value="autor;dozent">Studenten und Dozenten</option>
        			</select>
                       

			</td>
        	</tr>

                <tr>
                        <td style="text-align: right; width: 20%; vertical-align: top;">
                        Verknüpfen mit Ort
                        <span style="color: red; font-size: 1.6em"></span>
                        </td>
             		<td style="text-align: left" width="80%; color:gray">
						<input type="checkbox" onClick="modify(this.form, this.checked, 'location');">
				<select  name="location" style="width: 80%" disabled=true>
            				<option 
					<?php
						if ($_REQUEST["location"] == NULL){
							echo ' selected=true ';
						};
					?>
  					value=" "></option>
           				<option 
					<?php
						if ($_REQUEST["location"] == $locations[0]){
							echo ' selected=true ';
						};
					?>
  					value="<? echo htmlready($locations[0])?>"><? echo htmlready($locations[0])?></option>
       				<option 
				<?php
					if ($_REQUEST["location"] == $locations[1]){
						echo ' selected=true ';
					};
				?>
				value="<? echo htmlready($locations[1])?>"><? echo htmlready($locations[1])?></option>
           			
        			</select>
                        

			</td>
                </tr>
      		<tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Beschreibung
			</td>
             		<td style="text-align: left; vertical-align: top;" width="80%">
						<input type="checkbox" onClick="modify(this.form, this.checked, 'service_description');">
			<textarea  name="service_description" style="width: 80%; height: 100px; color:gray" class="" disabled=true><?= htmlReady($service_description) ?></textarea>
			</td>
		</tr>
	</table>
	</div>
	<div style="text-align:center; padding: 15px">
 <input type="hidden" name="service_id" value="<?php echo Request::get("service_id") ?>">

    <?= makeButton('uebernehmen', 'input', false, 'updateService') ?>
    <?= makeButton('abbrechen', 'input', false, 'cancel') ?>
</form>
