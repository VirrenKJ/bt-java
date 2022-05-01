CREATE DATABASE IF NOT EXISTS `bt_master` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `bt_master`;

CREATE TABLE `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_db_detail_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `db_name` varchar(255) NOT NULL,
  `db_uuid` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contact_number` bigint NOT NULL,
  `industry_type` varchar(255) NOT NULL,
  `pin_code` int NOT NULL,
  `state` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `company_db_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `db_url` varchar(255) NOT NULL,
  `db_username` varchar(255) NOT NULL,
  `db_password` varchar(255) NOT NULL,
  `delete_flag` tinyint(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `company_employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `password_reset_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `token` varchar(45) NOT NULL,
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `role` (`role_id`, `role_name`, `description`, `delete_flag`, `created_by`, `updated_by`) VALUES
(1, 'Admin', NULL, 0, 0, 0),
(2, 'Manager', NULL, 0, 0, 0),
(3, 'Developer', NULL, 0, 0, 0),
(4, 'Tester', NULL, 0, 0, 0),
(5, 'Viewer', NULL, 0, 0, 0);

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL UNIQUE,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `user_session` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `login_time` timestamp NOT NULL,
  `logout_time` timestamp NULL DEFAULT NULL,
  `session_time` int DEFAULT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
