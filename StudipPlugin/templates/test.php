<form id="edit_box" action="<?= URLHelper::getLink('#edit_box') ?>" method="POST">
    <textarea name="text" style="display: block; width: 80%; height: 8em;"><?=
        htmlReady($text)
    ?></textarea>
    <?= makeButton('uebernehmen', 'input', false, 'save') ?>
    <?= makeButton('abbrechen', 'input', false, 'cancel') ?>
</form>
