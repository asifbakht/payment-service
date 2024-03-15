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