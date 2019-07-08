package ua.kaj.snake.client.core;

import org.slf4j.Logger;
import org.springframework.web.socket.TextMessage;
import ua.kaj.snake.client.dto.AppleDto;
import ua.kaj.snake.client.dto.Message;
import ua.kaj.snake.client.dto.Replica;
import ua.kaj.snake.client.dto.SnakeDto;
import ua.kaj.snake.client.enums.Topic;
import ua.kaj.snake.client.net.Connection;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Font.PLAIN;
import static javax.swing.BorderFactory.createEmptyBorder;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.kaj.snake.client.util.JsonHelper.toJson;

public class GameField extends JPanel implements ActionListener {
    private static final Logger log = getLogger(GameField.class);

    private static final String START = "START GAME";
    private static final String DOT1= "/dot1.png";
    private static final String DOT2= "/dot2.png";
    private static final String APPLE = "/apple.png";

    private boolean inGame;

    private Image apple;
    private Image dotS1;
    private Image dotS2;

    private AppleDto appleDto;
    private List<SnakeDto> snakeDtos;

    private JButton btnStart;

    private static GameField gameField = new GameField();
    private Connection connection;

    private GameField() {
        connection = Connection.getInstance();
        addKeyListener(new FieldKeyListener());
        setBackground(DARK_GRAY);
        createStartButton();
        setFocusable(true);
        createGame();
    }

    private void createStartButton() {
        btnStart = new JButton(START);
        btnStart.setBackground(LIGHT_GRAY);
        btnStart.setBorder(createEmptyBorder(0, 0, 0, 0));
        btnStart.setFocusPainted(false);
        btnStart.setFont(new Font("Arial", PLAIN, 18));
        btnStart.setPreferredSize(new Dimension(126, 26));
        btnStart.addActionListener(this);
        btnStart.setVisible(true);
        add(btnStart);
    }

    private void createGame() {
        apple = new ImageIcon(GameField.class.getResource(APPLE)).getImage();
        dotS1 = new ImageIcon(GameField.class.getResource(DOT1)).getImage();
        dotS2 = new ImageIcon(GameField.class.getResource(DOT2)).getImage();
        appleDto = new AppleDto();
        this.snakeDtos = new ArrayList<>();
    }

    public void initGame(@NotNull Replica replica) {
        if (replica != null) {
            appleDto = replica.getApple();
            if (snakeDtos.isEmpty()) {
                snakeDtos.addAll(replica.getSnakeDtos());
            }
            inGame = true;
            btnStart.setVisible(false);
        } else {
            log.info("Method initGame(Replica replica). Replica was NULL.");
        }
    }

    private void startGame() {
        String message = toJson(new Message(Topic.START, ""));
        try {
            //todo handle exception when client doesn't have connection
            connection.getSession().sendMessage(new TextMessage(message));
        } catch (IOException e1) {
            log.error(e1.getMessage());
        }
    }

    public void stopGame() {
        inGame = false;
        btnStart.setVisible(true);
        repaint();
    }

    public void actionGame(@NotNull Replica replica) {
        if (replica != null) {
            appleDto = replica.getApple();
            snakeDtos.set(0, replica.getSnakeDtos().get(0));
            snakeDtos.set(1, replica.getSnakeDtos().get(1));
            repaint();
        } else {
            log.info("Method actionGame(Replica replica). Replica was NULL.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (START.equals(e.getActionCommand()) && !inGame) {
            startGame();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleDto.getX(), appleDto.getY(), this);

            SnakeDto sd1 = snakeDtos.get(0);
            SnakeDto sd2 = snakeDtos.get(1);

            for (int i = 0; i < sd1.getX().size(); i++) {
                g.drawImage(dotS1, sd1.getX().get(i), sd1.getY().get(i), this);
            }

            for (int i = 0; i < sd2.getX().size(); i++) {
                g.drawImage(dotS2, sd2.getX().get(i), sd2.getY().get(i), this);
            }
        }
    }

    public static GameField getInstance() {
        return gameField;
    }

    boolean isInGame() {
        return inGame;
    }

    Connection getConnection() {
        return connection;
    }
}
