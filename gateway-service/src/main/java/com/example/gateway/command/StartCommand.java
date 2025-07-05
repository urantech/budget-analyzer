package com.example.gateway.command;

import com.example.gateway.model.User;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    @Override
    public String execute(User user, String messageText) {
        return """
                Welcome to Budget Analyzer Bot, %s! How can I help you today?
                Please, enter one of the commands:
                /upload - upload bank account pdf
                """.formatted(user.getFirstName());
    }

    @Override
    public boolean canHandle(String messageText) {
        return messageText.startsWith("/start");
    }
}
