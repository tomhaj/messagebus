package dev.tomhaj.messagebus.handler;

public interface Handler<T extends Message> {
    void handle(T message);
}
