package edu.cpp.cs3560.chat;

import java.util.*;

public class ChatServer {

    private final List<User> users;
    private final Map<User, Set<User>> blockedUsers;

    public ChatServer() {
        users = new ArrayList<>();
        blockedUsers = new HashMap<>();
    }

    public void registerUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            blockedUsers.put(user, new HashSet<>());
        }
    }

    public void unregisterUser(User user) {
        users.remove(user);
        blockedUsers.remove(user);

        for (Set<User> blocked : blockedUsers.values()) {
            blocked.remove(user);
        }
    }

    public void blockUser(User blocker, User userToBlock) {
        if (users.contains(blocker)) {
            blockedUsers.get(blocker).add(userToBlock);
        }
    }

    public void unblockUser(User blocker, User userToUnblock) {
        if (users.contains(blocker)) {
            blockedUsers.get(blocker).remove(userToUnblock);
        }
    }

    public boolean isBlocked(User recipient, User sender) {
        return blockedUsers
                .getOrDefault(recipient, Collections.emptySet())
                .contains(sender);
    }

    public void sendMessage(Message message) {
        if (!users.contains(message.getSender())) {
            return;
        }

        message.getSender().recordSentMessage(message);

        for (User recipient : message.getRecipients()) {
            if (users.contains(recipient)
                    && !isBlocked(recipient, message.getSender())) {
                recipient.receiveMessage(message);
            }
        }
    }

    public List<User> getRegisteredUsers() {
        return List.copyOf(users);
    }


    public boolean undoMessage(User sender, Message message) {
        if (!users.contains(sender)
                || message.getSender() != sender
                || !sender.getChatHistory().contains(message)) {
            return false;
        }

        for (User recipient : message.getRecipients()) {
            recipient.removeReceivedMessage(message);
        }

        sender.removeSentMessage(message);

        return true;
    }

}
