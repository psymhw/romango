CREATE TABLE `etango`.`article` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100),
  `summary` VARCHAR(400),
  `article_date` DATE ,
  `author` VARCHAR(100),
  `category` VARCHAR(45),
  `body` TEXT,
  `active` INTEGER UNSIGNED,
  `admin_notes` VARCHAR(200),
  PRIMARY KEY(`id`)
)
ENGINE = MYISAM;
