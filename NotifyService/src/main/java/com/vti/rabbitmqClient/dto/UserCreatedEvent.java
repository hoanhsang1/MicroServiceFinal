package com.vti.rabbitmqClient.dto;

public record UserCreatedEvent(Long id, String username, String fullName, String email) {

    public static UserCreatedEvent parse(String raw) {
        String[] parts = raw.split(",", -1);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Malformed user.created message: " + raw);
        }
        return new UserCreatedEvent(
                Long.parseLong(parts[0].trim()),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim()
        );
    }
}