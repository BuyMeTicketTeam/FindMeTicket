INSERT INTO find_me_ticket_schema.users (id, registration_date, url_picture, notification, role_id)
VALUES ('282cd4b3-240c-4483-811c-3625e1795626', NOW(),
        'https://lh3.googleusercontent.com/pw/AP1GczOhz7hHCCfGHp3j3TL6hi6dAanTbwim6_4GcPLxhB7Db7Ezz67kagJUasJYSsgYd7yEcRIIT5BWWu1U0QYWhHqsT5WgwIleBhQ6DOoJD-v5OcvnhpKxZ0t-V1K-LsEbuFY_xbLpJEdRlQ-760_uZAc=w398-h398-s-no-gm?authuser=0',
        TRUE, 2),
       ('6bdafe1f-fb85-4d04-b95a-532570a2d8fb', NOW(),
        'https://lh3.googleusercontent.com/pw/AP1GczPbvb64vYc46m-u09vkphIVtW7M2bHx5Q_YlqdV4q9OMg1QsayQJucMShM91pB0oQ9sIqTk9OxAaMZqEgDEWfum8N5C-si1LYztJFhE78aYZlznl5S5O1OdK6D3DNL2FjKbbJ-1cApMoxg21VmPDHM=w945-h945-s-no-gm?authuser=0',
        TRUE, 2),
       ('c7a4efb5-1525-4f6a-9672-2b865f414de3', NOW(),
        'https://lh3.googleusercontent.com/pw/AP1GczN1kT8-ubvv3taOi2bnDP2cBvQ2Ai1IWxbulSqPa-P39hJhWQ7OeRDwm2iUVItk6UKsrlTt3wWt4Yta8signLfUPb6zt8JbjuouAVeeJ-WkP99Z2zbC5DiXklze5wShuwKEzEnTYYgPkBbNUnONKRA=w945-h945-s-no-gm?authuser=0',
        TRUE, 2),
       ('1523babf-4ede-4a3f-9920-9795c228091b', now(),
        'https://lh3.googleusercontent.com/pw/AP1GczMG03AiH9CHR4fkgAMOWLO8rj4xVN9kn0mN07LJ9oFQqeUd61LavwCeyUrJSk_gXmYkB0-6IHpnNZVhXAtxKsWNmoRRFhCQnq8BtcmfBG4IkYRhpfSOTDhJxBxiVTJX-6zHcDvGm_-wjZre8CNJmlM=w643-h643-s-no-gm?authuser=0',
        TRUE, 2);

INSERT INTO find_me_ticket_schema.user_credentials (id, email, password, username, user_id, provider,
                                                    account_non_expired, account_non_locked, credentials_non_expired,
                                                    enabled)
VALUES ('d40462f9-99c1-4798-86cd-58d606229bbb', 'max@gmail.com',
        '$2a$10$ceOGO7v/5HC0833TT1YKKe38nlrqIuBv5V8axGIWyjepqQFq1W3K2', 'Max',
        '282cd4b3-240c-4483-811c-3625e1795626', 'GOOGLE', TRUE, TRUE, TRUE, TRUE),
       ('a8746fb7-d3f4-4b02-b5c8-6b4a045b5af9', 'stepan@example.com',
        '$2a$10$ceOGO7v/5HC0833TT1YKKe38nlrqIuBv5V8axGIWyjepqQFq1W3K2', 'Stepan',
        '6bdafe1f-fb85-4d04-b95a-532570a2d8fb',
        'GOOGLE', TRUE, TRUE, TRUE, TRUE),
       ('f16ed366-3496-4874-b2e5-ec5fc31b96c4', 'mishaakamichael999@gmail.com@example.com',
        '$2a$10$ceOGO7v/5HC0833TT1YKKe38nlrqIuBv5V8axGIWyjepqQFq1W3K2', 'Mykhailo',
        'c7a4efb5-1525-4f6a-9672-2b865f414de3',
        'GOOGLE', TRUE, TRUE, TRUE, TRUE),
       ('518c5219-341d-48b0-8761-329e60a26087', 'kirilo@gmail.com',
        '$2a$10$ceOGO7v/5HC0833TT1YKKe38nlrqIuBv5V8axGIWyjepqQFq1W3K2', 'Kirilo',
        '1523babf-4ede-4a3f-9920-9795c228091b', 'GOOGLE', TRUE, TRUE,
        TRUE, TRUE);

INSERT INTO find_me_ticket_schema.review (id, review_text, writing_date, grade, user_id)
VALUES ('efe7ad40-edc9-4a4d-9c3d-74bbab8e9aa0',
        'Ефективний і зручний агрегатор квитків. Заощаджує час і гроші. Ідеальний для планування подорожей. Відтепер - мій основний інструмент для планування подорожей!',
        NOW(), 5, '282cd4b3-240c-4483-811c-3625e1795626'),
       ('890b2c2c-2147-4b85-bfa1-d8a609f428e4',
        'Цей агрегатор - справжнє відкриття! Швидкий пошук, зручний інтерфейс, широкий вибір квитків. Рекомендую усім шукачам найзручніших способів подорожувати!',
        NOW(), 5,
        '6bdafe1f-fb85-4d04-b95a-532570a2d8fb'),
       ('de78962b-8d20-4827-819f-6e3dd6d9a68c',
        'I recently had the opportunity to use FindMeTicket to look for tickets, and I must say, it exceeded my expectations in several ways. As a frequent traveler, I''m always on the lookout for convenient search propositions and efficient ways to plan my journeys, and FindMeTicket certainly delivered on both fronts.',
        NOW(), 5,
        'c7a4efb5-1525-4f6a-9672-2b865f414de3'),
       ('70239c68-4fc1-42c2-8848-ecd4e3f9a770',
        'Нещодавно я використав застосунок FindMeTicket, і можу сказати, що це дійсно зручний і корисний інструмент для тих, хто подорожує. Сайт пропонує широкий вибір квитків на автобуси та потяги, що дозволяє легко порівнювати ціни та вибирати найбільш вигідні пропозиції',
        NOW(), 5,
        '1523babf-4ede-4a3f-9920-9795c228091b');
