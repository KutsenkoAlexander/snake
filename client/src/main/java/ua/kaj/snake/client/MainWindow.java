package ua.kaj.snake.client;


import ua.kaj.snake.client.core.GameField;

import javax.swing.*;
import java.awt.*;

public class
MainWindow extends JFrame {
    private static final String SNAKE = "/snake.png";

    private MainWindow(){
        setTitle("Snake");
        setIconImage(new ImageIcon(MainWindow.class.getResource(SNAKE)).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(480, 695);
        setResizable(false);
        setOnCenter(this);
        add(GameField.getInstance());
        setVisible(true);
    }

    private static void setOnCenter(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
