package com.social.auth.service.impl;

import com.social.auth.dto.paymentmethod.PayType;
import com.social.auth.dto.paymentmethod.PaymentMethodDTO;
import com.social.auth.entity.PaymentMethod;
import com.social.auth.exception.DuplicateException;
import com.social.auth.exception.GenericException;
import com.social.auth.exception.NotFoundException;
import com.social.auth.helper.bankvalidator.BankValidator;
import com.social.auth.helper.cardvalidator.CardValidator;
import com.social.auth.mapper.PaymentMethodMapper;
import com.social.auth.repository.PaymentMethodRepository;
import com.social.auth.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.social.auth.utils.Constants.ACCOUNT_NUMBER_INVALID;
import static com.social.auth.utils.Constants.CARD_ALREADY_EXPIRED;
import static com.social.auth.utils.Constants.CARD_NUMBER_INVALID;
import static com.social.auth.utils.Constants.NOT_FOUND;
import static com.social.auth.utils.Constants.PAYMENT_ALREADY_EXISTS;
import static com.social.auth.utils.Constants.ROUTING_NUMBER_INVALID;


@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper mapper;

    @Override
    public PaymentMethodDTO add(PaymentMethodDTO paymentMethodDTO) {
        log.info("Add function initiated");
        log.trace("PaymentMethodDTO: {}", paymentMethodDTO);
        validatePaymentMethod(Boolean.TRUE, paymentMethodDTO);
        final PaymentMethod paymentMethod = paymentMethodRepository.save(mapper.toEntity(paymentMethodDTO));
        log.trace("Payment method added successfully");
        return mapper.toDTO(paymentMethod);
    }

    @Override
    public PaymentMethodDTO update(final String id, final PaymentMethodDTO paymentMethodDTO) {
        log.info("Payment method updating");
        log.trace("PaymentMethodDTO: {}", paymentMethodDTO);
        paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        validatePaymentMethod(Boolean.FALSE, paymentMethodDTO);
        final PaymentMethod updatedPaymentMethod = mapper.toEntity(paymentMethodDTO);
        updatedPaymentMethod.setId(id);
        paymentMethodRepository.save(updatedPaymentMethod);
        log.info("Payment method updated");
        return mapper.toDTO(updatedPaymentMethod);
    }

    @Override
    public PaymentMethodDTO get(final String id) {
        log.info("Payment method fetching for id: {} ", id);
        final PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        log.info("Payment method fetched");
        return mapper.toDTO(paymentMethod);
    }

    @Override
    public void delete(final String id) {
        log.info("Payment method deleting for id: {} ", id);
        final PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        paymentMethodRepository.delete(paymentMethod);
        log.info("Payment method deleted");
    }

    @Override
    public Page<PaymentMethodDTO> getAll(final Pageable pageable, final String customerId) {
        return paymentMethodRepository
                .findAllByCustomerId(customerId, pageable)
                .map(mapper::toDTO);
    }

    private void validatePaymentMethod(final boolean isAdd, final PaymentMethodDTO paymentMethodDTO) {
        log.info("Validating payment method information");
        final PayType paymentType = PayType.paymentType(paymentMethodDTO.getPaymentType());
        switch (paymentType) {
            case CARD -> {
                log.info("Payment type received: {}", paymentMethodDTO.getPaymentType());
                if (CardValidator.isExpired(paymentMethodDTO))
                    throw new GenericException(CARD_ALREADY_EXPIRED);
                if (CardValidator.isInvalid(paymentMethodDTO))
                    throw new GenericException(CARD_NUMBER_INVALID);
                if (isAdd)
                    paymentMethodRepository
                        .findByCustomerIdAndCardNumber(
                                paymentMethodDTO.getCustomerId(),
                                paymentMethodDTO.getCardNumber()
                        )
                        .ifPresent(paymentMethod -> {
                            log.info("Payment with same card number already exists");
                            throw new DuplicateException(PAYMENT_ALREADY_EXISTS);
                        });
            }
            case BANK_ACCOUNT -> {
                log.info("Payment type received: {}", paymentMethodDTO.getPaymentType());
                if (BankValidator.isValidRouting(paymentMethodDTO.getRoutingNumber()))
                    throw new GenericException(ROUTING_NUMBER_INVALID);
                if (BankValidator.isValidAccountNumber(paymentMethodDTO.getAccountNumber()))
                    throw new GenericException(ACCOUNT_NUMBER_INVALID);
                if (isAdd)
                    paymentMethodRepository
                        .findByCustomerIdAndAccountNumber(
                                paymentMethodDTO.getCustomerId(),
                                paymentMethodDTO.getAccountNumber()
                        )
                        .ifPresent(s -> {
                            log.info("Payment with same bank details already exists");
                            throw new DuplicateException(PAYMENT_ALREADY_EXISTS);
                        });
            }
        }
        log.info("Validation completed");
    }

}
