package com.khomishchak.cryptoportfolio.services.exchangers;

import com.khomishchak.cryptoportfolio.model.User;
import com.khomishchak.cryptoportfolio.model.enums.ExchangerCode;
import com.khomishchak.cryptoportfolio.model.enums.RegistrationStatus;
import com.khomishchak.cryptoportfolio.model.exchanger.ApiKeySetting;
import com.khomishchak.cryptoportfolio.model.exchanger.ApiKeysPair;
import com.khomishchak.cryptoportfolio.model.exchanger.Balance;
import com.khomishchak.cryptoportfolio.model.exchanger.trasaction.ExchangerDepositWithdrawalTransactions;
import com.khomishchak.cryptoportfolio.model.requests.RegisterApiKeysReq;
import com.khomishchak.cryptoportfolio.model.requests.RegisterExchangerInfoReq;
import com.khomishchak.cryptoportfolio.model.response.FirstlyGeneratedBalanceResp;
import com.khomishchak.cryptoportfolio.model.response.SyncDataResp;
import com.khomishchak.cryptoportfolio.repositories.ApiKeySettingRepository;
import com.khomishchak.cryptoportfolio.repositories.UserRepository;

import com.khomishchak.cryptoportfolio.services.exchangers.balance.BalancePricingService;
import com.khomishchak.cryptoportfolio.services.exchangers.balance.BalanceService;
import com.khomishchak.cryptoportfolio.services.exchangers.balance.history.AccountBalanceTransferOperationsHistoryService;
import com.khomishchak.cryptoportfolio.services.security.encryption.AesEncryptionService;
import org.springframework.stereotype.Service;

import java.util.List;

import jakarta.transaction.Transactional;

@Service
public class ExchangerServiceImpl implements ExchangerService {

    private final UserRepository userRepository;
    private final ApiKeySettingRepository apiKeySettingRepository;
    private final AesEncryptionService aesEncryptionService;
    private final BalanceService balanceService;
    private final BalancePricingService balancePricingService;
    private final AccountBalanceTransferOperationsHistoryService accountBalanceTransferOperationsHistoryService;

    public ExchangerServiceImpl(UserRepository userRepository, ApiKeySettingRepository apiKeySettingRepository,
                                AesEncryptionService aesEncryptionService, BalanceService balanceService,
                                BalancePricingService balancePricingService,
                                AccountBalanceTransferOperationsHistoryService accountBalanceTransferOperationsHistoryService) {
        this.userRepository = userRepository;
        this.apiKeySettingRepository = apiKeySettingRepository;
        this.aesEncryptionService = aesEncryptionService;
        this.balanceService = balanceService;
        this.balancePricingService = balancePricingService;
        this.accountBalanceTransferOperationsHistoryService = accountBalanceTransferOperationsHistoryService;
    }

    @Override
    @Transactional
    public FirstlyGeneratedBalanceResp addGeneralExchangerInfo(RegisterExchangerInfoReq exchangerInfoReq, long userId) {
        User user = userRepository.getReferenceById(userId);
        RegisterApiKeysReq apiKeys = exchangerInfoReq.apiKeysReq();
        ExchangerCode code = exchangerInfoReq.code();

        persistApiKeysSettings(user, apiKeys.secretKey(), apiKeys.publicKey(), code);

        Balance emptyBalance = balanceService.registerBalanceEntryInfo(code, exchangerInfoReq.balanceName(), user);

        return new FirstlyGeneratedBalanceResp(emptyBalance.getId(), userId, RegistrationStatus.SUCCESSFUL);
    }

    @Override
    public Balance getMainBalance(long userId, ExchangerCode exchangerCode) {
        Balance balance = balanceService.getMainBalance(userId, exchangerCode);
        balancePricingService.calculateBalanceValueUpToDate(balance);
        return balance;
    }

    @Override
    public List<Balance> getAllMainBalances(long userId) {
        return balanceService.getMainBalances(userId);
    }

    @Override
    public List<ExchangerDepositWithdrawalTransactions> getWithdrawalDepositWalletHistory(long userId) {
        return accountBalanceTransferOperationsHistoryService.getDepositWithdrawalTransactionsHistory(userId);
    }

    @Override
    public SyncDataResp synchronizeBalanceDataForUser(long userId) {
        List<Balance> balances = balanceService.synchronizeBalances(userId);
        return new SyncDataResp(balances);
    }

    @Override
    public List<ExchangerDepositWithdrawalTransactions> synchronizeDepositWithdrawalTransactionsData(long userId) {
        return accountBalanceTransferOperationsHistoryService.synchronizeDepositWithdrawalTransactionsHistory(userId);
    }

    @Override
    public void deleteExchangerForUser(long balanceId) {
        balanceService.deleteBalance(balanceId);
    }

    private void persistApiKeysSettings(User user, String privateKey, String publicApi, ExchangerCode code) {
        ApiKeysPair apiKeysPair = ApiKeysPair.builder()
                .publicApi(aesEncryptionService.encrypt(publicApi))
                .privateKey(aesEncryptionService.encrypt(privateKey))
                .build();

        ApiKeySetting apiKeySetting = ApiKeySetting.
                builder()
                .user(user)
                .code(code)
                .apiKeys(apiKeysPair)
                .build();

        user.getApiKeysSettings().add(apiKeySetting);

        apiKeySettingRepository.save(apiKeySetting);
    }
}
