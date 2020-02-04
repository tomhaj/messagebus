package dev.tomhaj.messagebus.middleware;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import dev.tomhaj.messagebus.handler.Message;

public final class HandledFullyBeforeNextMiddleware implements Middleware {
    private final Queue<Message> queue = new LinkedList<>();
    private boolean handling = false;

    @Override
    public void dispatch(Message message, Consumer<Message> next) {
        queue.offer(message);

        if (!handling) {
            handling = true;

            while (!queue.isEmpty()) {
                try {
                    next.accept(queue.poll());
                } catch (Exception e) {
                    handling = false;
                    throw e;
                }
            }

            handling = false;
        }
    }
}
