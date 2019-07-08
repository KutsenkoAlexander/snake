package ua.kaj.snake.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Replica {

    private AppleDto apple;
    private List<SnakeDto> snakeDtos;
    private boolean inGame;

    public Replica() {
        this.snakeDtos = new ArrayList<>();
    }

    @JsonCreator
    public Replica(@JsonProperty("apple") AppleDto apple,
                   @JsonProperty("snakes") List<SnakeDto> snakeDtos,
                   @JsonProperty("inGame") boolean inGame) {
        this.apple = apple;
        this.snakeDtos = snakeDtos;
        this.inGame = inGame;
    }

    public AppleDto getApple() {
        return apple;
    }

    public List<SnakeDto> getSnakeDtos() {
        return snakeDtos;
    }

    public boolean isInGame() {
        return inGame;
    }

    @Override
    public String toString() {
        return "Replica{" +
                "apple=" + apple +
                ", snakeDtos=" + snakeDtos +
                ", inGame=" + inGame +
                '}';
    }
}
