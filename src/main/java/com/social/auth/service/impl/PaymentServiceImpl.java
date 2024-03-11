package com.social.auth.service.impl;

import com.social.auth.dto.payment.PaymentDTO;
import com.social.auth.dto.payment.PaymentStatus;
import com.social.auth.entity.Payment;
import com.social.auth.exception.NotFoundException;
import com.social.auth.helper.paymentvalidator.MinimumAmountValidation;
import com.social.auth.helper.paymentvalidator.MinimumModificationValidation;
import com.social.auth.helper.paymentvalidator.PaymentCommand;
import com.social.auth.helper.paymentvalidator.PendingStatusValidation;
import com.social.auth.mapper.PaymentMapper;
import com.social.auth.repository.PaymentRepository;
import com.social.auth.service.PaymentService;
import com.social.auth.utils.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.social.auth.utils.Constants.MINIMUM_PAYMENT_AMOUNT;
import static com.social.auth.utils.Constants.PAYMENT_NOT_FOUND;
import static com.social.auth.utils.Helper.getProcessingDateTime;

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

    @Override
    public void pay(final PaymentDTO paymentDTO) {
        log.info("Payment adding in process...");
        log.trace("Payment information: {}", paymentDTO);
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
    }

    @Override
    public void update(final String id, final PaymentDTO paymentDTO) {
        log.info("Processing to update pay: {}", id);
        log.trace("Updated payment: {}", paymentDTO);
        final Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(PAYMENT_NOT_FOUND));
        log.trace("Previous payment: {}", payment);
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
        paymentRepository.save(payment);
        log.info("Payment update completed");
    }

    @Override
    public void cancel(final String id) {
        log.info("Cancelling payment in process: {}", id);
        final Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(PAYMENT_NOT_FOUND));
        new PaymentCommand.CommandBuilder()
                .addCommand(new PendingStatusValidation(payment.getStatus()))
                .build().executeAll();
        payment.setStatus(PaymentStatus.CANCELLED.name());
        paymentRepository.save(payment);
        log.info("Payment saved with cancel status");
    }


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
