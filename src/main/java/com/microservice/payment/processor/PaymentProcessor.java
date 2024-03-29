package com.microservice.payment.processor;

import com.microservice.payment.dto.payment.PaymentStatus;
import com.microservice.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.microservice.payment.utils.Helper.getCurrentUTCDateTime;

/**
 * class that is responsible to process all pending payments
 * this class will run on scheduler bases/
 *
 * @author Asif Bakht
 * @since 2024
 */
@Log4j2
@Component
@RequiredArgsConstructor
class PaymentProcessor {

    private final PaymentRepository paymentRepository;

    /**
     * scheduler lock can be programmatically set with configured lock times
     * https://github.com/lukas-krecan/ShedLock#running-without-spring
     */

    /**
     * process pending payment scheduled wise with lock mechanism to avoid other
     * instances to process those records.
     */
    @Scheduled(cron = "${spring.cron.pending-payment}")
    @SchedulerLock(name = "pendingPaymentScheduler", lockAtLeastFor = "PT5S", lockAtMostFor = "PT10S")
    public void scheduledTask() {
        // process payment code lies here.
        // it can be processed through even driven architecture using kafka, aws sqs + lambda, rabbitMQ

        // for the purpose of task a simple process will be coded here
        // in real based scenario data will be fetched with limit to avoid
        // memory heap issue then each payment will be processed individually

        try {
            paymentRepository.processPendingPayment(PaymentStatus.PROCESSED.name(),
                    PaymentStatus.PENDING.name(),
                    getCurrentUTCDateTime());
        } catch (final Exception e) {
            log.error("Error occurred processing pending payments, error: {}", e.getMessage());
        }
    }
}
