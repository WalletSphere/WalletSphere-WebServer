package com.khomishchak.cryptoportfolio.repositories.history;

import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.ExchangerDepositWithdrawalTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositWithdrawalTransactionsHistoryRepository extends JpaRepository<ExchangerDepositWithdrawalTransactions, Long> {

    List<ExchangerDepositWithdrawalTransactions> findAllByUserId(long userId);

    void deleteAllByUserId(long userId);
}
