package com.social.auth.helper.paymentvalidator;

import com.social.auth.dto.payment.PaymentStatus;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MinimumModificationValidation implements Command<String> {

    public final int currentModification;
    public final int totalModification;

    public MinimumModificationValidation(final int currentModification,
                                         final int totalModification) {

        this.currentModification = currentModification;
        this.totalModification  = totalModification;
    }

    @Override
    public String execute() {
        log.info("Validating current modification: {}", currentModification);
        if (currentModification < totalModification) {
            log.info(String.format("Total %s modification remaining: {}", currentModification - totalModification));
            return "Allowed";
        } else {
            throw new IllegalArgumentException("Payment modification exhausted");
        }
    }
}