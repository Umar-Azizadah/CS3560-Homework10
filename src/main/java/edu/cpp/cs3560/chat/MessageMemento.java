package edu.cpp.cs3560.chat;

import java.time.LocalDateTime;

public class MessageMemento {

    private final Message message;
    private final String content;
    private final LocalDateTime timestamp;

    public MessageMemento(Message message) {
        this.message = message;
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
    }

    public Message getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
