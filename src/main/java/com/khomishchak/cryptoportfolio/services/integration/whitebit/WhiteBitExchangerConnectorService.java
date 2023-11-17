package com.khomishchak.cryptoportfolio.services.integration.whitebit;

import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.ExchangerDepositWithdrawalTransactions;
import com.khomishchak.cryptoportfolio.services.exchangers.ExchangerConnectorService;

import org.springframework.stereotype.Service;

@Service
public class WhiteBitExchangerConnectorService implements ExchangerConnectorService {

    private final WhiteBitService whiteBitService;

    public WhiteBitExchangerConnectorService(WhiteBitService whiteBitService) {
        this.whiteBitService = whiteBitService;
    }

    @Override
    public Balance getMainBalance(long userId) {
        return whiteBitService.getAccountBalance(userId);
    }

    @Override
    public ExchangerDepositWithdrawalTransactions getDepositWithdrawalHistory(long userId) {
        return whiteBitService.getDepositWithdrawalHistory(userId);
    }
}
