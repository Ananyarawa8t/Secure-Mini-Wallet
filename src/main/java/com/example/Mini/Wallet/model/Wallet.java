package com.example.Mini.Wallet.model;

import java.math.BigDecimal;

public class Wallet {
    private Long id;
    private BigDecimal balance = BigDecimal.ZERO;
    private AppUser user;
}
