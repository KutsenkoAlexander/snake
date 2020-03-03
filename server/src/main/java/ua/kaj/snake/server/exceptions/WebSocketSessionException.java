package ua.kaj.snake.server.exceptions;

public class WebSocketSessionException extends Exception {
    public WebSocketSessionException() {
        super("WebSocketSession not found.");
    }
}
