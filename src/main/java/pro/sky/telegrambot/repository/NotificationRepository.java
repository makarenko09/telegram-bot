package pro.sky.telegrambot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insertNotification(Long chatId, String text, LocalDateTime time) {
        String sql = "INSERT INTO notification_task (message_chat_id, notification_text, date_time) VALUES (?, ?, ?)  ON CONFLICT DO NOTHING";
        int updates = jdbcTemplate.update(sql, chatId, text, Timestamp.valueOf(time));
        return updates > 0;
    }

    public List<Object[]> findAllDueNowWithTime(LocalDateTime time) {
        String sql = "SELECT message_chat_id, notification_text, date_time FROM notification_task WHERE date_time = ?";
        return jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(time)},
                (rs, rowNum) -> new Object[]{
                        rs.getLong("message_chat_id"),
                        rs.getString("notification_text"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                });
    }
}
