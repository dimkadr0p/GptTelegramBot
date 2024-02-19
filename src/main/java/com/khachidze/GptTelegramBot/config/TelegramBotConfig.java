package com.khachidze.GptTelegramBot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram-api")
@Getter
@Setter
public class TelegramBotConfig {
    private String botUsername;
    private String botToken;
}
