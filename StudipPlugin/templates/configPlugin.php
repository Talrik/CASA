<?php
$settings['broker'] = 'http://elbe5.uni-rostock.de:8080/GUI_Broker_StudIP/GUI_Broker_StudIP?WSDL';
$settings = CasaSettings::getCasaSettings();
//var_dump($settings);
?>

<h2>CASA-Einstellungen</h2>

<? if (!$casa_active) : ?>
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
