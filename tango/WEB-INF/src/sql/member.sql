 CREATE TABLE `etango`.`member` (
`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
`last_name` VARCHAR( 45 ) NULL ,
`first_name` VARCHAR( 45 ) NULL ,
`email` VARCHAR( 100 ) NULL ,
`location_id` INT UNSIGNED NULL ,
`instructor` INT UNSIGNED NULL ,
`password` VARCHAR( 45 ) NULL ,
`host` INT UNSIGNED NULL ,
`description` VARCHAR( 2000 ) NULL ,
`photo` MEDIUMBLOB NULL ,
`admin` INT UNSIGNED NULL ,
`active` INT UNSIGNED NULL ,
`username` VARCHAR( 10 ) NULL ,
`caption` VARCHAR( 200 ) NOT NULL ,
`photoType` VARCHAR( 4 ) NOT NULL 
) ENGINE = MYISAM 