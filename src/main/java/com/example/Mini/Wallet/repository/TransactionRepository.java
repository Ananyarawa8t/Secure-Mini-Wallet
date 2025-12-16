package com.example.Mini.Wallet.repository;

import com.example.Mini.Wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderWalletIdOrReceiverWalletId(Long senderId, Long receiverId);
}
