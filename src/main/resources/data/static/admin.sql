INSERT INTO find_me_ticket_schema.users (id, registration_date, profile_picture, notification, role_id)
VALUES ('e37a7578-ebd9-40fe-b070-953c9a4154ca', NOW(),
        decode('iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAACVklEQVR4Xu3bMW5VMQBE0ayPxbAROlaRHWQP9FQI0UUidYRCIvjIlO7GwtYTc640zW+s539a390O9PTwdvv07tmCjTs70d38w44AyAdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUL5jAMZBu/d4/3r79vHFgo07m+9xx+5meTs2PkhZ487me9wxAC4aAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlHQPw/OXXbfd+fv89f9+Wvn54uX1+/2PrxhknGnc23+OOHXkbeKrxB83C//XGGf9TAIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAuP8OwPxQYMc8DMk79jBkFr5jnoblHXsaNv+wYwDkAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQ3jEATw9vt917vH/9+0G7d+IF0jhjPnfHxp3N97hjR94GjoNmeTs2njrtbpwxn7tj485OBEAYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQ9wdoso1lvqfL1AAAAABJRU5ErkJggg==', 'base64')
        , TRUE, 2);
INSERT INTO find_me_ticket_schema.user_credentials (id, email, password, username, user_id, provider, account_non_expired, account_non_locked,
                              credentials_non_expired, enabled)
VALUES ('3d5d9363-3524-4695-8017-b71caf0747b3', 'Stepan@gmail.com', 'admin', 'Stepan',
        'e37a7578-ebd9-40fe-b070-953c9a4154ca', 'LOCAL', TRUE, TRUE, TRUE, TRUE);
INSERT INTO find_me_ticket_schema.review (id, review_text, writing_date, grade, user_id)
VALUES ('7f2fbe1a-8a56-43a4-9eef-4124f1192122', 'Нещодавно я використав застосунок FindMeTicket, і можу сказати, що це дійсно зручний і корисний інструмент для тих, хто подорожує. Сайт пропонує широкий вибір квитків на автобуси та потяги, що дозволяє легко порівнювати ціни та вибирати найбільш вигідні пропозиції', NOW(), 5, 'e37a7578-ebd9-40fe-b070-953c9a4154ca');


INSERT INTO find_me_ticket_schema.users (id, registration_date, profile_picture, notification, role_id)
VALUES ('54020e9d-3cdb-4114-b965-0a408ca877d0', NOW(),
        decode('iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAACVklEQVR4Xu3bMW5VMQBE0ayPxbAROlaRHWQP9FQI0UUidYRCIvjIlO7GwtYTc640zW+s539a390O9PTwdvv07tmCjTs70d38w44AyAdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUL5jAMZBu/d4/3r79vHFgo07m+9xx+5meTs2PkhZ487me9wxAC4aAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlHQPw/OXXbfd+fv89f9+Wvn54uX1+/2PrxhknGnc23+OOHXkbeKrxB83C//XGGf9TAIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAuP8OwPxQYMc8DMk79jBkFr5jnoblHXsaNv+wYwDkAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQ3jEATw9vt917vH/9+0G7d+IF0jhjPnfHxp3N97hjR94GjoNmeTs2njrtbpwxn7tj485OBEAYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQ9wdoso1lvqfL1AAAAABJRU5ErkJggg==', 'base64'), FALSE, 2);
INSERT INTO find_me_ticket_schema.user_credentials (id, email, password, username, user_id, provider, account_non_expired, account_non_locked,
                                                    credentials_non_expired, enabled)
VALUES ('0e9a858a-d6ca-4870-98b3-36bb4f7e3c08', 'Max@gmail.com', 'admin', 'Maxim',
        '54020e9d-3cdb-4114-b965-0a408ca877d0', 'LOCAL', TRUE, TRUE, TRUE, TRUE);
INSERT INTO find_me_ticket_schema.review (id, review_text, writing_date, grade, user_id)
VALUES ('cc280289-124b-4e0d-b377-1acff56a031e', 'Цей агрегатор - справжнє відкриття! Швидкий пошук, зручний інтерфейс, широкий вибір квитків. Рекомендую усім шукачам найзручніших способів подорожувати!', NOW(), 5, '54020e9d-3cdb-4114-b965-0a408ca877d0');


