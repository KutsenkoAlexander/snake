package ua.kaj.snake.client.dto;

import ua.kaj.snake.client.enums.Direction;
import java.util.List;

public class SnakeDto {
    private List<Integer> x;
    private List<Integer> y;
    private Direction direction;

    public SnakeDto() {
    }

    public SnakeDto(List<Integer> x, List<Integer> y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public List<Integer> getX() {
        return x;
    }

    public List<Integer> getY() {
        return y;
    }

    @Override
    public String toString() {
        return "SnakeDto{" +
                ", x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
