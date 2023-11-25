package com.khomishchak.ws.controllers;

import com.khomishchak.ws.model.enums.ExchangerCode;
import com.khomishchak.ws.model.exchanger.Balance;
import com.khomishchak.ws.model.exchanger.transaction.ExchangerDepositWithdrawalTransactions;
import com.khomishchak.ws.model.requests.RegisterExchangerInfoReq;
import com.khomishchak.ws.model.response.FirstlyGeneratedBalanceResp;
import com.khomishchak.ws.model.response.SyncDataResp;
import com.khomishchak.ws.services.exchangers.ExchangerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exchangers")
public class ExchangerController {

    private final ExchangerService exchangerService;

    public ExchangerController(ExchangerService exchangerService) {
        this.exchangerService = exchangerService;
    }

    @PostMapping("/api-keys")
    public FirstlyGeneratedBalanceResp addExchangerApiKeysForUser(@RequestAttribute Long userId,
                                                                  @RequestBody RegisterExchangerInfoReq exchangerInfoReq) {
        return exchangerService.addGeneralExchangerInfo(exchangerInfoReq , userId);
    }

    @GetMapping("/{exchangerCode}/balance")
    public Balance getAccountBalance(@RequestAttribute Long userId, @PathVariable String exchangerCode) {
        return exchangerService.getMainBalance(userId, ExchangerCode.valueOf(exchangerCode));
    }

    @GetMapping("/balance/all")
    public List<Balance> getAccountBalances(@RequestAttribute long userId) {
        return exchangerService.getAllMainBalances(userId);
    }

    @DeleteMapping("/{balanceId}/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountBalance(@PathVariable long balanceId) {
        exchangerService.deleteExchangerForUser(balanceId);
    }

    @GetMapping("/deposit-withdrawal-history")
    public List<ExchangerDepositWithdrawalTransactions> getWithdrawalDepositTransactionsHistory(@RequestAttribute long userId) {
        return exchangerService.getWithdrawalDepositWalletHistory(userId);
    }

    @PostMapping("/synchronize/balance")
    public SyncDataResp synchronizeBalanceDataForUser(@RequestAttribute Long userId) {
        return exchangerService.synchronizeBalanceDataForUser(userId);
    }

    @PostMapping("/synchronize/deposit-withdrawal-history")
    public List<ExchangerDepositWithdrawalTransactions> synchronizeDWTransactionsHistory(@RequestAttribute Long userId) {
        return exchangerService.synchronizeDepositWithdrawalTransactionsData(userId);
    }

}
