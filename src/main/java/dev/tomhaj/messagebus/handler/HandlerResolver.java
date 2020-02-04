package dev.tomhaj.messagebus.handler;

public interface HandlerResolver {
    Handler<Message> resolveForMessage(Class<? extends Message> messageClass);
}
