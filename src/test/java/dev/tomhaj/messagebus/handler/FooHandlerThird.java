package dev.tomhaj.messagebus.handler;

import lombok.RequiredArgsConstructor;
import dev.tomhaj.messagebus.bus.Bus;

@RequiredArgsConstructor
public final class FooHandlerThird implements Handler<FooMessage> {
    private final Bus bus;

    @Override
    public void handle(FooMessage message) {
        bus.dispatch(new BarMessage());
    }
}
