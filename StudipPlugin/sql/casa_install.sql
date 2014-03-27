
CREATE TABLE IF NOT EXISTS `casa_services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `description` text,
  `url` varchar(255) NOT NULL,
  `userrole` varchar(64) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `lecture` varchar(64) DEFAULT NULL,
  `serviceID` varchar(64) NOT NULL,
  `createdBy` varchar(64) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO log_actions (
		`action_id` ,
		`name` ,
		`description` ,
		`info_template` ,
		`active` ,
		`expires`
		)
	VALUES (
		'8e16ef418efca51bf99536ff9c4fc01b', 
		'CASA_COURSE_SERVICE_CREATED', 
		'Ein Dienst mit Veranstaltungsbezug wurde eingetragen', 
		'%user erzeugte den Dienst %affected für den Kurs %coaffected (%info)', '1', '0'
);
INSERT INTO log_actions (
		`action_id` ,
		`name` ,
		`description` ,
		`info_template` ,
		`active` ,
		`expires`
		)
	VALUES (
		'0934a79fb5dd0347d6b3ed893a9c3949', 
		'CASA_LOCATION_SERVICE_CREATED', 
		'Ein Dienst mit Ortsbezug wurde eingetragen', 
		'%user erzeugte den Dienst %affected für den Ort %coaffected (%info)', '1', '0'
	);
INSERT INTO log_actions (
		`action_id` ,
		`name` ,
		`description` ,
		`info_template` ,
		`active` ,
		`expires`
		)
	VALUES (
		'f469908c2d5f71c5637c61b58506ce3c', 
		'CASA_SERVICE_CHANGED', 
		'Ein Dienst wurde bearbeitet', 
		'%user bearbeitete den Dienst %affected (%info)', '1', '0'
	);
INSERT INTO log_actions (
		`action_id` ,
		`name` ,
		`description` ,
		`info_template` ,
		`active` ,
		`expires`
		)
	VALUES (
		'62f5cc46b58e1862bb352499e41f7aa0', 
		'CASA_SERVICE_REMOVED', 
		'Ein Dienst wurde entfernt', 
		'%user hat den Dienst mit der ID %affected entfernt (%info)', '1', '0'
	);
INSERT INTO log_actions (
		`action_id` ,
		`name` ,
		`description` ,
		`info_template` ,
		`active` ,
		`expires`
		)
	VALUES (
		'2780961516cea2fe59c8343c87534aa6', 
		'CASA_SERVICE_USED', 
		'Ein Dienst wurde genutzt', 
		'%user hat den Dienst mit der ID %affected aufgerufen (%info)', '1', '0'
	);