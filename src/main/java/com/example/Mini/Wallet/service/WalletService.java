package com.example.Mini.Wallet.service;

import com.example.Mini.Wallet.model.AppUser;
import com.example.Mini.Wallet.model.TransactionType;
import com.example.Mini.Wallet.model.Wallet;
import com.example.Mini.Wallet.repository.UserRepository;
import com.example.Mini.Wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    public Wallet findWalletByUsername(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return walletRepository.findByUserId(user.getId());
    }

    @Transactional
    public Wallet deposit(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId);

        // Update balance: balance = balance + amount
        wallet.setBalance(wallet.getBalance().add(amount));

        Wallet updatedWallet = walletRepository.save(wallet);

        // Record the deposit
        transactionService.recordTransaction(
                null, // No sender for a deposit
                wallet.getId(),
                amount,
                TransactionType.DEPOSIT
        );

        return updatedWallet;
    }

    @Transactional(rollbackFor =Exception.class)
    public void transfer(String senderUsername, String receiverUsername, BigDecimal amount) {
        // A. Validate Input
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }
        if (senderUsername.equals(receiverUsername)) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        // B. Fetch Wallets
        Wallet senderWallet = findWalletByUsername(senderUsername);
        Wallet receiverWallet = findWalletByUsername(receiverUsername); // This might fail if user doesn't exist

        // C. Check Balance
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Funds");
        }

        // D. Perform the Transfer
        // 1. Deduct from Sender
        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        walletRepository.save(senderWallet);

        // 2. Add to Receiver
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));
        walletRepository.save(receiverWallet);

        // 3. Record History
        transactionService.recordTransaction(
                senderWallet.getId(),
                receiverWallet.getId(),
                amount,
                TransactionType.TRANSFER
        );

        // If the code reaches here, the transaction commits.
        // If an error happens at step D2 or D3, step D1 is undone automatically.
    }
}
