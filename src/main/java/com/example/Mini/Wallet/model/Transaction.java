package com.example.Mini.Wallet.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderWalletId;
    private Long receiverWalletId;
    @Column(nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @CreationTimestamp
    private LocalDateTime timestamp;


    public void setSenderWalletId(Long senderWalletId) {
        this.senderWalletId = senderWalletId;
    }
    public Long getSenderWalletId() {
        return senderWalletId;
    }

    public void setReceiverWalletId(Long receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }
    public Long getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
    public TransactionType getType() {
        return type;
    }

    // timestamp is auto-generated, but here is a getter just in case
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
