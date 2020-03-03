package ua.kaj.snake.server.exceptions;

public class SnakeNotFountException extends Exception {
    public SnakeNotFountException() {
        super("Snake was not found");
    }
}
