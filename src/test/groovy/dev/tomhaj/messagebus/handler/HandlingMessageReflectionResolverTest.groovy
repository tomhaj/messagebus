package dev.tomhaj.messagebus.handler

import spock.lang.Specification

class HandlingMessageReflectionResolverTest extends Specification {
    def "should resolve handling message"() {
        given:
        def resolver = new HandlingMessageReflectionResolver();

        when:
        def handlerClass = resolver.resolveHandledMessage(FooHandler.class)

        then:
        handlerClass == FooMessage.class
    }
}
