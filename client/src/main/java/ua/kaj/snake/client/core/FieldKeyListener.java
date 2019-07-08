package ua.kaj.snake.client.core;

import org.slf4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ua.kaj.snake.client.dto.Message;
import ua.kaj.snake.client.net.Connection;

import javax.validation.constraints.NotNull;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.kaj.snake.client.enums.Topic.*;
import static ua.kaj.snake.client.util.JsonHelper.toJson;

public class FieldKeyListener extends KeyAdapter {
    private static final Logger log = getLogger(FieldKeyListener.class);

    private Connection connection;
    private WebSocketSession session;

    @Override
    public void keyPressed(@NotNull KeyEvent e) {
        super.keyPressed(e);

        if (connection == null && session == null) {
            this.connection = GameField.getInstance().getConnection();
            this.session = connection.getSession();
        }

        String message = toJson(new Message(MOVE, toJson("")));

        int key = e.getKeyCode();

        if (GameField.getInstance().isInGame()) {
            if (key == VK_LEFT) {
                message = toJson(new Message(MOVE_LEFT, toJson("")));
            }
            if (key == VK_RIGHT) {
                message = toJson(new Message(MOVE_RIGHT, toJson("")));
            }
            if ((key == VK_UP)) {
                message = toJson(new Message(MOVE_UP, toJson("")));
            }
            if (key == VK_DOWN) {
                message = toJson(new Message(MOVE_DOWN, toJson("")));
            }
        } else {
            if (key == VK_ENTER && !GameField.getInstance().isInGame()) {
                message = toJson(new Message(START, toJson("")));
            }
        }

        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e1) {
            log.error(e1.getMessage());
        }
    }
}