package com.khomishchak.ws.model.exchanger.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khomishchak.ws.model.enums.ExchangerCode;
import com.khomishchak.ws.model.exchanger.Balance;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "deposit_withdrawal_transactions_history")
public class ExchangerDepositWithdrawalTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExchangerCode code;

    private long userId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @OneToMany(mappedBy = "exchangerDepositWithdrawalTransactions", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DepositWithdrawalTransaction> transactions;

    @JsonIgnore
    public Balance getBalance() {
        return balance;
    }

    public Long getBalanceId() {
        return balance.getId();
    }
}
