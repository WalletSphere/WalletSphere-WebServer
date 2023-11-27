package com.khomishchak.ws.model.exchanger.transaction;


import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
