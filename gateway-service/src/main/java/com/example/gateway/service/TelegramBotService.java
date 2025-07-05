package com.example.gateway.service;

import com.example.gateway.command.Command;
import com.example.gateway.config.TelegramBotConfig;
import com.example.gateway.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramBotConfig config;
    private final List<Command> commands;

    public TelegramBotService(TelegramBotConfig config, List<Command> commands) {
        super(config.getToken());
        this.config = config;
        this.commands = commands;
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            // Get user by Telegram chat ID (mocked for now)
            User user = User.findByTelegramChatId(chatId);

            String responseText = processMessage(user, messageText);

            sendMessage(chatId, responseText);
        }
    }

    /**
     * Process the incoming message and generate a response.
     *
     * @param user The user who sent the message
     * @param messageText The text of the message
     * @return The response text
     */
    private String processMessage(User user, String messageText) {
        log.info("Processing message from user {}: {}", user.getUsername(), messageText);

        for (var command : commands) {
            if (command.canHandle(messageText)) {
                return command.execute(user, messageText);
            }
        }
        return "You said: " + messageText;
    }

    /**
     * Send a message to a specific chat.
     * 
     * @param chatId The chat ID to send the message to
     * @param text The text of the message
     */
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
            log.info("Message sent to chat {}: {}", chatId, text);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}: {}", chatId, e.getMessage(), e);
        }
    }
}
