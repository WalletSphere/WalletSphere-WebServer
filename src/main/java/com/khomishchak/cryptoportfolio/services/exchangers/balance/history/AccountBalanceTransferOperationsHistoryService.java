package com.khomishchak.cryptoportfolio.services.exchangers.balance.history;

import com.khomishchak.cryptoportfolio.model.enums.ExchangerCode;
import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.ExchangerDepositWithdrawalTransactions;

import java.util.List;

public interface AccountBalanceTransferOperationsHistoryService {

    List<ExchangerDepositWithdrawalTransactions> getDepositWithdrawalTransactionsHistory(long userId);

    List<ExchangerDepositWithdrawalTransactions> synchronizeDepositWithdrawalTransactionsHistory(long userId);

    void deleteDepositWithdrawalTransactionsHistory(long balanceId);
}