INSERT INTO find_me_ticket_schema.users (id, registration_date, profile_picture, notification, role_id)
VALUES ('282cd4b3-240c-4483-811c-3625e1795626', NOW(),
        decode('iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAACVklEQVR4Xu3bMW5VMQBE0ayPxbAROlaRHWQP9FQI0UUidYRCIvjIlO7GwtYTc640zW+s539a390O9PTwdvv07tmCjTs70d38w44AyAdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUL5jAMZBu/d4/3r79vHFgo07m+9xx+5meTs2PkhZ487me9wxAC4aAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlHQPw/OXXbfd+fv89f9+Wvn54uX1+/2PrxhknGnc23+OOHXkbeKrxB83C//XGGf9TAIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAuP8OwPxQYMc8DMk79jBkFr5jnoblHXsaNv+wYwDkAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQ3jEATw9vt917vH/9+0G7d+IF0jhjPnfHxp3N97hjR94GjoNmeTs2njrtbpwxn7tj485OBEAYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQ9wdoso1lvqfL1AAAAABJRU5ErkJggg==', 'base64'), FALSE, 2);
INSERT INTO find_me_ticket_schema.user_credentials (id, email, password, username, user_id, provider, account_non_expired, account_non_locked,
                                                    credentials_non_expired, enabled)
VALUES ('d40462f9-99c1-4798-86cd-58d606229bbb', 'Kirill@gmail.com', 'admin', 'Kirill',
        '282cd4b3-240c-4483-811c-3625e1795626', 'LOCAL', TRUE, TRUE, TRUE, TRUE);
INSERT INTO find_me_ticket_schema.review (id, review_text, writing_date, grade, user_id)
VALUES ('efe7ad40-edc9-4a4d-9c3d-74bbab8e9aa0', 'Ефективний і зручний агрегатор квитків. Заощаджує час і гроші. Ідеальний для планування подорожей. Відтепер - мій основний інструмент для планування подорожей!', NOW(), 5, '282cd4b3-240c-4483-811c-3625e1795626');


INSERT INTO find_me_ticket_schema.users (id, registration_date, profile_picture, notification, role_id)
VALUES ('1a194ba1-42e9-429f-b253-3e038d35341d', NOW(),
        decode('iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAACVklEQVR4Xu3bMW5VMQBE0ayPxbAROlaRHWQP9FQI0UUidYRCIvjIlO7GwtYTc640zW+s539a390O9PTwdvv07tmCjTs70d38w44AyAdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUD4AygdA+QAoHwDlA6B8AJQPgPIBUL5jAMZBu/d4/3r79vHFgo07m+9xx+5meTs2PkhZ487me9wxAC4aAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlHQPw/OXXbfd+fv89f9+Wvn54uX1+/2PrxhknGnc23+OOHXkbeKrxB83C//XGGf9TAIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAOAAuHAB5AIQD4MIBkAdAuP8OwPxQYMc8DMk79jBkFr5jnoblHXsaNv+wYwDkAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQHgDlAVAeAOUBUB4A5QFQ3jEATw9vt917vH/9+0G7d+IF0jhjPnfHxp3N97hjR94GjoNmeTs2njrtbpwxn7tj485OBEAYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQB0AYAAsBkA+AhQGQ9wdoso1lvqfL1AAAAABJRU5ErkJggg==', 'base64'), FALSE, 2);
INSERT INTO find_me_ticket_schema.user_credentials (id, email, password, username, user_id, provider, account_non_expired, account_non_locked,
                                                    credentials_non_expired, enabled)
VALUES ('4d647f91-b7f6-4f42-aa5d-abede9d8897c', 'Misha@gmail.com', 'admin', 'Misha',
        '1a194ba1-42e9-429f-b253-3e038d35341d', 'LOCAL', TRUE, TRUE, TRUE, TRUE);
INSERT INTO find_me_ticket_schema.review (id, review_text, writing_date, grade, user_id)
VALUES ('3fee5a03-dee8-4f59-b30b-4239ea84e180', 'I recently had the opportunity to use Findmeticket to look for tickets, and I must say, it exceeded my expectations in several ways. As a frequent traveler, I''m always on the lookout for convenient search propositions and efficient ways to plan my journeys, and Findmeticket certainly delivered on both fronts.', NOW(), 5, '1a194ba1-42e9-429f-b253-3e038d35341d');

