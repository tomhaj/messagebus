package dev.tomhaj.messagebus.handler

import spock.lang.Specification

class HandlerResolverSingleTest extends Specification {
    def "message is handled"() {
        given:
        def stack = new ArrayList<String>();
        def message = new FooMessage();
        def resolver = new HandlerResolverSingle(new HandlingMessageReflectionResolver(), List.of(new FooHandler(stack)));

        when:
        resolver.resolveForMessage(message.getClass()).handle(message);

        then:
        stack.size() == 1
        stack.get(0) == "foo handled"
    }

    def "message is not handled"() {
        given:
        def stack = new ArrayList<String>();
        def message = new FooMessage();
        def resolver = new HandlerResolverSingle(new HandlingMessageReflectionResolver(), List.of());

        when:
        resolver.resolveForMessage(message.getClass()).handle(message);

        then:
        stack.isEmpty()
    }
}
