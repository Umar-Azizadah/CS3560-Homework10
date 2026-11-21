
package edu.cpp.cs3560.chat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageMementoTest {

    @Test
    void testMementoStoresMessageSnapshot() {
        User umar = new User("Umar");
        User bob = new User("Bob");

        Message message = new Message(
                umar, List.of(bob), "Hello Bob!"
        );

        MessageMemento memento = new MessageMemento(message);

        assertEquals("Hello Bob!", memento.getContent());
        assertEquals(message.getTimestamp(), memento.getTimestamp());
        assertSame(message, memento.getMessage());
    }

    @Test
    void testUndoLastMessage() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        umar.sendMessage(List.of(bob), "Hello Bob!");

        assertEquals(1, bob.getReceivedMessages().size());

        assertTrue(umar.undoLastMessage());

        assertTrue(umar.getChatHistory().isEmpty());
        assertTrue(bob.getChatHistory().isEmpty());
        assertTrue(bob.getReceivedMessages().isEmpty());
    }

    @Test
    void testUndoOnlyMostRecentMessage() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        umar.sendMessage(List.of(bob), "First message");
        umar.sendMessage(List.of(bob), "Second message");

        assertTrue(umar.undoLastMessage());

        assertEquals(1, umar.getChatHistory().size());
        assertEquals(1, bob.getReceivedMessages().size());

        assertEquals("First message",
                bob.getReceivedMessages().get(0).getContent());
    }

    @Test
    void testUndoWithNoMessages() {
        ChatServer server = new ChatServer();
        User umar = new User("Umar", server);

        server.registerUser(umar);

        assertFalse(umar.undoLastMessage());
        assertTrue(umar.getChatHistory().isEmpty());
    }

    @Test
    void testUndoMessageWithMultipleRecipients() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(umar);
        server.registerUser(bob);
        server.registerUser(charlie);

        umar.sendMessage(
                List.of(bob, charlie),
                "Hello everyone!"
        );

        assertEquals(1, bob.getReceivedMessages().size());
        assertEquals(1, charlie.getReceivedMessages().size());

        assertTrue(umar.undoLastMessage());

        assertTrue(umar.getChatHistory().isEmpty());
        assertTrue(bob.getReceivedMessages().isEmpty());
        assertTrue(charlie.getReceivedMessages().isEmpty());

        assertTrue(bob.getChatHistory().isEmpty());
        assertTrue(charlie.getChatHistory().isEmpty());
    }

    @Test
    void testUndoDoesNotRemoveReceivedMessages() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        bob.sendMessage(List.of(umar), "Hi Umar!");
        umar.sendMessage(List.of(bob), "Hello Bob!");

        assertTrue(umar.undoLastMessage());

        assertEquals(1, umar.getChatHistory().size());
        assertEquals("Hi Umar!",
                umar.getChatHistory().get(0).getContent());

        assertEquals(1, umar.getReceivedMessages().size());
        assertEquals(1, bob.getChatHistory().size());
    }

    @Test
    void testUndoMultipleTimes() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        umar.sendMessage(List.of(bob), "First");
        umar.sendMessage(List.of(bob), "Second");
        umar.sendMessage(List.of(bob), "Third");

        assertTrue(umar.undoLastMessage());
        assertTrue(umar.undoLastMessage());

        assertEquals(1, bob.getReceivedMessages().size());
        assertEquals("First",
                bob.getReceivedMessages().get(0).getContent());

        assertTrue(umar.undoLastMessage());
        assertFalse(umar.undoLastMessage());

        assertTrue(umar.getChatHistory().isEmpty());
        assertTrue(bob.getChatHistory().isEmpty());
    }
}
