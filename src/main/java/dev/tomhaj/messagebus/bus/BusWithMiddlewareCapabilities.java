package dev.tomhaj.messagebus.bus;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import dev.tomhaj.messagebus.handler.Message;
import dev.tomhaj.messagebus.middleware.Middleware;

public final class BusWithMiddlewareCapabilities implements Bus {
    private final List<Middleware> middlewares;

    public BusWithMiddlewareCapabilities(List<Middleware> middlewares) {
        this.middlewares = new LinkedList<>(new LinkedHashSet<>(middlewares));
    }

    @Override
    public void dispatch(Message message) {
        consumerForNextMiddleware(0).accept(message);
    }

    private Consumer<Message> consumerForNextMiddleware(int index) {
        try {
            Middleware middleware = middlewares.get(index);

            return message -> middleware.dispatch(message, consumerForNextMiddleware(index + 1));
        } catch (IndexOutOfBoundsException e) {
            return message -> {};
        }
    }
}
