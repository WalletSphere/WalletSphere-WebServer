package com.khomishchak.cryptoportfolio.services.exchangers;

import com.khomishchak.cryptoportfolio.model.User;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;

public interface BalanceService {

    Balance registerBalanceEntryInfo(String balanceName, User user);
}
