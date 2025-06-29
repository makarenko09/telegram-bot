package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.jdbc.datasource.init.DatabasePopulatorUtils.execute;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String text = update.message().text();
            if (text.contains("/start")) {
                Long id = update.message().chat().id();
                sendText(id, "Welcome to the bot!");
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendText(Long who, String what) {
        SendMessage request = new SendMessage(who, what);
        SendResponse response = telegramBot.execute(request);
        if (!response.isOk()) {
            logger.error("Failed to send message: {}", response.description());
        }
    }
}
