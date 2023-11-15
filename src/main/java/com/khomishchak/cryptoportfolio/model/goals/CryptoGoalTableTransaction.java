package com.khomishchak.cryptoportfolio.model.goals;

import com.khomishchak.cryptoportfolio.model.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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