-- liquibase formatted sql

-- changeset telegramBot:1

CREATE TABLE notification_task(
                             id SERIAL PRIMARY KEY,
                             message_chat_id INT NOT NULL,
                             notification_text TEXT NOT NULL,
                             date_time DATE NOT NULL
);

-- changeset telegramBot:2

ALTER TABLE notification_task
    ADD CONSTRAINT notification_unique_time_for_chat
        UNIQUE (message_chat_id, date_time);

-- changeset telegramBot:3

ALTER TABLE notification_task
ALTER COLUMN date_time TYPE TIMESTAMP;
ALTER TABLE notification_task
ALTER COLUMN message_chat_id TYPE BIGINT;
