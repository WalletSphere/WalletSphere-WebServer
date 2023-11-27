package com.khomishchak.ws.services.goal.impl;

import com.khomishchak.ws.model.TransferTransactionType;
import com.khomishchak.ws.model.exchanger.transaction.ExchangerDepositWithdrawalTransactions;
import com.khomishchak.ws.model.exchanger.transaction.Transaction;
import com.khomishchak.ws.model.goals.CommonGoalType;
import com.khomishchak.ws.model.goals.CryptoGoalsTableRecord;
import com.khomishchak.ws.model.goals.GoalType;
import com.khomishchak.ws.model.goals.SelfGoal;
import com.khomishchak.ws.services.exchangers.ExchangerService;
import com.khomishchak.ws.services.goal.SelfGoalValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
public class DepositSelfGoalValidator implements SelfGoalValidator {

    public static final int END_OF_PREVIOUS_PERIOD = 2;
    public static final int END_OF_CURRENT_PERIOD = 1;
    public static final int PERCENTAGE_SCALE = 100;

    private final ExchangerService exchangerService;

    public DepositSelfGoalValidator(ExchangerService exchangerService) {
        this.exchangerService = exchangerService;
    }

    @Override
    public CommonGoalType getCommonGoalType() {
        return CommonGoalType.DEPOSIT_GOAL;
    }

    @Override
    public boolean isAchieved(SelfGoal goal) {
        GoalType goalType = goal.getGoalType();
        double depositValue = getDepositValueForPeriod(goal.getUser().getId(), goal.getTicker(),
                goalType.getStartTime(END_OF_PREVIOUS_PERIOD), goalType.getStartTime(END_OF_CURRENT_PERIOD));

        goal.setCurrentAmount(depositValue);
        return depositValue > goal.getGoalAmount();
    }

    private double getDepositValueForPeriod(long userId, String ticker, LocalDateTime startingDate,
                                            LocalDateTime endingDate) {
        return exchangerService.getWithdrawalDepositWalletHistory(userId).stream()
                .map(transactions -> getDepositValueForPeriodForSingleIntegratedBalance(transactions, ticker, startingDate, endingDate))
                .reduce(0.0, Double::sum);
    }

    private double getDepositValueForPeriodForSingleIntegratedBalance(ExchangerDepositWithdrawalTransactions transactions,
                                                                      String ticker, LocalDateTime startingDate,
                                                                      LocalDateTime endingDate) {
        return transactions.getTransactions().stream()
                .filter(transaction -> transaction.getTicker().equalsIgnoreCase(ticker) &&
                        transaction.getTransferTransactionType().equals(TransferTransactionType.DEPOSIT) &&
                        transaction.getCreatedAt().isAfter(startingDate) && transaction.getCreatedAt().isBefore(endingDate))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
    }

    private void setPostQuantityValues(CryptoGoalsTableRecord entity) {
        BigDecimal goalQuantity = entity.getGoalQuantity();
        BigDecimal quantity = entity.getQuantity();

        BigDecimal leftToBuy = goalQuantity.subtract(quantity);

        entity.setLeftToBuy(leftToBuy.compareTo(BigDecimal.ZERO) >= 0 ? goalQuantity.subtract(quantity) : BigDecimal.ZERO);
        entity.setDonePercentage(quantity
                .multiply(BigDecimal.valueOf(PERCENTAGE_SCALE))
                .divide(goalQuantity, 1, RoundingMode.DOWN));
        entity.setFinished(quantity.compareTo(goalQuantity) >= 0);
    }
}
