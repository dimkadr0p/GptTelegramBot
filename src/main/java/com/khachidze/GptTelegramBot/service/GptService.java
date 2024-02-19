package com.khachidze.GptTelegramBot.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.khachidze.GptTelegramBot.dto.AssistantResponseDTO;
import com.khachidze.GptTelegramBot.dto.QuestionDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GptService {

    private static final String API_URL = "http://172.19.0.3:8080/generate";

    public Optional<AssistantResponseDTO> processQuestion(QuestionDto questionDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(questionDto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

            return response.getStatusCode() == HttpStatus.OK
                    ? Optional.of(objectMapper.readValue(response.getBody(), AssistantResponseDTO.class))
                    : Optional.empty();

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
