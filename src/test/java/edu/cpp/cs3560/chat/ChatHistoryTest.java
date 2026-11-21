package edu.cpp.cs3560.chat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatHistoryTest {

    @Test
    void testAddMessage() {
        ChatHistory history = new ChatHistory();

        User umar = new User("Umar");
        User bob = new User("Bob");

        Message message = new Message(
                umar, List.of(bob), "Hello!"
        );

        history.addMessage(message);

        assertEquals(1, history.getMessages().size());
        assertSame(message, history.getMessages().get(0));
    }

    @Test
    void testEmptyHistory() {
        ChatHistory history = new ChatHistory();

        assertTrue(history.getMessages().isEmpty());
    }

    @Test
    void testGetLastMessageSent() {
        ChatHistory history = new ChatHistory();

        User umar = new User("Umar");
        User bob = new User("Bob");

        Message first = new Message(
                umar, List.of(bob), "First message"
        );

        Message second = new Message(
                bob, List.of(umar), "Reply"
        );

        Message third = new Message(
                umar, List.of(bob), "Last message"
        );

        history.addMessage(first);
        history.addMessage(second);
        history.addMessage(third);

        assertSame(third, history.getLastMessageSent(umar));
        assertSame(second, history.getLastMessageSent(bob));
    }

    @Test
    void testNoSentMessage() {
        ChatHistory history = new ChatHistory();

        User umar = new User("Umar");

        assertNull(history.getLastMessageSent(umar));
    }

    @Test
    void testHistoryCannotBeModifiedExternally() {
        ChatHistory history = new ChatHistory();

        assertThrows(UnsupportedOperationException.class,
                () -> history.getMessages().clear());
    }

    @Test
    void testUserHistoryIncludesSentAndReceivedMessages() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        umar.sendMessage(List.of(bob), "Hello Bob!");
        bob.sendMessage(List.of(umar), "Hi Umar!");

        assertEquals(2, umar.getChatHistory().size());
        assertEquals(2, bob.getChatHistory().size());

        assertEquals(1, umar.getReceivedMessages().size());
        assertEquals(1, bob.getReceivedMessages().size());

        assertEquals("Hello Bob!",
                umar.getLastSentMessage().getContent());

        assertEquals("Hi Umar!",
                bob.getLastSentMessage().getContent());
    }
}
