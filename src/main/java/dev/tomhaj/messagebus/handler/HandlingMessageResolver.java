package dev.tomhaj.messagebus.handler;

public interface HandlingMessageResolver {
    Class<Message> resolveHandledMessage(Class<? extends Handler> handlerClass);
}
