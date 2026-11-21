package edu.cpp.cs3560.chat;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String username;
    private final ChatServer server;
    private final List<Message> receivedMessages;

    public User(String username) {
        this(username, null);
    }

    public User(String username, ChatServer server) {
        this.username = username;
        this.server = server;
        this.receivedMessages = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(List<User> recipients, String content) {
        if (server == null) {
            throw new IllegalStateException("User is not connected to a server");
        }

        Message message = new Message(this, recipients, content);
        server.sendMessage(message);
    }

    public void receiveMessage(Message message) {
        receivedMessages.add(message);
    }

    public List<Message> getReceivedMessages() {
        return List.copyOf(receivedMessages);
    }
}
