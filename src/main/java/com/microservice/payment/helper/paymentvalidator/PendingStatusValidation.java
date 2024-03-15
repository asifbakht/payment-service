package com.microservice.payment.helper.paymentvalidator;

import com.microservice.payment.dto.payment.PaymentStatus;
import lombok.extern.log4j.Log4j2;

/**
 * this class is associated with Payment command pattern
 * and it validates current payment record pending status
 *
 * @author Asif Bakht
 * @since 2024
 */
@Log4j2
public class PendingStatusValidation implements Command<String> {

    public final String status;

    public PendingStatusValidation(final String status) {
        this.status = status;
    }

    /**
     * validate the current record status is in pending status
     * if its not then exception is thrown
     *
     * @return {@link String} return current payment status
     */
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