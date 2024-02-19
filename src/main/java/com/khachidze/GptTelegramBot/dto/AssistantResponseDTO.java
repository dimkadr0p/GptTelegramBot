package com.khachidze.GptTelegramBot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AssistantResponseDTO {
    private Result result;
    @Data
    @NoArgsConstructor
    public static class Result {
        private List<Alternative> alternatives;
        private Usage usage;
        private String modelVersion;
    }

    @Data
    @NoArgsConstructor
    public static class Alternative {
        private Message message;
        private String status;
    }

    @Data
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String text;
    }

    @Data
    @NoArgsConstructor
    public static class Usage {
        private String inputTextTokens;
        private String completionTokens;
        private String totalTokens;
    }
}