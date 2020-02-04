package dev.tomhaj.messagebus.middleware

import dev.tomhaj.messagebus.bus.BusWithMiddlewareCapabilities
import dev.tomhaj.messagebus.handler.Message
import spock.lang.Specification

import java.util.function.Consumer

class HandledFullyBeforeNextMiddlewareTest extends Specification {
    def "finish handling message before handling next" () {
        given:
        def firstMessage = new Message() {
            @Override
            String toString() {
                return "first";
            }
        }

        def secondMessage = new Message() {
            @Override
            String toString() {
                return "second"
            }
        }

        def stack = new ArrayList()
        def bus = new BusWithMiddlewareCapabilities(List.of(new HandledFullyBeforeNextMiddleware()))
        def field = bus.getClass().getDeclaredField("middlewares")
        field.setAccessible(true);
        ((List) field.get(bus)).add(
                new Middleware() {
                    @Override
                    void dispatch(Message message, Consumer<Message> next) {
                        if (message.toString().equals(firstMessage.toString())) {
                            stack.add("start handling original message")
                            bus.dispatch(secondMessage)
                            stack.add("finished handling original message")
                        } else if (message.toString().equals(secondMessage.toString())) {
                            stack.add("start handling new message")
                            stack.add("finished handling new message")
                        }
                    }
                }
        )

        when:
        bus.dispatch(firstMessage)

        then:
        stack.get(0) == "start handling original message"
        stack.get(1) == "finished handling original message"
        stack.get(2) == "start handling new message"
        stack.get(3) == "finished handling new message"
    }
}
