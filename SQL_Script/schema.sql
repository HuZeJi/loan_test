CREATE DATABASE IF NOT EXISTS chn_test
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE chn_test;

-- chn_test.client definition

CREATE TABLE `client` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `birthday` date NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_client_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- chn_test.loan_term definition

CREATE TABLE `loan_term` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `days` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO chn_test.loan_term
(id, description, days)
VALUES('1', '1 year', 365);
INSERT INTO chn_test.loan_term
(id, description, days)
VALUES('2', '1 mes', 31);
INSERT INTO chn_test.loan_term
(id, description, days)
VALUES('3', '1 semana', 7);

-- chn_test.`user` definition

CREATE TABLE `user` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`),
  UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- chn_test.loan definition

CREATE TABLE `loan` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `pending_amount` decimal(12,2) NOT NULL,
  `application_status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resolution_notes` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `resolution_date` date DEFAULT NULL,
  `client_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `loan_term_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_loan_client_id` (`client_id`),
  KEY `idx_loan_loan_term_id` (`loan_term_id`),
  KEY `idx_loan_user_id` (`user_id`),
  CONSTRAINT `fk_loan_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  CONSTRAINT `fk_loan_loan_term` FOREIGN KEY (`loan_term_id`) REFERENCES `loan_term` (`id`),
  CONSTRAINT `fk_loan_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- chn_test.loan_payment definition

CREATE TABLE `loan_payment` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_date` date NOT NULL,
  `payment_method` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `loan_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_loan_payment_loan_id` (`loan_id`),
  KEY `idx_loan_payment_user_id` (`user_id`),
  CONSTRAINT `fk_loan_payment_loan` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`),
  CONSTRAINT `fk_loan_payment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
