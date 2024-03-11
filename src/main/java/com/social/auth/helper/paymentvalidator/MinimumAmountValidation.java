package com.social.auth.helper.paymentvalidator;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MinimumAmountValidation implements Command<Double> {

    private final double amount;
    private final double minimumAmount;

    public MinimumAmountValidation(final double amount,
                                   final double minimumAmount) {
        this.amount = amount;
        this.minimumAmount = minimumAmount;
    }

    @Override
    public Double execute() {
        log.info("Validating payment amount");
        if (amount > minimumAmount) {
            log.info("Payment amount is valid");
            return amount;
        } else {
            log.info("Payment amount should be greater than {}", minimumAmount);
            throw new IllegalArgumentException(String.format("Minimum amount %s is required", minimumAmount));
        }
    }
}