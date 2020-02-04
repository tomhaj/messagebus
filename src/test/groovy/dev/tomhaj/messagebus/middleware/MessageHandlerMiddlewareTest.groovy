package dev.tomhaj.messagebus.middleware

import dev.tomhaj.messagebus.bus.BusWithMiddlewareCapabilities
import dev.tomhaj.messagebus.handler.Handler
import dev.tomhaj.messagebus.handler.HandlerResolver
import dev.tomhaj.messagebus.handler.Message
import spock.lang.Specification

class MessageHandlerMiddlewareTest extends Specification {
    def "proxy message to resolved handler"() {
        given:
        def stack = new ArrayList();
        def handler = new Handler<Message>() {
            @Override
            void handle(Message message) {
                stack.add("handled")
            }
        }
        def resolver = new HandlerResolver() {
            @Override
            Handler<Message> resolveForMessage(Class<Message> messageClass) {
                return handler
            }
        }
        def middleware = new MessageHandlerMiddleware(resolver)
        def bus = new BusWithMiddlewareCapabilities(List.of(middleware))

        when:
        bus.dispatch(new Message() {})

        then:
        stack.size() == 1
        stack.get(0) == "handled"
    }
}
