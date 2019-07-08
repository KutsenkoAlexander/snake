package ua.kaj.snake.client.enums;

public enum DotSize {
    DOT_SIZE(16),
    ALL_DOTS(1200);

    private final int size;

    DotSize(int size) { this.size = size; }

    public int getValue() { return size; }
}