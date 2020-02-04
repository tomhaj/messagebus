package dev.tomhaj.messagebus.handler;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public final class HandlingMessageReflectionResolver implements HandlingMessageResolver {
    @Override
    public Class<Message> resolveHandledMessage(Class<? extends Handler> handlerClass) {
        return (Class<Message>) List
                .of(handlerClass.getGenericInterfaces())
                .stream()
                .map(t -> (ParameterizedType) t)
                .filter(t -> List .of(t.getActualTypeArguments())
                        .stream()
                        .filter(st -> {
                            Class<?> clazz = ((Class) st);
                            Optional<Class<?>> superClazz = List
                                    .of(clazz.getInterfaces())
                                    .stream()
                                    .filter(pi -> pi.equals(Message.class))
                                    .findAny();

                            return superClazz.isPresent();
                        })
                        .findAny()
                        .isPresent()
                ).findAny()
                .get()
                .getActualTypeArguments()[0];
    }
}
