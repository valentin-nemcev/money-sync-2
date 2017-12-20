CREATE TABLE IF NOT EXISTS account(id BIGINT PRIMARY KEY, name VARCHAR(60) NOT NULL);
--;;
CREATE SEQUENCE IF NOT EXISTS account_id_seq AS BIGINT INCREMENT BY 1;
--;;
INSERT INTO account
VALUES
(NEXTVAL('account_id_seq'), 'test common account'),
(NEXTVAL('account_id_seq'), 'test personal account');
