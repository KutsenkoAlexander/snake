package ua.kaj.snake.server.network;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ua.kaj.snake.server.service.GameSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

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
        game.getPlayers().parallelStream().forEach(p -> send(p.getSession(), msg));
    }

    public void add(@NotNull WebSocketSession session, @NotNull String player) {
        if (pool.putIfAbsent(session, player) == null) {
            log.info("{} joined", player);
        }
    }

    public void remove(WebSocketSession session) {
        pool.remove(session);
    }
}
