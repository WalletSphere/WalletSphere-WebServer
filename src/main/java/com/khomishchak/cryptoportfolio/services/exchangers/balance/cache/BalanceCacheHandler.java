package com.khomishchak.cryptoportfolio.services.exchangers.balance.cache;

import com.khomishchak.cryptoportfolio.services.exchangers.balance.history.AccountBalanceTransferOperationsHistoryService;
import org.springframework.stereotype.Service;

@Service
public class BalanceCacheHandler {

    AccountBalanceTransferOperationsHistoryService accountBalanceTransferOperationsHistoryService;

    public BalanceCacheHandler(AccountBalanceTransferOperationsHistoryService accountBalanceTransferOperationsHistoryService) {
        this.accountBalanceTransferOperationsHistoryService = accountBalanceTransferOperationsHistoryService;
    }

    public void deleteAllBalanceRelatedCacheInfo(long balanceId) {
        accountBalanceTransferOperationsHistoryService.deleteDepositWithdrawalTransactionsHistory(balanceId);
    }
}
