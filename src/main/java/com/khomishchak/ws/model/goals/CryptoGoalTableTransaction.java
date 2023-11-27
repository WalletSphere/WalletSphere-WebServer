package com.khomishchak.ws.model.goals;

import com.khomishchak.ws.model.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "crypto_goal_table_transactions")
public class CryptoGoalTableTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private TransactionType transactionType;

    private BigDecimal quantity;
    private BigDecimal averagePrice;
}
