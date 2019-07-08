package ua.kaj.snake.server.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Replica {
    private AppleDto apple;
    private List<SnakeDto> snakeDtos;
    private boolean inGame;

    public Replica() {
        this.snakeDtos = new ArrayList<>();
    }

    public AppleDto getApple() {
        return apple;
    }

    public void setApple(@NotNull AppleDto apple) {
        this.apple = apple;
    }

    public List<SnakeDto> getSnakeDtos() {
        return snakeDtos;
    }

    public void setSnakeDtos(@NotNull List<SnakeDto> snakeDtos) {
        this.snakeDtos = snakeDtos;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
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
