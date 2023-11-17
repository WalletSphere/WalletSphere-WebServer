package com.khomishchak.cryptoportfolio.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@MappedSuperclass
public class Transaction {

    @Id
    private String transactionId;
    private String transactionHash;
    private String ticker;
    private BigDecimal fee;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
