package com.khomishchak.ws.repositories;

import com.khomishchak.ws.model.enums.ExchangerCode;
import com.khomishchak.ws.model.exchanger.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findByCodeAndUser_Id(ExchangerCode code, Long userId);
    List<Balance> findAllByUser_Id(Long userId);
    Balance deleteByUser_IdAndCode(long userId, ExchangerCode code);
}
