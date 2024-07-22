CREATE TYPE provider_type AS ENUM ('BASIC', 'GOOGLE', 'FACEBOOK');

INSERT INTO find_me_ticket_schema.provider (id, type)
VALUES (1, 'BASIC'),
       (2, 'GOOGLE'),
       (3, 'FACEBOOK');