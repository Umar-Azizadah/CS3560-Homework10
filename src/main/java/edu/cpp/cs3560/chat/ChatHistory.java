package edu.cpp.cs3560.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class ChatHistory implements IterableByUser {

    private final List<Message> messages;

    public ChatHistory() {
        messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return List.copyOf(messages);
    }

    public Message getLastMessageSent(User sender) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);

            if (message.getSender() == sender) {
                return message;
            }
        }

        return null;
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return new searchMessagesByUser(messages, userToSearchWith);
    }

}
