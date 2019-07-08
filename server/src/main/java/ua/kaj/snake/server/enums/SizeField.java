package ua.kaj.snake.server.enums;

public enum SizeField {
    SIZE_X(432),
    SIZE_Y(624);

    private final int size;

    SizeField(int size) { this.size = size; }

    public int getValue() { return size; }
}
