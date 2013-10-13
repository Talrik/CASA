<?php
class CreateDatabase extends DBMigration
{

    function description ()
    {
        return 'Creates neccessary tables for CASA plugin';
    }

    function up ()
    {
        DBManager::get()->exec("CREATE TABLE IF NOT EXISTS `casa_services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `description` text,
  `url` varchar(255) NOT NULL,
  `userrole` varchar(64) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `lecture` varchar(64) DEFAULT NULL,
  `serviceXML` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)");
    }

    function down ()
    {
        DBManager::get()->exec("DROP TABLE IF EXISTS `casa_services`");
    }
}