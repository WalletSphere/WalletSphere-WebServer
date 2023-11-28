package com.khomishchak.ws.model.exchanger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.khomishchak.ws.model.User;
import com.khomishchak.ws.model.enums.ExchangerCode;
import com.khomishchak.ws.model.exchanger.transaction.ExchangerDepositWithdrawalTransactions;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String balanceName;

    private LocalDateTime lastTimeWasUpdated;

    @Enumerated(value = EnumType.STRING)
    private ExchangerCode code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private ExchangerDepositWithdrawalTransactions depositWithdrawalTransactions;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private ApiKeySetting apiKeySetting;

    @ElementCollection
    private List<Currency> currencies;

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return user.getId();
    }
}
