<form id="edit_box" action="<?= URLHelper::getLink('#removeService') ?>" method="POST">
    <div style="text-align:center" id="settings" class="steel1">
		<h2 id="bd_basicsettings" class="steelgraulight">Dienst löschen</h2>
  		<div><table width="100%">
            	<tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Name des Dienstes
			</td>
             		<td style="text-align: left" width="80%">    <input  type="text" name="service_name" value="<?= htmlReady($service_name) ?>" style="width: 80%" disabled="disabled">
			</td>
          	</tr>
                <tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Adresse des Dienstes
			</td>
             		<td style="text-align: left" width="80%">    <input  type="url" name="service_address" value="<?= htmlReady($service_address) ?>" style="width: 80%"  disabled="disabled">
			</td>
          	</tr>
                <tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Zielgruppe
			</td>
             		<td style="text-align: left" width="80%">
				<select  name="service_restrictions" style="width: 80%"  disabled="disabled">
            				<option <?php
if (in_array("autor",$_REQUEST["service_restrictions"])){
echo ' selected="selected" ';
};
?>
  value="autor">Studenten</option>
           				<option value="dozent" selected>Dozenten</option>
            				<option value="autor;dozent">Studenten und Dozenten</option>
        			</select>
			</td>
        	</tr>

                <tr>
                        <td style="text-align: right; width: 20%; vertical-align: top;">
                        Verknüpfen mit Ort
                        </td>
                        <td style="text-align: left" width="80%">    <input  type="text" name="location" value="<?php echo htmlReady($location);?>" style="width: 80%"  disabled="disabled">
                        </td>
                </tr>

      		<tr>
             		<td style="text-align: right; width: 20%; vertical-align: top;">
                 	Beschreibung
			</td>
             		<td style="text-align: left" width="80%">
			<textarea  name="service_description" style="width: 80%; height: 100px;" class=""  disabled="disabled" ><?= htmlReady($service_description) ?></textarea>
			</td>
		</tr>
	</table>
	</div>
	<div style="text-align:center; padding: 15px">
 <input type="hidden" name="service_id" value="<?php echo Request::get("service_id") ?>">

    <?= makeButton('entfernen', 'input', false, 'removeService') ?>
    <?= makeButton('abbrechen', 'input', false, 'cancel') ?>
</form>
