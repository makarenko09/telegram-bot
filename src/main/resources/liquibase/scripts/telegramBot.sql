-- liquibase formatted sql

-- changeset telegramBot:1

CREATE TABLE notification_task(
                             id SERIAL PRIMARY KEY,
                             message_chat_id INT NOT NULL,
                             notification_text TEXT NOT NULL,
                             date_time DATE NOT NULL
);