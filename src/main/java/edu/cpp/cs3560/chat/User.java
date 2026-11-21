package edu.cpp.cs3560.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class User implements IterableByUser {

    private final String username;
    private final ChatServer server;
    private final List<Message> receivedMessages;
    private final ChatHistory chatHistory;
    private final Deque<MessageMemento> sentMessageSnapshots;

    public User(String username) {
        this(username, null);
    }

    public User(String username, ChatServer server) {
        this.username = username;
        this.server = server;
        this.receivedMessages = new ArrayList<>();
        this.chatHistory = new ChatHistory();
        this.sentMessageSnapshots = new ArrayDeque<>();
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
        sentMessageSnapshots.push(new MessageMemento(message));
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

    public boolean undoLastMessage() {
        if (server == null || sentMessageSnapshots.isEmpty()) {
            return false;
        }

        MessageMemento memento = sentMessageSnapshots.peek();

        if (server.undoMessage(this, memento.getMessage())) {
            sentMessageSnapshots.pop();
            return true;
        }

        return false;
    }

    void removeReceivedMessage(Message message) {
        receivedMessages.remove(message);
        chatHistory.removeMessage(message);
    }

    void removeSentMessage(Message message) {
        chatHistory.removeMessage(message);
    }

    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return chatHistory.iterator(userToSearchWith);
    }


}
