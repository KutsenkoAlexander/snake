package ua.kaj.snake.server.entity;

import org.slf4j.Logger;
import org.springframework.web.socket.WebSocketSession;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static java.lang.Integer.compare;
import static org.slf4j.LoggerFactory.getLogger;

public class Player implements Comparable<Player> {
    private static final Logger log = getLogger(Player.class);

    private String id;
    private WebSocketSession session;
    private Snake snake;

    public Player() {
        this.id = UUID.randomUUID().toString();
    }

    public Player(@NotNull WebSocketSession session) {
        this();
        this.session = session;
    }

    public Player(@NotNull WebSocketSession session, @NotNull Snake snake) {
        this(session);
        this.snake = snake;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(@NotNull Snake snake) {
        this.snake = snake;
    }

    @Override
    public int compareTo(Player o) {
        return compare(hashCode(), o.hashCode());
    }
}
