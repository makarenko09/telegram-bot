package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.NotificationRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationRepository notificationRepository;

    private final Pattern messagePattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            if (update.message() == null || update.message().text() == null) return;

            String text = update.message().text();
            Long id = update.message().chat().id();

            if ("/start".equals(text)) {
                sendText(id, "Добро пожаловать в бота! Введите напоминание в формате '01.01.2022 20:00 Сделать домашнюю работу'");
                return;
            }

            Matcher matcher = messagePattern.matcher(text);
            if (!matcher.find()) {
                sendText(id, "Неверный формат сообщения. Введите в виде: '01.01.2022 20:00 Сделать домашнюю работу', отдельным сообщением");
                return;
            }

            if (matcher.matches()) {
                String datetimeStr = matcher.group(1);
                String reminderText = matcher.group(3);
                LocalDateTime dateTime = LocalDateTime.parse(datetimeStr, dateTimeFormatter);
                validateReminders(id, reminderText, dateTime);
            }

        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void validateReminders(Long id, String reminderText, LocalDateTime dateTime) {
        boolean inserted = notificationRepository.insertNotification(id, reminderText, dateTime);
        if (inserted) {
            sendText(id, "✅ Напоминание сохранено!");
        } else {
            sendText(id, "ℹ️ Напоминание на это время уже существует.");
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Object[]> dueReminders = notificationRepository.findAllDueNowWithTime(now);
        if (!dueReminders.isEmpty()) {
            logger.info("Отправка {} напоминаний на {}", dueReminders.size(), now);
        }
        dueReminders.forEach(obj -> {
            Long chatId = (Long) obj[0];
            String text = (String) obj[1];
            LocalDateTime time = (LocalDateTime) obj[2];
            sendText(chatId, "🔔 Напоминание: " + text);
        });
    }

    public void sendText(Long who, String what) {
        SendMessage request = new SendMessage(who, what);
        SendResponse response = telegramBot.execute(request);
        logResponseExc(response);
    }

    private void logResponseExc(SendResponse response) {
        if (!response.isOk()) {
            logger.error("Failed to send message: {}", response.description());
        }
    }

}
