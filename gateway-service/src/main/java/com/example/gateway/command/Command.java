package com.example.gateway.command;

import com.example.gateway.model.User;

public interface Command {
    String execute(User user, String messageText);
    boolean canHandle(String messageText);
}
