package com.example.transaction.service;

import com.example.kafka.dto.TransactionDto;
import com.example.transaction.model.Transaction;
import com.example.transaction.model.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    void testSave_ShouldConvertDtoToEntityAndSave() {
        Long id = 1L;
        Long userId = 123L;
        String description = "Test transaction";
        BigDecimal amount = new BigDecimal("100.50");
        String category = "Food";
        LocalDateTime timestamp = LocalDateTime.now();

        TransactionDto transactionDto = TransactionDto.builder()
                .id(id)
                .userId(userId)
                .description(description)
                .amount(amount)
                .category(category)
                .timestamp(timestamp)
                .build();

        transactionService.save(transactionDto);

        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertEquals(userId, savedTransaction.getUserId());
        assertEquals(description, savedTransaction.getDescription());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(category, savedTransaction.getCategory());
        assertEquals(timestamp, savedTransaction.getTimestamp());
    }

    @Test
    void testGetUserTransaction_shouldReturnListOfTransactions() {
        Long userId = 1L;
        LocalDate fromDate = LocalDate.of(2025, 10, 1);
        LocalDate toDate = LocalDate.of(2025, 10, 30);
        when(transactionRepository.findByUserIdAndTimestampBetween(anyLong(), ArgumentMatchers.any(LocalDateTime.class), ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(List.of(buildTransaction()));

        List<TransactionDto> transactions = transactionService.getUserTransactions(userId, fromDate, toDate);

        assertThat(transactions, hasSize(1));
        assertThat(transactions, contains(
                allOf(
                        hasProperty("amount", is(BigDecimal.valueOf(1000)))
                )
        ));
    }

    private Transaction buildTransaction() {
        return Transaction.builder()
                .userId(1L)
                .description("desc")
                .amount(BigDecimal.valueOf(1000))
                .category("cat")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
