CREATE SCHEMA IF NOT EXISTS `PHARMACY`;
USE `PHARMACY`;

DROP TABLE IF EXISTS `customer_type`;
CREATE TABLE `customer_type`
(
    `customer_type_id` int          NOT NULL PRIMARY KEY,
    `name`             varchar(255) NOT NULL UNIQUE
);


DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE `user_permission`
(
    `user_permission_id` int          NOT NULL PRIMARY KEY,
    `name`               varchar(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `user_role_id` int          NOT NULL PRIMARY KEY,
    `name`         varchar(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS `user_role_permission`;
CREATE TABLE `user_role_permission`
(
    `id`              int          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_role`       varchar(255) NOT NULL,
    `user_permission` varchar(255) NOT NULL,

    FOREIGN KEY (`user_role`) REFERENCES `user_role` (`name`),
    FOREIGN KEY (`user_permission`) REFERENCES `user_permission` (`name`)
);

DROP TABLE IF EXISTS `user_credentials`;
CREATE TABLE `user_credentials`
(
    `id`        bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `created`   datetime(6)  DEFAULT NULL,
    `updated`   datetime(6)  DEFAULT NULL,
    `phone`     varchar(255) NOT NULL UNIQUE,
    `login`     varchar(64)  DEFAULT NULL UNIQUE,
    `email`     varchar(255) DEFAULT NULL UNIQUE,
    `password`  varchar(255) NOT NULL UNIQUE,
    `user_role` varchar(255) NOT NULL,
    `is_active` boolean      NOT NULL,
    FOREIGN KEY (`user_role`) REFERENCES `user_role` (`name`),
    UNIQUE INDEX `phone_UNIQUE` (`phone` ASC),
    UNIQUE INDEX `password_UNIQUE` (`password` ASC),
    UNIQUE INDEX `id_credentials_UNIQUE` (`id` ASC)
);

DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile`
(
    `id`                bigint       NOT NULL PRIMARY KEY,
    `created`           datetime(6)    DEFAULT NULL,
    `updated`           datetime(6)    DEFAULT NULL,
    `name`              varchar(255)   DEFAULT NULL,
    `surname`           varchar(255)   DEFAULT NULL,
    `birthdate`         date           DEFAULT NULL,
    `country`           varchar(64)    DEFAULT NULL,
    `city`              varchar(64)    DEFAULT NULL,
    `address`           varchar(255)   DEFAULT NULL,
    `index`             varchar(32)    DEFAULT NULL,
    `company`           varchar(255)   DEFAULT NULL,
    `personal_discount` decimal(19, 2) DEFAULT NULL,
    `customer_type`     varchar(255) NOT NULL,
    FOREIGN KEY (`customer_type`) REFERENCES `customer_type` (`name`),
    UNIQUE INDEX `id_user_UNIQUE` (`id` ASC),
    CONSTRAINT `fk_user_credentials_user`
        FOREIGN KEY (`id`)
            REFERENCES `user_credentials` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`
(
    `id`             bigint       NOT NULL  PRIMARY KEY,
    `created`        datetime(6)  DEFAULT NULL,
    `updated`        datetime(6)  DEFAULT NULL,
    `name`           varchar(255) NOT NULL,
    `surname`        varchar(255) DEFAULT NULL,
    UNIQUE INDEX `id_admin_UNIQUE` (`id` ASC),
    CONSTRAINT `fk_user_credentials_admin`
        FOREIGN KEY (`id`)
            REFERENCES `user_credentials` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
);
