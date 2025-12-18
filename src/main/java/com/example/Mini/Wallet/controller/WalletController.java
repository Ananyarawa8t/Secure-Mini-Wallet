package com.example.Mini.Wallet.controller;


import com.example.Mini.Wallet.model.Transaction;
import com.example.Mini.Wallet.model.Wallet;
import com.example.Mini.Wallet.service.TransactionService;
import com.example.Mini.Wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final TransactionService transactionService;

    public WalletController(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    // Helper to get currently logged-in username
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Wallet> deposit(@RequestBody Map<String, BigDecimal> request){
        String username = getCurrentUsername();
        Wallet wallet = walletService.findWalletByUsername(username); // Get wallet to find ID (not efficient but simple)

        BigDecimal amount = request.get("amount");
        return ResponseEntity.ok(walletService.deposit(wallet.getUser().getId(), amount));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String > transfer(@RequestBody Map<String, Object> request){
        String senderUsername = getCurrentUsername();
        String receiverUsername = (String) request.get("toUsername");
        // Convert Integer/Double to BigDecimal safely
        BigDecimal amount = new BigDecimal(request.get("amount").toString());

        walletService.transfer(senderUsername, receiverUsername, amount);
        return ResponseEntity.ok("Transfer Successful");
    }
    // 4. Transaction History
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getHistory() {
        String username = getCurrentUsername();
        Wallet wallet = walletService.findWalletByUsername(username);
        return ResponseEntity.ok(transactionService.getTransactions(wallet.getId()));
    }
}
