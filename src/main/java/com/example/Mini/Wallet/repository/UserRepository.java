package com.example.Mini.Wallet.repository;

import com.example.Mini.Wallet.model.AppUser;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    //custom query method
    Optional<AppUser> findByUsername(String username);
}
