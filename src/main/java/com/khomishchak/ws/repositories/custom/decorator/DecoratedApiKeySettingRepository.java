package com.khomishchak.ws.repositories.custom.decorator;

import com.khomishchak.ws.repositories.ApiKeySettingRepository;
import com.khomishchak.ws.repositories.custom.ApiKeySettingCustomRepository;

public interface DecoratedApiKeySettingRepository extends ApiKeySettingCustomRepository, ApiKeySettingRepository {
}
