package com.social.auth.helper.paymentvalidator;

import com.social.auth.dto.payment.PaymentStatus;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PendingStatusValidation implements Command<String> {

    public final String status;

    public PendingStatusValidation(final String status) {
        this.status = status;
    }

    @Override
    public String execute() {
        log.info("Validating payment status: {}", status);
        if (PaymentStatus.PENDING.name().equalsIgnoreCase(status)) {
            return status;
        } else {
            throw new IllegalArgumentException("Payment cannot be updated");
        }
    }
}