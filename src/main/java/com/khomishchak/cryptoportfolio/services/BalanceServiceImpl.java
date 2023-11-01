package com.khomishchak.cryptoportfolio.services;

import com.khomishchak.cryptoportfolio.exceptions.BalanceNotFoundException;
import com.khomishchak.cryptoportfolio.model.User;
import com.khomishchak.cryptoportfolio.model.enums.ExchangerCode;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.model.exchanger.Currency;
import com.khomishchak.cryptoportfolio.repositories.BalanceRepository;
import com.khomishchak.cryptoportfolio.services.markets.MarketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final MarketService marketService;

    public BalanceServiceImpl(BalanceRepository balanceRepository, MarketService marketService) {
        this.balanceRepository = balanceRepository;
        this.marketService = marketService;
    }

    @Override
    public Balance registerBalanceEntryInfo(ExchangerCode code, String balanceName, User user) {
        Balance emptyBalance = Balance.builder()
                .code(code)
                .balanceName(balanceName)
                .user(user)
                .build();

        return balanceRepository.save(emptyBalance);
    }

    @Override
    public Balance getBalanceByCodeAndUserId(ExchangerCode code, Long userId, List<Currency> availableCurrencies) {

        Balance balance = balanceRepository.findByCodeAndUser_Id(code, userId)
                .orElseThrow(() -> new BalanceNotFoundException(String.format("User with id: %d, do not have balance for exchanger: %s", userId, code)));
        balance.setCurrencies(availableCurrencies);

        Balance persistedBalance = balanceRepository.save(balance);

        return persistedBalance.toBuilder()
                .totalValue(getTotalPrices(availableCurrencies))
                .build();
    }

    // TODO: move to BalancePricesService later on
    private Double getTotalPrices(List<Currency> currencies) {


        Map<String, Double> marketValues = marketService.getCurrentMarketValues();
        Map<String, Currency> currencyMap = currencies.stream()
                .collect(Collectors.toMap(Currency::getCurrencyCode, Function.identity()));


        double totalValue = 0;
        for (Map.Entry<String, Double> entry : marketValues.entrySet()) {
            Currency currency = currencyMap.get(entry.getKey());
            if (currency != null) {
                double currencyTotalValue = entry.getValue() * currency.getAmount();
                currency.setTotalValue(currencyTotalValue);
                totalValue += currencyTotalValue;
            }
        }

        return totalValue;
    }
}
