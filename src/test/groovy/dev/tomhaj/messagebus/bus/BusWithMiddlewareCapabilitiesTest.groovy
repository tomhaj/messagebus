package dev.tomhaj.messagebus.bus

import dev.tomhaj.messagebus.handler.Message
import dev.tomhaj.messagebus.middleware.Middleware
import spock.lang.Specification

import java.util.function.Consumer

class BusWithMiddlewareCapabilitiesTest extends Specification {
    def "not accept duplicated middlerwares" () {
        given:
        def stack = new ArrayList();
        def middleware = new Middleware() {
            @Override
            void dispatch(Message message, Consumer<Message> next) {
                stack.add("call")
                next.accept(message)
            }
        }
        def bus = new BusWithMiddlewareCapabilities(List.of(middleware, middleware))

        when:
        bus.dispatch(new Message() {})

        then:
        stack.size() == 1
    }
}
