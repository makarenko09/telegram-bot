package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Object, Long> {
    @Query(value = "INSERT INTO notification_task (message_chat_id, notification_text, date_time) VALUES (:chatId, :text, :time)", nativeQuery = true)
    void insertNotification(@Param("chatId") Long chatId,
                            @Param("text") String text,
                            @Param("time") LocalDateTime time);
}
//    @Query(value = "SELECT nt.* FROM public.notification_task AS nt,   LENGTH(name) AS length FROM faculty ORDER BY length DESC LIMIT 1;", nativeQuery = true)

