CREATE TABLE IF NOT EXISTS `customer` (
    `id` VARCHAR(50) NOT NULL PRIMARY KEY,
    `first_name` varchar(50) NOT NULL,
    `last_name` varchar(50) NOT NULL,
    `email` varchar(80) NOT NULL,
    `date_of_birth` varchar(50) NOT NULL,
    `active` smallint DEFAULT false,
    `phone_number` varchar(12) DEFAULT NULL,
    `itin_or_ssn` varchar(10) NOT NULL,
    `date_created` timestamp DEFAULT NOW(),
    `date_updated` timestamp NOT NULL DEFAULT NOW() ON UPDATE NOW()
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;



CREATE TABLE IF NOT EXISTS `payment_method` (
    `id` VARCHAR(50) NOT NULL PRIMARY KEY,
    `customer_id` varchar(50) NOT NULL,
    `payment_name` varchar(50) DEFAULT NULL,
    `payment_type` varchar(10) DEFAULT NULL,
    `card_holder_name` varchar(50) DEFAULT NULL,
    `card_number` varchar(16) DEFAULT NULL,
    `expiration_month` SMALLINT NULL,
    `expiration_year` SMALLINT NULL,
    `cvv` SMALLINT,
    `card_type` varchar(10) DEFAULT NULL,
    `account_number` varchar(12) DEFAULT NULL,
    `routing_number` varchar(9) DEFAULT NULL,
    `account_holder_name` varchar(50) DEFAULT NULL,
    `bank_name` varchar(50) DEFAULT NULL,
    `date_created` timestamp DEFAULT NOW(),
    `date_updated` timestamp NOT NULL DEFAULT NOW() ON UPDATE NOW()
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE IF NOT EXISTS `payment` (
    `id` VARCHAR(50) NOT NULL PRIMARY KEY,
    `customer_id` varchar(50) NOT NULL,
    `payment_method_id` varchar(50) NOT NULL,
    `amount` DECIMAL(12, 2) NOT NULL,
    `status` varchar(12) DEFAULT NULL,
    `processing_time` varchar(30) DEFAULT NULL,
    `version` smallint(5) DEFAULT 1,
    `date_created` timestamp DEFAULT NOW(),
    `date_updated` timestamp NOT NULL DEFAULT NOW() ON UPDATE NOW()
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE IF NOT EXISTS `shedlock` (
  name VARCHAR(64) NOT NULL,
  lock_until TIMESTAMP(3) NOT NULL,
  locked_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  locked_by VARCHAR(255) NOT NULL,
  PRIMARY KEY (name)
)