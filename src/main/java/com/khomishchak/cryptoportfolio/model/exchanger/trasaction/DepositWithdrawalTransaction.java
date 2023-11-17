package com.khomishchak.cryptoportfolio.model.exchanger.trasaction;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khomishchak.cryptoportfolio.model.Transaction;
import com.khomishchak.cryptoportfolio.model.TransactionType;
import com.khomishchak.cryptoportfolio.model.enums.ExchangerCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "deposit_withdrawal_transactions")
public class DepositWithdrawalTransaction extends Transaction {

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "deposit_withdrawal_transactions_history_id")
    @JsonIgnore
    private ExchangerDepositWithdrawalTransactions exchangerDepositWithdrawalTransactions;

    @Builder(builderMethodName = "depositWithdrawalTransactionBuilder")
    public DepositWithdrawalTransaction(String transactionId, String transactionHash, String ticker, BigDecimal fee,
                                        BigDecimal amount, LocalDateTime createdAt, TransactionType transactionType) {
        super(transactionId, transactionHash, ticker, fee, amount, createdAt);
        this.transactionType = transactionType;
    }
}
