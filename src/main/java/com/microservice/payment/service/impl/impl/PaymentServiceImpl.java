package com.microservice.payment.service.impl.impl;

import com.microservice.payment.dto.payment.PaymentDTO;
import com.microservice.payment.dto.payment.PaymentStatus;
import com.microservice.payment.entity.Payment;
import com.microservice.payment.exception.NotFoundException;
import com.microservice.payment.helper.paymentvalidator.MinimumAmountValidation;
import com.microservice.payment.helper.paymentvalidator.MinimumModificationValidation;
import com.microservice.payment.helper.paymentvalidator.PaymentCommand;
import com.microservice.payment.helper.paymentvalidator.PendingStatusValidation;
import com.microservice.payment.mapper.PaymentMapper;
import com.microservice.payment.repository.PaymentRepository;
import com.microservice.payment.service.impl.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.microservice.payment.utils.Constants.MINIMUM_PAYMENT_AMOUNT;
import static com.microservice.payment.utils.Constants.PAYMENT_NOT_FOUND;
import static com.microservice.payment.utils.Helper.getProcessingDateTime;

/**
 * Payment crud operation functionality resides here
 *
 * @author Asif Bakht
 * @since 2024
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.processing.day}")
    private int processInDays;
    @Value("${payment.modification}")
    private int totalModification;

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    /**
     * create payment in database, it will check the minimum amount
     * if less than minimum amount then throw exception
     *
     * @param paymentDTO {@link PaymentDTO} payment dto object
     * @return {@link PaymentDTO} payment dto object
     */
    @Override
    public PaymentDTO pay(final PaymentDTO paymentDTO) {
        log.info("Payment adding in process...");
        log.debug("Payment information: {}", paymentDTO);
        new PaymentCommand.CommandBuilder()
                .addCommand(new MinimumAmountValidation(
                        paymentDTO.getAmount(),
                        MINIMUM_PAYMENT_AMOUNT)
                )
                .build().executeAll();
        Payment payment = mapper.toEntity(paymentDTO);
        payment.setStatus(PaymentStatus.PENDING.name());
        payment.setProcessingTime(getProcessingDateTime(processInDays));
        payment = paymentRepository.save(payment);
        log.info("Payment add completed: {}", payment.getId());
        return mapper.toDTO(payment);
    }

    /**
     * update payment in database, it will check following:
     * 1. payment exists in database
     * 2. minimum amount if less than minimum amount
     * then throw exception
     * 3. total times modification is allowed after
     * that throw exception
     * 4. validate if the payment is in pending state
     * or else throw exception
     *
     * @param paymentDTO {@link PaymentDTO} payment dto object
     * @return {@link PaymentDTO} payment dto object
     */
    @Override
    public PaymentDTO update(final String id, final PaymentDTO paymentDTO) {
        log.info("Processing to update pay: {}", id);
        log.debug("Updated payment: {}", paymentDTO);
        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(PAYMENT_NOT_FOUND));
        log.debug("Previous payment: {}", payment);
        new PaymentCommand.CommandBuilder()
                .addCommand(new MinimumAmountValidation(
                        paymentDTO.getAmount(),
                        MINIMUM_PAYMENT_AMOUNT)
                )
                .addCommand(new MinimumModificationValidation(
                        payment.getVersion(),
                        totalModification
                ))
                .addCommand(new PendingStatusValidation(
                        payment.getStatus())
                )
                .build().executeAll();
        payment.setPaymentMethodId(paymentDTO.getPaymentMethodId());
        payment.setAmount(paymentDTO.getAmount());
        payment = paymentRepository.save(payment);
        log.info("Payment update completed");
        return mapper.toDTO(payment);
    }

    /**
     * update payment in database, it will check following:
     * 1. payment exists in database
     * 2. validate if the payment is in pending state
     * or else throw exception
     *
     * @param id {@link String} payment id
     * @return {@link PaymentDTO} payment dto object
     */
    @Override
    public PaymentDTO cancel(final String id) {
        log.info("Cancelling payment in process: {}", id);
        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(PAYMENT_NOT_FOUND));
        new PaymentCommand.CommandBuilder()
                .addCommand(new PendingStatusValidation(payment.getStatus()))
                .build().executeAll();
        payment.setStatus(PaymentStatus.CANCELLED.name());
        payment = paymentRepository.save(payment);
        log.info("Payment saved with cancel status");
        return mapper.toDTO(payment);
    }

    /**
     * retrieve payment from database by payment id
     *
     * @param id {@link String} payment id
     * @return {@link PaymentDTO} payment dto object
     */
    @Override
    public PaymentDTO get(String id) {
        log.info("Fetching payment in process: {}", id);
        final Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(PAYMENT_NOT_FOUND));
        log.debug("Payment information: {}", payment);
        log.info("Payment fetched");
        return mapper.toDTO(payment);
    }

    /**
     * get all payment of customer from database
     *
     * @param pageable   {@link Pageable} pagination properties
     * @param customerId {@link String} customer id
     * @return {@link Page<PaymentDTO>} customer list with paginated properties
     */
    @Override
    public Page<PaymentDTO> getAllPayments(final Pageable pageable, final String customerId) {
        log.info("Search payments in process");
        final Page<PaymentDTO> pagePayments = paymentRepository
                .findAllByCustomerId(customerId, pageable)
                .map(mapper::toDTO);
        log.info("Search payments completed");
        return pagePayments;
    }
}
