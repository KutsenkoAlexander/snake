package ua.kaj.snake.client.net;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ua.kaj.snake.client.core.GameField;
import ua.kaj.snake.client.dto.Message;
import ua.kaj.snake.client.dto.Replica;

import javax.validation.constraints.NotNull;

import static org.slf4j.LoggerFactory.getLogger;
import static ua.kaj.snake.client.enums.Topic.*;
import static ua.kaj.snake.client.util.JsonHelper.fromJson;
import static ua.kaj.snake.client.util.JsonHelper.getJsonNode;

public class EventHandler extends TextWebSocketHandler implements WebSocketHandler {
    private static final Logger log = getLogger(EventHandler.class);

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("Socket Connected: " + session);
    }


    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {
        Message msg = fromJson(message.getPayload(), Message.class);
        JsonNode jsonNode = getJsonNode(msg.getData());
        Replica replica = fromJson(jsonNode.asText(), Replica.class);

        if (msg.getTopic().equals(MOVE) && replica.isInGame()) {
            GameField.getInstance().actionGame(replica);
        } else if (msg.getTopic().equals(START) && !replica.isInGame()) {
            GameField.getInstance().initGame(replica);
        } else if (msg.getTopic().equals(STOP) && !replica.isInGame()) {
            GameField.getInstance().stopGame();
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        log.info("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        GameField.getInstance().stopGame();
        super.afterConnectionClosed(session, closeStatus);
    }
}
