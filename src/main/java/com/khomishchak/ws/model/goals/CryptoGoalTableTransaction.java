package com.khomishchak.ws.model.goals;

import com.khomishchak.ws.model.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
