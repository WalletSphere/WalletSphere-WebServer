package com.khomishchak.ws.model.response;

import com.khomishchak.ws.model.exchanger.Balance;

import java.util.List;

public record SyncBalancesResp(List<Balance> synchronizedBalances) {
}
