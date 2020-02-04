package dev.tomhaj.messagebus.handler;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FooHandler implements Handler<FooMessage> {
    private final List<String> stack;

    @Override
    public void handle(FooMessage message) {
        stack.add("foo handled");
    }
}
