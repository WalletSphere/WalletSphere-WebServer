package com.khomishchak.cryptoportfolio.services.exchangers.balances;

import com.khomishchak.cryptoportfolio.model.exchanger.Balance;

public interface BalancePricingService {

    void calculateBalanceValueUpToDate(Balance balance);
}
