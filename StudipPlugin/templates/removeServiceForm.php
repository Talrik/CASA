<?php
/**
 * This file contains the form for the removal of services - if the user verifies this the service is deleted by the removeService.php
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
	
// 
?>

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
