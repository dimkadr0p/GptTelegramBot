package com.khachidze.GptTelegramBot.service;

import com.khachidze.GptTelegramBot.config.TelegramBotConfig;
import com.khachidze.GptTelegramBot.dto.AssistantResponseDTO;
import com.khachidze.GptTelegramBot.dto.QuestionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramBotConfig telegramBotConfig;
    private final GptService gptService;
    private final SendMessage message = new SendMessage();

    @Autowired
    public TelegramBotService(TelegramBotConfig telegramBotConfig, GptService gptService) {
        this.telegramBotConfig = telegramBotConfig;
        this.gptService = gptService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getUserName());
                    log.info("Запустил бота пользователь: {}, chatId: {}", update.getMessage().getChat().getUserName(), update.getMessage().getChatId());
                    break;
                default:
                    log.info("пользователь {} отправил такое сообщение {}",
                            update.getMessage().getChat().getUserName(), messageText);
                    Optional<AssistantResponseDTO> response = gptService.processQuestion(new QuestionDto(chatId, messageText));
                    String responseText = response.map(res -> res.getResult().getAlternatives()
                            .get(0).getMessage().getText()).orElse("Сервис пока не может выдать вам ответ");
                    sendMessage(chatId, responseText);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getBotToken();
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привествуем вас, " + name + ".\nБот основе алгоритмов GPT поможет вам во многом!\n" +
                "Задавайте вопросы и не стесняйтесь!\nПриятного использования!!!";

        sendMessage(chatId, answer);
    }

    public void sendMessage(long chatId, String textToSend) {
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException ex) {
            log.error("error occurred: {}", ex.getMessage());
        }
    }


}
