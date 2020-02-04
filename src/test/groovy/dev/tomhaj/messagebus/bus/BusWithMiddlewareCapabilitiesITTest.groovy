package dev.tomhaj.messagebus.bus

import dev.tomhaj.messagebus.handler.*
import dev.tomhaj.messagebus.middleware.HandledFullyBeforeNextMiddleware
import dev.tomhaj.messagebus.middleware.MessageHandlerMiddleware
import dev.tomhaj.messagebus.middleware.Middleware
import spock.lang.Specification

import java.util.function.Consumer

class BusWithMiddlewareCapabilitiesITTest extends Specification {
    def stack = new ArrayList<String>()
    def logMiddleWare = new Middleware() {
        @Override
        void dispatch(Message message, Consumer<Message> next) {
            stack.add(String.format("Started handling %s", message.getClass().getSimpleName()));
            next.accept(message)
            stack.add(String.format("Finished handling %s", message.getClass().getSimpleName()));
        }
    }

    def "command bus"() {
        given:
        def bus = new BusWithMiddlewareCapabilities(List.of(
                new HandledFullyBeforeNextMiddleware(),
                logMiddleWare,
                new MessageHandlerMiddleware(
                        new HandlerResolverSingle(
                                new HandlingMessageReflectionResolver(),
                                List.of(new FooHandler(stack), new BarHandler(stack))
                        )
                )
        ))

        when:
        bus.dispatch(new FooMessage())

        then:
        stack.size() == 3
        stack.get(0) == "Started handling FooMessage"
        stack.get(1) == "foo handled"
        stack.get(2) == "Finished handling FooMessage"
    }

    def "event bus"() {
        given:
        def resolver = new HandlerResolverComposite(
                new HandlingMessageReflectionResolver(),
                List.of(new FooHandler(stack), new FooHandlerSecond(stack), new BarHandler(stack))
        );
        def bus = new BusWithMiddlewareCapabilities(List.of(
                new HandledFullyBeforeNextMiddleware(),
                logMiddleWare,
                new MessageHandlerMiddleware(resolver)
        ))
        def field = resolver.getClass().getDeclaredField("messageToHandlerMap")
        field.setAccessible(true);
        ((Map<Class, List>) field.get(resolver)).get(FooMessage.class).add(new FooHandlerThird(bus))

        when:
        bus.dispatch(new FooMessage())

        then:
        stack.size() == 7
        stack.get(0) == "Started handling FooMessage"
        stack.get(1) == "foo handled"
        stack.get(2) == "foo handled in second handler"
        stack.get(3) == "Finished handling FooMessage"
        stack.get(4) == "Started handling BarMessage"
        stack.get(5) == "bar handled"
        stack.get(6) == "Finished handling BarMessage"
    }
}
