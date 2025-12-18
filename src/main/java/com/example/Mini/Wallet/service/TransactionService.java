package com.example.Mini.Wallet.service;

import com.example.Mini.Wallet.model.Transaction;
import com.example.Mini.Wallet.model.TransactionType;
import com.example.Mini.Wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionService {
    private  final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void recordTransaction(Long senderId, Long receiverId, BigDecimal amount, TransactionType type){
        Transaction transaction = new Transaction();
        transaction.setSenderWalletId(senderId);
        transaction.setReceiverWalletId(receiverId);
        transaction.setAmount(amount);
        transaction.setType(type);

        transactionRepository.save(transaction);
    }
    public List<Transaction> getTransactions(Long walletId) {
        // Use the custom method we defined in the Repository in Module 2
        return transactionRepository.findBySenderWalletIdOrReceiverWalletId(walletId, walletId);
    }
}
