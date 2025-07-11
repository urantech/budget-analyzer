package com.example.transaction.controller;

import com.example.kafka.dto.TransactionDto;
import com.example.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/srv/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionDto> getUserTransactions(
            @RequestParam Long userId, @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        return transactionService.getUserTransactions(userId, fromDate, toDate);
    }
}
