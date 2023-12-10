package com.khomishchak.ws.repositories.custom.impl;

import com.khomishchak.ws.model.enums.ExchangerCode;
import com.khomishchak.ws.model.exchanger.ApiKeysPair;
import com.khomishchak.ws.model.exchanger.DecryptedApiKeySettingDTO;
import com.khomishchak.ws.repositories.ApiKeySettingRepository;
import com.khomishchak.ws.repositories.custom.ApiKeySettingCustomRepository;
import com.khomishchak.ws.services.security.encryption.AesEncryptionService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiKeySettingCustomRepositoryImpl implements ApiKeySettingCustomRepository {

    private final ApiKeySettingRepository apiKeySettingRepository;
    private final AesEncryptionService aesEncryptionService;

    public ApiKeySettingCustomRepositoryImpl(ApiKeySettingRepository apiKeySettingRepository, AesEncryptionService aesEncryptionService) {
        this.apiKeySettingRepository = apiKeySettingRepository;
        this.aesEncryptionService    = aesEncryptionService;
    }

    @Override
    public List<DecryptedApiKeySettingDTO> findAllByUserIdDecrypted(long userId) {
        return apiKeySettingRepository.findAllByUserId(userId).stream()
                .map(settings -> {
                    ApiKeysPair encryptedKeysPair = settings.getApiKeys();
                    return buildDecryptedApiKeySettingDTO(encryptedKeysPair, settings.getCode());
                }).toList();
    }

    private DecryptedApiKeySettingDTO buildDecryptedApiKeySettingDTO(ApiKeysPair encryptedKeysPair, ExchangerCode code) {
        String decryptedPrivateKey = decryptKey(encryptedKeysPair.getPrivateKey());
        String decryptedPublicApi = decryptKey(encryptedKeysPair.getPublicApi());
        return DecryptedApiKeySettingDTO.builder()
                .publicKey(decryptedPublicApi)
                .privateKey(decryptedPrivateKey)
                .code(code)
                .build();
    }

    private String decryptKey(String encryptedKey) {
        return aesEncryptionService.decrypt(encryptedKey);
    }
}
