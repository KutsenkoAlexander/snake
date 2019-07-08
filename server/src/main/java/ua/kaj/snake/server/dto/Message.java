package ua.kaj.snake.server.dto;

import ua.kaj.snake.server.enums.Topic;
import javax.validation.constraints.NotNull;

public class Message {
    private Topic topic;
    private String data;

    public Message() {
    }

    public Message(@NotNull Topic topic, @NotNull String data) {
        this.topic = topic;
        this.data = data;
    }

    public Topic getTopic() {
        return topic;
    }

    public String getData() {
        return data;
    }

    public void setTopic(@NotNull Topic topic) {
        this.topic = topic;
    }

    public void setData(@NotNull String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "topic=" + topic +
                ", data='" + data + '\'' +
                '}';
    }
}
