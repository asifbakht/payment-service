package com.microservice.payment.service.impl.impl;

import com.microservice.payment.dto.paymentmethod.PayType;
import com.microservice.payment.dto.paymentmethod.PaymentMethodDTO;
import com.microservice.payment.entity.PaymentMethod;
import com.microservice.payment.exception.DuplicateException;
import com.microservice.payment.exception.GenericException;
import com.microservice.payment.exception.NotFoundException;
import com.microservice.payment.helper.bankvalidator.BankValidator;
import com.microservice.payment.helper.cardvalidator.CardValidator;
import com.microservice.payment.mapper.PaymentMethodMapper;
import com.microservice.payment.repository.PaymentMethodRepository;
import com.microservice.payment.service.impl.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.microservice.payment.utils.Constants.ACCOUNT_NUMBER_INVALID;
import static com.microservice.payment.utils.Constants.CARD_ALREADY_EXPIRED;
import static com.microservice.payment.utils.Constants.CARD_NUMBER_INVALID;
import static com.microservice.payment.utils.Constants.NOT_FOUND;
import static com.microservice.payment.utils.Constants.PAYMENT_ALREADY_EXISTS;
import static com.microservice.payment.utils.Constants.ROUTING_NUMBER_INVALID;

/**
 * Payment method crud operation functionality resides here
 *
 * @author Asif Bakht
 * @since 2024
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper mapper;


    /**
     * add payment method to database
     *
     * @param paymentMethodDTO {@link PaymentMethodDTO} payment method dto object
     * @return {@link PaymentMethodDTO} payment method dto with id populated
     */
    @Override
    public PaymentMethodDTO add(PaymentMethodDTO paymentMethodDTO) {
        log.info("Add function initiated");
        log.trace("PaymentMethodDTO: {}", paymentMethodDTO);
        validatePaymentMethod(Boolean.TRUE, paymentMethodDTO);
        final PaymentMethod paymentMethod = paymentMethodRepository.save(mapper.toEntity(paymentMethodDTO));
        log.trace("Payment method added successfully");
        return mapper.toDTO(paymentMethod);
    }

    /**
     * update existing payment method to database
     *
     * @param id               {@link String} payment method id
     * @param paymentMethodDTO {@link PaymentMethodDTO} payment method dto object
     * @return {@link PaymentMethodDTO} payment method dto
     */
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

    /**
     * fetch payment method from database
     *
     * @param id {@link String} payment method id
     * @return {@link PaymentMethodDTO} payment method dto
     */
    @Override
    public PaymentMethodDTO get(final String id) {
        log.info("Payment method fetching for id: {} ", id);
        final PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        log.info("Payment method fetched");
        return mapper.toDTO(paymentMethod);
    }

    /**
     * delete payment method from database
     *
     * @param id {@link String} payment method id
     */
    @Override
    public void delete(final String id) {
        log.info("Payment method deleting for id: {} ", id);
        final PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        paymentMethodRepository.delete(paymentMethod);
        log.info("Payment method deleted");
    }

    /**
     * return customer's all payment method from database with
     * paginated properties
     *
     * @param pageable   {@link Pageable} paginated properties
     * @param customerId {@link String} customer id
     */
    @Override
    public Page<PaymentMethodDTO> getAll(final Pageable pageable, final String customerId) {
        return paymentMethodRepository
                .findAllByCustomerId(customerId, pageable)
                .map(mapper::toDTO);
    }

    /**
     * validate payment method
     * if its card then card number and its expiry is validated
     * if its bank account then its routing no and account no is validated
     * <p>
     * isAdd helps to identify if its first time adding payment to database
     *
     * @param isAdd            {@link Boolean} indicate if its add function or not
     * @param paymentMethodDTO {@link PaymentMethodDTO} payment method dto object
     */
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
