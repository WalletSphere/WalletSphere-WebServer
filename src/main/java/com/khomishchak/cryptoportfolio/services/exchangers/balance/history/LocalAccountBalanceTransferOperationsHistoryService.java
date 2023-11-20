package com.khomishchak.cryptoportfolio.services.exchangers.balance.history;

import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.ExchangerDepositWithdrawalTransactions;
import com.khomishchak.cryptoportfolio.repositories.history.DepositWithdrawalTransactionsHistoryRepository;
import com.khomishchak.cryptoportfolio.services.UserService;
import com.khomishchak.cryptoportfolio.services.exchangers.ExchangerConnectorServiceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(value = "ws.account.balance.operations.history.service.type", havingValue = "local")
public class LocalAccountBalanceTransferOperationsHistoryService extends CommonAccountBalanceTransferOperationsHistoryService {

    private final DepositWithdrawalTransactionsHistoryRepository depositWithdrawalTransactionsHistoryRepository;

    public LocalAccountBalanceTransferOperationsHistoryService(UserService userService,
                                                               List<ExchangerConnectorServiceFactory> exchangerServiceFactories,
                                                               DepositWithdrawalTransactionsHistoryRepository depositWithdrawalTransactionsHistoryRepository) {
        super(userService, exchangerServiceFactories, depositWithdrawalTransactionsHistoryRepository);
        this.depositWithdrawalTransactionsHistoryRepository = depositWithdrawalTransactionsHistoryRepository;
    }

    @Override
    @Cacheable(value = "depositWithdrawalTransactionHistoryCache", key = "#userId")
    public List<ExchangerDepositWithdrawalTransactions> getDepositWithdrawalTransactionsHistory(long userId) {
        return depositWithdrawalTransactionsHistoryRepository.findAllByUserId(userId);
    }

}
