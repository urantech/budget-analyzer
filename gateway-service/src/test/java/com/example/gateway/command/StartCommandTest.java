package com.example.gateway.command;

import com.example.gateway.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartCommandTest {

    private StartCommand startCommand;
    private User testUser;

    @BeforeEach
    void setUp() {
        startCommand = new StartCommand();
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .telegramChatId(123L)
                .build();
    }

    @Test
    void testExecute() {
        String result = startCommand.execute(testUser, "/start");

        assertTrue(result.contains("Welcome to Budget Analyzer Bot, Test!"));
        assertTrue(result.contains("/upload"));
    }

    @Test
    void testCanHandle() {
        assertTrue(startCommand.canHandle("/start"));
        assertTrue(startCommand.canHandle("/start with additional text"));

        assertFalse(startCommand.canHandle("start"));
        assertFalse(startCommand.canHandle("/upload"));
        assertFalse(startCommand.canHandle("hello"));
    }
}
