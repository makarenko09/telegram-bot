package pro.sky.telegrambot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertNotification(Long chatId, String text, LocalDateTime time) {
        String sql = "INSERT INTO notification_task (message_chat_id, notification_text, date_time) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, chatId, text, Timestamp.valueOf(time));
    }
}
