package com.microservice.payment.helper.paymentvalidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentCommand {
    final List<Command<?>> commands;
    final List<Object> result;

    public PaymentCommand(final CommandBuilder builder) {
        this.commands = Collections.unmodifiableList(builder.commands);
        this.result = new ArrayList<>();
    }

    public List<?> executeAll() {
        for(var eachCommand: commands)
            result.add(eachCommand.execute());
        return result;
    }

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
