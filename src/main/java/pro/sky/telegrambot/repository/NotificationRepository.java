package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository /*extends JpaRepository*/ {
//    @Query(value = "SELECT   notification_text,   LENGTH(name) AS length FROM faculty ORDER BY length DESC LIMIT 1;", nativeQuery = true)
//    String getLongestFacultyName();
}
