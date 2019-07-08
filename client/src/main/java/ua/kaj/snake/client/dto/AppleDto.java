package ua.kaj.snake.client.dto;

public class AppleDto {
    private int x;
    private int y;

    public AppleDto() {
    }

    public AppleDto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "AppleDto{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
