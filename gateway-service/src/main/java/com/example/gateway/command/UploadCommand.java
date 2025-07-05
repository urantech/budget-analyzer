package com.example.gateway.command;

import com.example.gateway.model.Transaction;
import com.example.gateway.model.User;
import com.example.gateway.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadCommand implements Command {

    private final TransactionService transactionService;

    @Override
    public String execute(User user, String messageText) {
        log.info("Processing upload command from user: {}", user.getUsername());

        // For now, we'll create a mock transaction and send it to Kafka
        // In a real implementation, we would parse the uploaded file
        Transaction transaction = Transaction.createMockTransaction(user.getId());

        try {
            transactionService.sendTransaction(transaction);
            return "Transaction uploaded successfully and sent for processing.";
        } catch (Exception e) {
            log.error("Error processing transaction upload", e);
            return "Error processing your transaction. Please try again later.";
        }
    }

    @Override
    public boolean canHandle(String messageText) {
        return messageText.startsWith("/upload");
    }
}
