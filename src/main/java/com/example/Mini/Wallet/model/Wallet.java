package com.example.Mini.Wallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    public void setUser(AppUser user) { this.user = user; }
    public void setBalance(java.math.BigDecimal balance) { this.balance = balance; }
    public AppUser getUser() { return user; }
    public java.math.BigDecimal getBalance() { return balance; }
    public Long getId() { return id; }
}
