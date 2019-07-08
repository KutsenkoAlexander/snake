package ua.kaj.snake.server.service;

import org.slf4j.Logger;
import org.springframework.web.socket.WebSocketSession;
import ua.kaj.snake.server.dto.AppleDto;
import ua.kaj.snake.server.dto.Message;
import ua.kaj.snake.server.dto.Replica;
import ua.kaj.snake.server.dto.SnakeDto;
import ua.kaj.snake.server.entity.Apple;
import ua.kaj.snake.server.entity.Player;
import ua.kaj.snake.server.entity.Snake;
import ua.kaj.snake.server.entity.builder.Director;
import ua.kaj.snake.server.entity.builder.SnakeDownBuilder;
import ua.kaj.snake.server.entity.builder.SnakeUpBuilder;
import ua.kaj.snake.server.network.ConnectionPool;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.lang.Integer.compare;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.kaj.snake.server.enums.Direction.DOWN;
import static ua.kaj.snake.server.enums.Direction.UP;
import static ua.kaj.snake.server.enums.Topic.*;
import static ua.kaj.snake.server.util.JsonHelper.toJson;

public class GameSession implements Tickable, Comparable<Tickable>, Runnable {
    private static final Logger log = getLogger(GameSession.class);

    private final String id = UUID.randomUUID().toString();
    private boolean inGame;

    private Set<Player> players;
    private Apple apple;

    private AppleDto appleDto;
    private Replica replica;

    private ConnectionPool connectionPool;
    private Message message;
    private TickerGame tickerGame;

    private boolean firstSnakeIsInit;

    public GameSession() {
        this.players = new ConcurrentSkipListSet<>();
    }

    private void init() {
        this.apple = Apple.getInstance();
        this.appleDto = new AppleDto(apple.getX(), apple.getY());
        this.replica = new Replica();

        List<Integer> xDtos = new ArrayList<>();
        List<Integer> yDtos = new ArrayList<>();

        Director director = new Director();
        players.forEach(p -> {
            if (firstSnakeIsInit) {
                director.setSnakeBuilder(new SnakeDownBuilder());
                Snake s = director.buildSnake();
                p.setSnake(s);

                xDtos.addAll(s.getX());
                yDtos.addAll(s.getY());
                replica.getSnakeDtos().add(new SnakeDto(xDtos, yDtos, DOWN));

                firstSnakeIsInit = false;
            } else {
                director.setSnakeBuilder(new SnakeUpBuilder());
                Snake s = director.buildSnake();
                p.setSnake(s);

                xDtos.addAll(s.getX());
                yDtos.addAll(s.getY());
                replica.getSnakeDtos().add(new SnakeDto(xDtos, yDtos, UP));

                firstSnakeIsInit = true;
            }
            xDtos.clear();
            yDtos.clear();
        });

        replica.setInGame(false);
        replica.setApple(appleDto);

        this.message = new Message();
        this.tickerGame = new TickerGame();
        tickerGame.registerTickable(this);
        firstSnakeIsInit = false;
    }

    public void addPlayer(@NotNull Player player) {
        players.add(player);
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void startGame() {
        if (!inGame) {
            init();
        }

        inGame = true;

        message.setTopic(START);
        message.setData(toJson(replica));
        connectionPool.broadcast(toJson(message), this);

        while (inGame) {
            tickerGame.gameLoop();
        }
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Player getPlayerBySession(@NotNull WebSocketSession session) {
        return players.stream().filter(p -> p.getSession().equals(session)).findFirst().get();
    }

    @Override
    public void tick() {
        List<SnakeDto> snakeDtos = new ArrayList<>();

        players.forEach(player -> {
            if (inGame) {
                Snake snake = player.getSnake();
                snake.collision(this);
                snake.eatApple(apple);
                snake.move();
                snakeDtos.add(new SnakeDto(snake.getX(), snake.getY(), snake.getDirection()));
            }
        });

        if (inGame) {
            appleDto.setX(apple.getX());
            appleDto.setY(apple.getY());
            replica.setApple(appleDto);
            replica.setSnakeDtos(snakeDtos);
            replica.setInGame(true);

            message.setTopic(MOVE);
            message.setData(toJson(replica));
        } else {
            replica.setInGame(false);
            replica.setApple(null);
            replica.getSnakeDtos().clear();

            message.setTopic(STOP);
            message.setData(toJson(replica));

            this.apple = null;
        }

        snakeDtos.clear();

        connectionPool.broadcast(toJson(message), this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSession game = (GameSession) o;
        return id == game.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(@NotNull Tickable o) {
        return compare(hashCode(), o.hashCode());
    }

    public void setConnectionPool(@NotNull ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        startGame();
    }
}
