package com.khomishchak.cryptoportfolio.services.exchangers.balance;

import com.khomishchak.cryptoportfolio.model.enums.ExchangerCode;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.repositories.BalanceRepository;
import com.khomishchak.cryptoportfolio.services.UserService;
import com.khomishchak.cryptoportfolio.services.exchangers.ExchangerConnectorService;
import com.khomishchak.cryptoportfolio.services.exchangers.ExchangerConnectorServiceFactory;
import com.khomishchak.cryptoportfolio.services.exchangers.balance.cache.BalanceCacheHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(value = "ws.balance.service.type", havingValue = "remote")
public class RemoteBalanceService extends CommonBalanceService {

    public RemoteBalanceService(BalanceRepository balanceRepository, BalancePricingService balancePricingService,
                                List<ExchangerConnectorServiceFactory> exchangerServiceFactories, UserService userService,
                                BalanceCacheHandler balanceCacheHandler) {
        super(balanceRepository, userService, exchangerServiceFactories, balancePricingService, balanceCacheHandler);
    }

    @Override
    public List<Balance> getMainBalances(long userId) {
        return super.synchronizeBalances(userId);
    }

    @Override
    public Balance getMainBalance(long userId, ExchangerCode exchangerCode) {
        ExchangerConnectorService exchangerConnectorService = super.getExchangerConnectorService(exchangerCode);
        return exchangerConnectorService.getMainBalance(userId);
    }
}
