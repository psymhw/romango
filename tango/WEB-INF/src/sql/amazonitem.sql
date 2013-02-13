CREATE TABLE `etango`.`AmazonItem` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45),
  `amazon_id` VARCHAR(15),
  `lastUpdate` DATE,
  `author` VARCHAR(45),
  `title` VARCHAR(150),
  `period` VARCHAR(45),
  `smallImageURL` VARCHAR(300),
  `mediumImageURL` VARCHAR(300),
  `avaliability` VARCHAR(45),
  `et_review` TEXT,
  `featured` INTEGER UNSIGNED,
  `singer` VARCHAR(100),
  `et_rating` INTEGER UNSIGNED,
  `detail_page_url` VARCHAR(300),
  `publisher` VARCHAR(100),
  `pub_date` VARCHAR(20),
  `see_also` VARCHAR(300),
  PRIMARY KEY(`id`)
)
ENGINE = MYISAM;
