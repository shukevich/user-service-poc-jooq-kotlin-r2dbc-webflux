USE `PHARMACY`;

INSERT INTO `customer_type` (customer_type_id, name)
VALUES (1, 'Customer as a person'),
       (2, 'Customer as a company'),
       (3, 'Not a Customer'),
       (4, 'No type');
INSERT INTO `user_role` (user_role_id, name)
VALUES (1, 'Customer'),
       (2, 'Sales manager'),
       (3, 'Administrator'),
       (4, 'No role');
INSERT INTO `user_permission` (user_permission_id, name)
VALUES (1, 'Create any user'),
       (2, 'Read any user'),
       (3, 'Edit any user'),
       (4, 'Delete any user'),
       (5, 'Write a single user'),
       (6, 'Read a single user'),
       (7, 'Edit a single user'),
       (8, 'Delete a single user');
INSERT INTO `user_role_permission`(id, user_role, user_permission)
VALUES (1, 'Customer', 'Read any user'),
       (2, 'Customer', 'Write a single user'),
       (3, 'Customer', 'Read a single user'),
       (4, 'Customer', 'Edit a single user'),
       (5, 'Customer', 'Delete a single user'),
       (6, 'Sales manager', 'Write a single user'),
       (7, 'Sales manager', 'Read a single user'),
       (8, 'Sales manager', 'Edit a single user'),
       (9, 'Sales manager', 'Delete a single user'),
       (10, 'Administrator', 'Create any user'),
       (11, 'Administrator', 'Read any user'),
       (12, 'Administrator', 'Edit any user'),
       (13, 'Administrator', 'Delete any user');

INSERT INTO `user_credentials`(id, phone, login, email, password, user_role, is_active)
VALUES (2, '123456789', 'ola', 'ivanova@mail.ru', '$2y$12$S398UdXtTCgsTfaKsx4W9./I4HmpFr9/4iGKvomLSGr4INqF6aTSK',
        'Customer', 1),
       (1, '987654321', 'ivan', 'ivanov@mail.ru', '$2y$12$OJXAfug3wbcs8Vjj8nbk7OjGKrS0YzBHZjHvfGF6JnKXEMVCH4zPW',
        'Administrator', 1);

INSERT INTO `user_profile` (id, name, surname, customer_type)
VALUES (2, 'Olga', 'Ivanova', 'Customer as a person');

INSERT INTO `administrator` (id, name, surname)
VALUES (1, 'Ivan', 'Ivanov');
