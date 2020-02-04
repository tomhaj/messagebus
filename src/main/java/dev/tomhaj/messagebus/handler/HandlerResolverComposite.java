package dev.tomhaj.messagebus.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class HandlerResolverComposite implements HandlerResolver {
    private final Map<Class<Message>, List<Handler<Message>>> messageToHandlerMap = new HashMap<>();

    public HandlerResolverComposite(HandlingMessageResolver messageResolver, List<Handler<Message>> handlers) {
        buildHandlerMap(messageResolver, handlers);
    }

    @Override
    public Handler<Message> resolveForMessage(Class<? extends Message> messageClass) {
        List<? extends Handler<Message>> handlersForMessage = messageToHandlerMap.get(messageClass);

        if (Objects.isNull(handlersForMessage)) {
            return message -> {};
        }

        return message -> handlersForMessage.forEach(h -> h.handle(message));
    }

    private void buildHandlerMap(HandlingMessageResolver messageResolver, List<Handler<Message>> handlers) {
        handlers.forEach(h -> {
            Class<Message> handledMessageClass = messageResolver.resolveHandledMessage(h.getClass());

            if (!messageToHandlerMap.containsKey(handledMessageClass)) {
                messageToHandlerMap.put(handledMessageClass, new ArrayList<>());
            }

            if (messageToHandlerMap.get(handledMessageClass).contains(h)) {
                return;
            }

            messageToHandlerMap.get(handledMessageClass).add(h);
        });
    }
}
