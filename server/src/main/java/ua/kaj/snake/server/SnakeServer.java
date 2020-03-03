package ua.kaj.snake.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ua.kaj.snake.server"})
public class SnakeServer {
	public static void main(String[] args) {
		SpringApplication.run(SnakeServer.class, args);
	}
}
