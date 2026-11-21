package edu.cpp.cs3560.chat;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String username;
    private final ChatServer server;
    private final List<Message> receivedMessages;
    private final ChatHistory chatHistory;

    public User(String username) {
        this(username, null);
    }

    public User(String username, ChatServer server) {
        this.username = username;
        this.server = server;
        this.receivedMessages = new ArrayList<>();
        this.chatHistory = new ChatHistory();
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(List<User> recipients, String content) {
        if (server == null) {
            throw new IllegalStateException(
                    "User is not connected to a server");
        }

        Message message = new Message(this, recipients, content);
        server.sendMessage(message);
    }

    void recordSentMessage(Message message) {
        chatHistory.addMessage(message);
    }

    public void receiveMessage(Message message) {
        receivedMessages.add(message);
        chatHistory.addMessage(message);
    }

    public List<Message> getReceivedMessages() {
        return List.copyOf(receivedMessages);
    }

    public List<Message> getChatHistory() {
        return chatHistory.getMessages();
    }

    public Message getLastSentMessage() {
        return chatHistory.getLastMessageSent(this);
    }

    public void blockUser(User userToBlock) {
        if (server == null) {
            throw new IllegalStateException(
                    "User is not connected to a server");
        }

        server.blockUser(this, userToBlock);
    }

    public void unblockUser(User userToUnblock) {
        if (server == null) {
            throw new IllegalStateException(
                    "User is not connected to a server");
        }

        server.unblockUser(this, userToUnblock);
    }
}
