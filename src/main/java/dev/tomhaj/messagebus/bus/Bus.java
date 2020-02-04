package dev.tomhaj.messagebus.bus;

import dev.tomhaj.messagebus.handler.Message;

public interface Bus {
    void dispatch(Message message);
}
