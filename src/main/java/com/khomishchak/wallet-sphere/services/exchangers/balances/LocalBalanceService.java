package com.khomishchak.cryptoportfolio.services.exchangers.balances;

import com.khomishchak.cryptoportfolio.model.enums.ExchangerCode;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.repositories.BalanceRepository;
import com.khomishchak.cryptoportfolio.services.UserService;
import com.khomishchak.cryptoportfolio.services.exchangers.ExchangerConnectorServiceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(value = "ws.balance.service.type", havingValue = "local")
public class LocalBalanceService extends com.khomishchak.cryptoportfolio.services.exchangers.balances.CommonBalanceService {

    private final BalanceRepository balanceRepository;

    public LocalBalanceService(BalanceRepository balanceRepository, UserService userService,
                               List<ExchangerConnectorServiceFactory> exchangerServiceFactories) {
        super(balanceRepository, userService, exchangerServiceFactories);
        this.balanceRepository = balanceRepository;
    }

    @Override
    @Cacheable(value = "balanceCache", key = "#userId")
    public List<Balance> getMainBalances(long userId) {
        return balanceRepository.findAllByUser_Id(userId);
    }

    @Override
    @Cacheable(value = "balanceCache", key="{#userId, #code}")
    public Balance getMainBalance(long userId, ExchangerCode code) {
        return super.getBalanceByCodeAndUserIdOrThrow(userId, code);
    }

}
