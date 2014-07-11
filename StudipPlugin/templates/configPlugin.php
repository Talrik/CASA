<?php
/**
 * This file contains the interface for the CasaAdminPlugin
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

use Studip\Button;

/**
* default value for the CASA Server
*/
$settings['broker'] = 'http://elbe5.uni-rostock.de:8080/GUI_Broker_StudIP/GUI_Broker_StudIP?WSDL';

/**
* gets the already stored settings
*/
$settings = CasaSettings::getCasaSettings();
?>

<h2>CASA-Einstellungen</h2>

<? if (!$casa_active) : ?>

<fieldset>

<legend>Import / Export von Diensten</legend>

<label>
	<a href= "download_services">
<?= Button::create('Alle Dienste exportieren', 'download_services') ?>
	</a>
	<form enctype="multipart/form-data" action="upload_services" method="POST">
	    <!-- MAX_FILE_SIZE muss vor dem Dateiupload Input Feld stehen -->
	    <input type="hidden" name="MAX_FILE_SIZE" value="30000" />
	    <!-- Der Name des Input Felds bestimmt den Namen im $_FILES Array -->
	    Diese Datei hochladen: <input name="userfile" type="file" />
	    <input type="submit" value="Send File" />
	</form>
</label>

</fieldset>

<form action="<?= PluginEngine::getLink("casaadminplugin/settings") ?>" method="post">
	
	<fieldset>

	<legend>Wie wollen Sie das CASA-PLugin nutzen?</legend>
	<label>
	<?= _('Modus:') ?>
	<select required name="casaConfig[useCASA]" >
	      <option <?php if ($settings[useCASA]) : ?>selected=true' <?php endif; ?>value="1">CASA mit Server verwenden</option>
	      <option <?php if (!$settings[useCASA]) :?>selected=true' <?php endif; ?> value="0">CASA nur lokal verwenden</option>
	    </select>
	</label>

	</fieldset>
<fieldset>

<legend>Wo befindet sich Ihr CASA-Server? (Nur bei Verwendung mit Server!)</legend>

<label>
<?= _('Broker:') ?>
<input  type="text" name="casaConfig[broker]" value="<?= htmlReady($settings['broker']) ?>">
</label>

</fieldset>
<fieldset>
<legend>Notwendige Berechtigungen</legend>

<label>
<?= _('Rolle zum Hinzuf&uuml;gen:') ?>
<select required name="casaConfig[addRole]" >
      <option <?php if ($settings[addRole] == 'root') : ?>selected=true' <?php endif; ?>value="root">root</option>
      <option <?php if ($settings[addRole] == 'admin') :?>selected=true' <?php endif; ?> value="admin">admin</option>
      <option <?php if ($settings[addRole] == 'dozent') :?>selected=true' <?php endif; ?>value="dozent">dozent</option>
      <option <?php if ($settings[addRole] == 'tutor') :?>selected=true' <?php endif; ?> value="tutor">tutor</option>
      <option <?php if ($settings[addRole] == 'autor') : ?>selected=true' <?php endif; ?>value="autor">autor</option>
      <option <?php if ($settings[addRole] == 'user') :?>selected=true' <?php endif; ?> value="user">user</option>
    </select>
</label>
</br>
<label>
<?= _('Rolle zum Verwalten:') ?>
<select required name="casaConfig[admRole]" >
      <option <?php if ($settings[admRole] == 'root') : ?>selected=true' <?php endif; ?>value="root">root</option>
      <option <?php if ($settings[admRole] == 'admin') :?>selected=true' <?php endif; ?> value="admin">admin</option>
      <option <?php if ($settings[admRole] == 'dozent') :?>selected=true' <?php endif; ?>value="dozent">dozent</option>
      <option <?php if ($settings[admRole] == 'tutor') :?>selected=true' <?php endif; ?> value="tutor">tutor</option>
      <option <?php if ($settings[admRole] == 'autor') : ?>selected=true' <?php endif; ?>value="autor">autor</option>
      <option <?php if ($settings[admRole] == 'user') :?>selected=true' <?php endif; ?> value="user">user</option>
    </select>
</label>

</fieldset>

<div class="button-group">

<?= makeButton('uebernehmen', 'input', false, 'save') ?>
<?= makeButton('abbrechen', 'input', false, 'cancel') ?>
</div>
</form>

<? else : ?>

<dl>
<dt>CASA-Server</dt> <dd><?= htmlReady($settings['server']) ?></dd>
<dt>Broker</dt> <dd><?= htmlReady($settings['broker']) ?></dd>
</dl>

<? endif ?>
