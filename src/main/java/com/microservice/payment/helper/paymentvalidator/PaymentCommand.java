package com.microservice.payment.helper.paymentvalidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command pattern builder that collects all command and execute
 *
 * @author Asif Bakht
 * @since 2024
 */
public class PaymentCommand {
    final List<Command<?>> commands;
    final List<Object> result;

    public PaymentCommand(final CommandBuilder builder) {
        this.commands = Collections.unmodifiableList(builder.commands);
        this.result = new ArrayList<>();
    }

    /**
     * return all command execution result
     *
     * @return {@link List<?>} list of all commands result
     */
    public List<?> executeAll() {
        for (var eachCommand : commands)
            result.add(eachCommand.execute());
        return result;
    }

    /**
     * Builder class that build commands
     */
    public static class CommandBuilder {
        private final List<Command<?>> commands = new ArrayList<>();

        public CommandBuilder addCommand(final Command<?> command) {
            commands.add(command);
            return this;
        }

        public PaymentCommand build() {
            return new PaymentCommand(this);
        }
    }
}
