package ua.kaj.snake.server.network;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ua.kaj.snake.server.entity.Player;
import ua.kaj.snake.server.service.GameSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Scope("singleton")
public class ConnectionPool {
    private static final Logger log = getLogger(ConnectionPool.class);

    private final ConcurrentHashMap<WebSocketSession, String> pool;

    @Autowired
    public ConnectionPool() {
        pool = new ConcurrentHashMap<>();
    }

    public void send(@NotNull WebSocketSession session, @NotNull String msg) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void broadcast(@NotNull String msg, GameSession game) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        game.getPlayers().forEach(player -> new PlayerThread(countDownLatch, player, msg).start());
    }

    public void add(@NotNull WebSocketSession session, @NotNull String player) {
        if (pool.putIfAbsent(session, player) == null) {
            log.info("{} joined", player);
        }
    }

    public void remove(WebSocketSession session) {
        pool.remove(session);
    }


    private class PlayerThread extends Thread {
        private final CountDownLatch countDownLatch;
        private final Player player;
        private String msg;

        public PlayerThread(CountDownLatch countDownLatch, Player player, String msg) {
            this.countDownLatch = countDownLatch;
            this.player = player;
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                runUnsafe();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        private void runUnsafe() throws InterruptedException {
            countDownLatch.countDown();
            countDownLatch.await();
            send(player.getSession(), msg);
        }
    }
}
