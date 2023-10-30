package com.khomishchak.cryptoportfolio.services.exchangers;

import com.khomishchak.cryptoportfolio.model.User;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.repositories.BalanceRepository;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public Balance registerBalanceEntryInfo(String balanceName, User user) {
        Balance emptyBalance = Balance.builder()
                .balanceName(balanceName)
                .user(user)
                .build();

        return balanceRepository.save(emptyBalance);
    }
}
