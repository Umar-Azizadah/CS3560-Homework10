package edu.cpp.cs3560.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory {

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
}
