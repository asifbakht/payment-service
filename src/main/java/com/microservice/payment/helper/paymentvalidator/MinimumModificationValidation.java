package com.microservice.payment.helper.paymentvalidator;

import lombok.extern.log4j.Log4j2;

/**
 * this class is assoicated with command pattern that
 * is responsible to validate minimum times payment modification allowed
 *
 * @author Asif Bakht
 * @since 2024
 */
@Log4j2
public class MinimumModificationValidation implements Command<Void> {

    public final int currentModification;
    public final int totalModification;

    public MinimumModificationValidation(final int currentModification,
                                         final int totalModification) {

        this.currentModification = currentModification;
        this.totalModification = totalModification;
    }

    /**
     * validate minimum numbers of modification allowed for current
     * payment record. If modification is exhausted then exception
     * is thrown
     *
     * @return {@link Void}
     */
    @Override
    public Void execute() {
        log.info("Validating current modification: {}", currentModification);
        if (currentModification < totalModification) {
            log.info(String.format("Total %s modification remaining: {}", currentModification - totalModification));
            return null;
        } else {
            throw new IllegalArgumentException("Payment modification exhausted");
        }
    }
}