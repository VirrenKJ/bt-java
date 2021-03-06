CREATE TABLE IF NOT EXISTS `global_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `assigned_id` int DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `issue` (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_id` int NOT NULL,
  `category_id` int NOT NULL,
  `reproducibility` varchar(255) NOT NULL,
  `severity` varchar(255) NOT NULL,
  `priority` varchar(255) NOT NULL,
  `profile_id` int NOT NULL,
  `assigned_id` int NOT NULL,
  `reported_by_id` int NOT NULL,
  `summary` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `steps_to_reproduce` text DEFAULT NULL,
  `add_info` text DEFAULT NULL,
  `document_id` int DEFAULT NULL,
  `view_status` varchar(255) NOT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `project` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `inherit_category_flag` tinyint(1) NOT NULL DEFAULT '0',
  `view_status` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `role` (`role_id`, `role_name`, `description`, `delete_flag`, `created_by`, `updated_by`) VALUES
(1, 'Admin', NULL, 0, 0, 0),
(2, 'Manager', NULL, 0, 0, 0),
(3, 'Developer', NULL, 0, 0, 0),
(4, 'Tester', NULL, 0, 0, 0),
(5, 'Viewer', NULL, 0, 0, 0);

CREATE TABLE IF NOT EXISTS `system_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `platform` varchar(255) NOT NULL,
  `os_name` varchar(255) NOT NULL,
  `os_version` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `user` (
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

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `company` (
   `id` int NOT NULL AUTO_INCREMENT,
   `company_db_detail_id` int DEFAULT NULL,
   `user_id` int DEFAULT NULL,
   `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
   `db_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
   `db_uuid` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
   `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
   `contact_number` bigint NOT NULL,
   `industry_type` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
   `pin_code` int NOT NULL,
   `state` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
   `city` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
   `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
   `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `created_by` int NOT NULL,
   `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_by` int NOT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `company_db_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `db_url` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `db_username` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `db_password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `delete_flag` tinyint(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
