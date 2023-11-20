package com.khomishchak.cryptoportfolio.services.exchangers.balance.history;

import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.ExchangerDepositWithdrawalTransactions;
import com.khomishchak.cryptoportfolio.repositories.history.DepositWithdrawalTransactionsHistoryRepository;
import com.khomishchak.cryptoportfolio.services.UserService;
import com.khomishchak.cryptoportfolio.services.exchangers.ExchangerConnectorServiceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(value = "ws.account.balance.operations.history.service.type", havingValue = "remote")
public class RemoteAccountBalanceTransferOperationsHistoryService extends CommonAccountBalanceTransferOperationsHistoryService {

    public RemoteAccountBalanceTransferOperationsHistoryService(UserService userService,
                                        List<ExchangerConnectorServiceFactory> exchangerServiceFactories,
                                        DepositWithdrawalTransactionsHistoryRepository depositWithdrawalTransactionsHistoryRepository) {
        super(userService, exchangerServiceFactories, depositWithdrawalTransactionsHistoryRepository);
    }

    @Override
    public List<ExchangerDepositWithdrawalTransactions> getDepositWithdrawalTransactionsHistory(long userId) {
        return super.synchronizeDepositWithdrawalTransactionsHistory(userId);
    }
}
