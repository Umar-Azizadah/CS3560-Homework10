package edu.cpp.cs3560.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private final List<User> users;

    public ChatServer() {
        users = new ArrayList<>();
    }

    public void registerUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public void unregisterUser(User user) {
        users.remove(user);
    }

    public void sendMessage(Message message) {
        if (!users.contains(message.getSender())) {
            return;
        }

        for (User recipient : message.getRecipients()) {
            if (users.contains(recipient)) {
                recipient.receiveMessage(message);
            }
        }
    }

    public List<User> getRegisteredUsers() {
        return List.copyOf(users);
    }
}
