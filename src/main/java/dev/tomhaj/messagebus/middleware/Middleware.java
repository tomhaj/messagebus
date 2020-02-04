package dev.tomhaj.messagebus.middleware;

import java.util.function.Consumer;

import dev.tomhaj.messagebus.handler.Message;

public interface Middleware {
    void dispatch(Message message, Consumer<Message> next);
}
