CREATE TYPE role_type AS ENUM ('USER', 'ADMIN', 'MANAGER');

INSERT INTO find_me_ticket_schema.role (id, type)
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'MANAGER');