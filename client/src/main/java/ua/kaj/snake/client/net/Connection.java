package ua.kaj.snake.client.net;

import org.slf4j.Logger;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import static org.slf4j.LoggerFactory.getLogger;

public class Connection {
    private static final Logger log = getLogger(Connection.class);

    private static final String uri = "ws://localhost:7656/events";
    private static final Connection instance = new Connection();

    private static StandardWebSocketClient client = new StandardWebSocketClient();
    private static WebSocketSession session = null;

    private Connection() {
    }

    public static Connection getInstance() {
        init();
        return instance;
    }

    private static void init() {
        try {
            EventHandler socket = new EventHandler();
            ListenableFuture<WebSocketSession> fut = client.doHandshake(socket, uri);
            session = fut.get();
        } catch (Throwable t) {
            log.error(t.getMessage());
        }
    }

    public WebSocketSession getSession() {
        return session;
    }
}
