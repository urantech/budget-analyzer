package com.example.gateway.client;

import com.example.kafka.dto.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${transaction-service.base-url}")
    private String baseUrl;

    public List<TransactionDto> getUserTransactions(Long userId) {
        YearMonth month = YearMonth.from(LocalDate.now());
        LocalDate fromDate = month.atDay(1);
        LocalDate toDate = month.atEndOfMonth();

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("userId", userId)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .build()
                .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(response, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("Cannot process json from query {}", url);
            return List.of();
        }
    }
}
