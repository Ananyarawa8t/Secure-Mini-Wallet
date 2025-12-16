package com.example.Mini.Wallet.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction{
    private Long id;
    private Long senderWalletId;
    private Long receiverWalletId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime timestamp;

}
