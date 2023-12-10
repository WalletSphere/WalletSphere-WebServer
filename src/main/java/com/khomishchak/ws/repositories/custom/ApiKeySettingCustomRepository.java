package com.khomishchak.ws.repositories.custom;

import com.khomishchak.ws.model.exchanger.DecryptedApiKeySettingDTO;

import java.util.List;

public interface ApiKeySettingCustomRepository {
    List<DecryptedApiKeySettingDTO> findAllByUserIdDecrypted(long userId);
}
