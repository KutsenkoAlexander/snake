package ua.kaj.snake.server.enums;

public enum DotSize {
    DOT_SIZE(16);

    private final int size;

    DotSize(int size) { this.size = size; }

    public int getValue() { return size; }
}

