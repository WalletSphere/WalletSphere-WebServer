package com.khomishchak.cryptoportfolio.services.integration.whitebit;

import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.DepositWithdrawalTransaction;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.ExchangerDepositWithdrawalTransactions;

import java.util.List;

public interface WhiteBitService {

    Balance getAccountBalance(long userId);

    ExchangerDepositWithdrawalTransactions getDepositWithdrawalHistory(long userId);
}
