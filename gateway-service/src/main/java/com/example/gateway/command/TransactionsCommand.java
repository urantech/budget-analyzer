package com.example.gateway.command;

import com.example.gateway.client.TransactionServiceClient;
import com.example.gateway.model.User;
import com.example.kafka.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionsCommand implements Command {

    private final TransactionServiceClient transactionServiceClient;

    @Override
    public String execute(User user, String messageText) {
        List<TransactionDto> transactions = transactionServiceClient.getUserTransactions(user.getId());

        StringBuilder builder = new StringBuilder()
                .append(user.getFirstName()).append(", ваши транзакции:\n\n");
        for (var transaction : transactions) {
            builder.append("▸ Транзакция #").append(transaction.getDescription()).append("\n")
                    .append("  Сумма: ").append(String.format("%.2f", transaction.getAmount())).append(" ₽\n\n");
        }
        return builder.toString();
    }

    @Override
    public boolean canHandle(String messageText) {
        return messageText.startsWith("/transactions");
    }
}
