package com.khomishchak.ws.model.goals;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Embeddable
public class CryptoGoalsTableRecord {
    private String name;
    private BigDecimal quantity;
    private BigDecimal averageCost;
    private BigDecimal goalQuantity;

    @Transient
    private BigDecimal donePercentage;

    @Transient
    private BigDecimal leftToBuy;

    @Transient
    private boolean finished;
}
