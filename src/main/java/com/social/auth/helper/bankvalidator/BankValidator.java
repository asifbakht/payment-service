package com.social.auth.helper.bankvalidator;

import lombok.extern.log4j.Log4j2;

import java.util.Objects;

import static com.social.auth.utils.Constants.ACCOUNT_NUMBER_INVALID;
import static com.social.auth.utils.Constants.BANK_ACCOUNT_REGEX;
import static com.social.auth.utils.Constants.NUMERIC_REGEX;
import static com.social.auth.utils.Constants.ROUTING_LENGTH;
import static com.social.auth.utils.Constants.ROUTING_NUMBER_INVALID;

@Log4j2
public class BankValidator {
    public static boolean isValidRouting(final String routingNumber) {
       log.info("Validating routing number");
        Objects.requireNonNull(routingNumber, ROUTING_NUMBER_INVALID);
        if (routingNumber.isBlank() ||
                !routingNumber.matches(NUMERIC_REGEX) ||
                routingNumber.length() != ROUTING_LENGTH)
            return Boolean.FALSE;

        int n = 0;
        for (int i = 0; i < routingNumber.length(); i += 3) {
            final char firstNumber = routingNumber.charAt(i);
            final char secondNumber = routingNumber.charAt(i + 1);
            final char thirdNumber = routingNumber.charAt(i + 2);

            n += Integer.parseInt(Character.toString(firstNumber), 10) * 3
                    + Integer.parseInt(Character.toString(secondNumber), 10) * 7
                    + Integer.parseInt(Character.toString(thirdNumber), 10);
        }
        final boolean isInvalidRoute = !(n != 0 && n % 10 == 0);
        log.info("Routing number valid: {}", !isInvalidRoute);
        return isInvalidRoute;
    }


    public static boolean isValidAccountNumber(final String accountNumber) {
        log.info("Validating bank account number");
        Objects.requireNonNull(accountNumber, ACCOUNT_NUMBER_INVALID);
        final boolean isInvalidAccountNo =  (accountNumber.isBlank() ||
                !accountNumber.matches(NUMERIC_REGEX) ||
                !accountNumber.matches(BANK_ACCOUNT_REGEX));
        log.info("Account number valid: {}", !isInvalidAccountNo);
        return isInvalidAccountNo;
    }
}
