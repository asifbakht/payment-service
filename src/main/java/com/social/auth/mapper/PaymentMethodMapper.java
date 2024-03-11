package com.social.auth.mapper;

import com.social.auth.dto.paymentmethod.PaymentMethodDTO;
import com.social.auth.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {
    PaymentMethod toEntity(final PaymentMethodDTO paymentMethodDTO);

    @Mapping(target="cvv", ignore = true)
    @Mapping(target="accountNumber", qualifiedByName = "maskAccountNumber")
    @Mapping(target="cardNumber", qualifiedByName = "maskCardNumber")
    PaymentMethodDTO toDTO(final PaymentMethod paymentMethod);


    @Named("maskAccountNumber")
    default String mastAccountNumber(final String accountNumber){
        if (Objects.nonNull(accountNumber))
            return accountNumber.replaceAll(".(?=.{4})", "#");
        return accountNumber;
    }

    @Named("maskCardNumber")
    default String maskCardNumber(final String cardNumber) {
        if (Objects.nonNull(cardNumber))
            return cardNumber.replaceAll(".(?=.{4})", "#");
        return cardNumber;
    }
}
