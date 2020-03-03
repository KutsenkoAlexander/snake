package ua.kaj.snake.server.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import ua.kaj.snake.server.entity.Player;
import ua.kaj.snake.server.exceptions.WebSocketSessionException;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GameService {
    private static final Logger log = getLogger(GameService.class);

    private Map<WebSocketSession, GameSession> activeGamesPool = new ConcurrentHashMap<>();
    private Map<WebSocketSession, GameSession> pendingGamesPool = new ConcurrentHashMap<>();

    public void connect(@NotNull WebSocketSession session) throws WebSocketSessionException {
        if (pendingGamesPool.containsKey(session) || activeGamesPool.containsKey(session)) {
            return;
        }

        if (!pendingGamesPool.isEmpty()) {
            GameSession game = pendingGamesPool.values().stream().findFirst().get();

            WebSocketSession firstPlayer = game.getPlayers().stream().map(Player::getSession).findFirst().orElseThrow(WebSocketSessionException::new);
            activeGamesPool.put(firstPlayer, game);
            pendingGamesPool.remove(firstPlayer);

            game.addPlayer(new Player(session));

            activeGamesPool.put(session, game);
            pendingGamesPool.remove(game);
        } else {
            GameSession game = new GameSession();
            game.addPlayer(new Player(session));
            pendingGamesPool.put(session, game);
        }
    }

    public void start(@NotNull GameSession game) {
        if (!game.isInGame()) {
            new Thread(game, "SNAKE GAME THREAD").start();
        }
    }

    public void disconnected(@NotNull GameSession game, @NotNull WebSocketSession disconnectedPlayer) throws WebSocketSessionException {
        Set<Player> players = game.getPlayers();
        players.removeIf(player -> player.getSession().equals(disconnectedPlayer));
        WebSocketSession pendingPlayer = players.stream()
                .map(Player::getSession)
                .findFirst()
                .orElseThrow(WebSocketSessionException::new);
        pendingGamesPool.put(pendingPlayer, game);
        activeGamesPool.remove(disconnectedPlayer);
        activeGamesPool.remove(pendingPlayer);
    }

    public void cleanPendingPool(@NotNull WebSocketSession session) {
        pendingGamesPool.remove(session);
    }

    public boolean canStartGame(@NotNull WebSocketSession session) {
        return activeGamesPool.containsKey(session);
    }

    public GameSession getActiveGameBySession(@NotNull WebSocketSession session) {
        return activeGamesPool.get(session);
    }

    public GameSession getPendingGameBySession(@NotNull WebSocketSession session) {
        return pendingGamesPool.get(session);
    }
}
