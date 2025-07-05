package com.example.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user in the system.
 * This is a mock implementation for now.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Long telegramChatId;

    public static User findByTelegramChatId(Long chatId) {
        return User.builder()
                .id(1L)
                .username("mock_user")
                .firstName("Mock")
                .lastName("User")
                .telegramChatId(chatId)
                .build();
    }
}
