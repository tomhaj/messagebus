package dev.tomhaj.messagebus.handler;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BarHandler implements Handler<BarMessage> {
    private final List<String> stack;

    @Override
    public void handle(BarMessage message) {
        stack.add("bar handled");
    }
}
