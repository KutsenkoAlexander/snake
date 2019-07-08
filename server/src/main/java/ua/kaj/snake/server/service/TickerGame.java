package ua.kaj.snake.server.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TickerGame {
    private static final Logger log = getLogger(TickerGame.class);
    private static final long FRAME_TIME = 250;

    private Set<Tickable> tickables = new ConcurrentSkipListSet<>();

    public void gameLoop() {
        long started = System.currentTimeMillis();
        act();
        long elapsed = System.currentTimeMillis() - started;

        if (elapsed < FRAME_TIME) {
            elapsed += 1;
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
        } else {
            log.warn("tick lag {} ms", elapsed - FRAME_TIME);
        }
    }

    public void registerTickable(@NotNull Tickable tickable) {
        tickables.add(tickable);
    }

    public void unregisterTickable(@NotNull Tickable tickable) {
        tickables.remove(tickable);
    }

    private void act() {
        tickables.forEach(Tickable::tick);
    }
}
