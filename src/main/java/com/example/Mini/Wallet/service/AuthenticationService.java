package com.example.Mini.Wallet.service;

import com.example.Mini.Wallet.dto.AuthenticationRequest;
import com.example.Mini.Wallet.dto.AuthenticationResponse;
import com.example.Mini.Wallet.dto.RegisterRequest;
import com.example.Mini.Wallet.model.AppUser;
import com.example.Mini.Wallet.model.Wallet;
import com.example.Mini.Wallet.repository.UserRepository;
import com.example.Mini.Wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private  final PasswordEncoder passwordEncoder;
    private final  JwtService jwtService;
    private  final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 WalletRepository walletRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request){
        var user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        AppUser savedUser = userRepository.save(user);

        var wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);

        //var jwtToken = jwtService.generateToken(savedUser);


        org.springframework.security.core.userdetails.User springUser= new org.springframework.security.core.userdetails.User(
                savedUser.getUsername(),
                savedUser.getPassword(),
                java.util.Collections.emptyList()
        );

        var finalToken = jwtService.generateToken(springUser);
        return new AuthenticationResponse(finalToken);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                java.util.Collections.emptyList()
        );
        var jwtToken = jwtService.generateToken(springUser);
        return new AuthenticationResponse(jwtToken);
    }
}
