CREATE TABLE `etango`.`event` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `member_id` INTEGER UNSIGNED,
  `location_id` INTEGER UNSIGNED,
  `type_id` INTEGER UNSIGNED,
  `day_of_week` VARCHAR(10),
  `regular` INTEGER UNSIGNED,
  `start_date` DATE ,
  `last_date` DATE,
  `entry_date` DATE ,
  `expire_date` DATE ,
  `description` TEXT,
  `photo` MEDIUMBLOB,
  `caption` VARCHAR(200),
  photoType` VARCHAR( 4 ) NOT NULL ,
  `title` VARCHAR( 200 ) NOT NULL,
  PRIMARY KEY(`id`)
)
ENGINE = MYISAM;
