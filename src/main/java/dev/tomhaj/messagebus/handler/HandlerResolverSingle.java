package dev.tomhaj.messagebus.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class HandlerResolverSingle implements HandlerResolver {
    private final Map<Class<Message>, Handler<Message>> messageToHandlerMap = new HashMap<>();

    public HandlerResolverSingle(HandlingMessageResolver messageResolver, List<Handler<Message>> handlers) {
        buildHandlerMap(messageResolver, handlers);
    }

    @Override
    public Handler<Message> resolveForMessage(Class<? extends Message> messageClass) {
        Handler<Message> handler = messageToHandlerMap.get(messageClass);

        if (Objects.isNull(handler)) {
            return message -> {};
        }

        return handler;
    }

    private void buildHandlerMap(HandlingMessageResolver messageResolver, List<Handler<Message>> handlers) {
        handlers.forEach(h -> {
            Class<Message> handledMessageClass = messageResolver.resolveHandledMessage(h.getClass());

            if (messageToHandlerMap.containsKey(handledMessageClass)) {
                throw new IllegalStateException("TODO");
            }

            messageToHandlerMap.put(handledMessageClass, h);
        });
    }
}
