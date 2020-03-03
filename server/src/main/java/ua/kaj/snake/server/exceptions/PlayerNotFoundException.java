package ua.kaj.snake.server.exceptions;

public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException() {
        super("Player was not found");
    }
}
