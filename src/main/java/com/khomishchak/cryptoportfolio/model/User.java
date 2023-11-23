package com.khomishchak.cryptoportfolio.model;

import com.khomishchak.cryptoportfolio.model.enums.UserRole;
import com.khomishchak.cryptoportfolio.model.exchanger.ApiKeySetting;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.model.goals.CryptoGoalsTable;
import com.khomishchak.cryptoportfolio.model.goals.SelfGoal;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
@ToString(exclude =  {"cryptoGoalsTable", "apiKeysSettings", "balances", "selfGoals", "feedbacks"})
@EqualsAndHashCode(exclude = {"cryptoGoalsTable", "apiKeysSettings", "balances", "selfGoals", "feedbacks"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    private LocalDateTime createdTime;
    private LocalDateTime lastLoginTime;

    @Column(name = "accept_tc")
    private boolean acceptTC;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(fetch = FetchType.LAZY)
    private CryptoGoalsTable cryptoGoalsTable;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApiKeySetting> apiKeysSettings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Balance> balances;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SelfGoal> selfGoals = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();
}