package dev.tomhaj.messagebus.middleware;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import dev.tomhaj.messagebus.handler.HandlerResolver;
import dev.tomhaj.messagebus.handler.Message;

@RequiredArgsConstructor
public class MessageHandlerMiddleware implements Middleware {
    private final HandlerResolver resolver;

    @Override
    public void dispatch(Message message, Consumer<Message> next) {
        resolver.resolveForMessage(message.getClass()).handle(message);

        next.accept(message);
    }
}
