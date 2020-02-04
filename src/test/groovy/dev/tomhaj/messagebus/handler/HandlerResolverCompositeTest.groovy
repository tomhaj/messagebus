package dev.tomhaj.messagebus.handler

import spock.lang.Specification

class HandlerResolverCompositeTest extends Specification {
    def "message not handled"() {
        given:
        def stack = new ArrayList<String>()
        def resolver = new HandlerResolverComposite(new HandlingMessageReflectionResolver(), List.of())

        when:
        resolver.resolveForMessage(FooMessage.class).handle(new FooMessage())
        resolver.resolveForMessage(BarMessage.class).handle(new BarMessage())

        then:
        stack.isEmpty()
    }

    def "messages are handled"() {
        given:
        def stack = new ArrayList<String>()
        def resolver = new HandlerResolverComposite(
                new HandlingMessageReflectionResolver(),
                List.of(new FooHandler(stack), new FooHandlerSecond(stack), new BarHandler(stack))
        )

        when:
        resolver.resolveForMessage(FooMessage.class).handle(new FooMessage())
        resolver.resolveForMessage(BarMessage.class).handle(new BarMessage())

        then:
        stack.size() == 3
        stack.get(0) == "foo handled"
        stack.get(1) == "foo handled in second handler"
        stack.get(2) == "bar handled"
    }
}
