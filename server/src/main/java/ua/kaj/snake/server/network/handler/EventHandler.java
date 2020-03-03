package ua.kaj.snake.server.network.handler;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ua.kaj.snake.server.dto.Message;
import ua.kaj.snake.server.dto.Replica;
import ua.kaj.snake.server.entity.Player;
import ua.kaj.snake.server.entity.Snake;
import ua.kaj.snake.server.enums.Direction;
import ua.kaj.snake.server.enums.Topic;
import ua.kaj.snake.server.exceptions.PlayerNotFoundException;
import ua.kaj.snake.server.exceptions.WebSocketSessionException;
import ua.kaj.snake.server.network.ConnectionPool;
import ua.kaj.snake.server.service.GameService;
import ua.kaj.snake.server.service.GameSession;

import javax.validation.constraints.NotNull;

import static org.slf4j.LoggerFactory.getLogger;
import static ua.kaj.snake.server.enums.Direction.*;
import static ua.kaj.snake.server.util.JsonHelper.fromJson;
import static ua.kaj.snake.server.util.JsonHelper.toJson;

@Component
public class EventHandler extends TextWebSocketHandler implements WebSocketHandler {
    private static final Logger log = getLogger(EventHandler.class);

    private final GameService gameService;
    private final ConnectionPool connectionPool;

    @Autowired
    public EventHandler(@NotNull ConnectionPool connectionPool, @NotNull GameService gameService) {
        this.gameService = gameService;
        this.connectionPool = connectionPool;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("Socket Connected: " + session);
        Message message = new Message(Topic.HELLO, "");
        connectionPool.add(session, message.toString());
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws PlayerNotFoundException {
        Message msg = fromJson(message.getPayload(), Message.class);

        if (msg.getTopic().equals(Topic.START)) {
            try {
                gameService.connect(session);
            } catch (WebSocketSessionException e) {
                log.error(e.getMessage());
            }
            if (gameService.canStartGame(session)) {
                GameSession game = gameService.getActiveGameBySession(session);
                game.setConnectionPool(connectionPool);
                gameService.start(game);
            }
        } else {
            GameSession game = gameService.getActiveGameBySession(session);
            Snake snake = game.getPlayerBySession(session).getSnake();
            if (game.isInGame()) {
                switch (msg.getTopic()) {
                    case MOVE_LEFT:
                        if(!snake.getDirection().equals(RIGHT)) {
                            snake.setDirection(LEFT);
                        }
                        break;
                    case MOVE_RIGHT:
                        if(!snake.getDirection().equals(LEFT)) {
                            snake.setDirection(RIGHT);
                        }
                        break;
                    case MOVE_UP:
                        if(!snake.getDirection().equals(DOWN)) {
                            snake.setDirection(Direction.UP);
                        }
                        break;
                    case MOVE_DOWN:
                        if(!snake.getDirection().equals(UP)) {
                            snake.setDirection(Direction.DOWN);
                        }
                        break;
                    default:
                        log.warn(msg.getTopic()+" it is not correct message's topic");
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        log.info("Socket Closed:[" + closeStatus.getCode() + "] " + closeStatus.getReason());

        if (gameService.canStartGame(session)) {
            GameSession game = gameService.getActiveGameBySession(session);

            if (game.isInGame()) {
                game.setInGame(false);
            }

            gameService.disconnected(game, session);

            Replica replica = new Replica();
            replica.setInGame(false);
            Message msg = new Message(Topic.STOP, toJson(replica));
            connectionPool.send(game.getPlayers().stream()
                    .map(Player::getSession)
                    .findFirst()
                    .orElseThrow(WebSocketSessionException::new), toJson(msg));
        }

        if (gameService.getPendingGameBySession(session) != null) {
            gameService.cleanPendingPool(session);
        }

        connectionPool.remove(session);

        super.afterConnectionClosed(session, closeStatus);
    }
}

