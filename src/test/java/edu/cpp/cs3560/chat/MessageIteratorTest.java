package edu.cpp.cs3560.chat;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MessageIteratorTest {

    @Test
    void testIteratorFindsSentMessages() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        umar.sendMessage(List.of(bob), "Hello Bob!");

        Iterator<Message> iterator = umar.iterator(bob);

        assertTrue(iterator.hasNext());
        assertEquals("Hello Bob!", iterator.next().getContent());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorFindsReceivedMessages() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        bob.sendMessage(List.of(umar), "Hello Umar!");

        Iterator<Message> iterator = umar.iterator(bob);

        assertTrue(iterator.hasNext());
        assertEquals("Hello Umar!", iterator.next().getContent());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorSkipsUnrelatedMessages() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(umar);
        server.registerUser(bob);
        server.registerUser(charlie);

        umar.sendMessage(List.of(bob), "Message to Bob");
        charlie.sendMessage(List.of(umar), "Message from Charlie");
        bob.sendMessage(List.of(umar), "Reply from Bob");
        umar.sendMessage(List.of(charlie), "Reply to Charlie");

        Iterator<Message> iterator = umar.iterator(bob);

        assertTrue(iterator.hasNext());
        assertEquals("Message to Bob", iterator.next().getContent());

        assertTrue(iterator.hasNext());
        assertEquals("Reply from Bob", iterator.next().getContent());

        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorFindsMultipleRecipientMessages() {
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

        Iterator<Message> iterator = umar.iterator(charlie);

        assertTrue(iterator.hasNext());
        assertEquals("Hello everyone!", iterator.next().getContent());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorWithNoMatchingMessages() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(umar);
        server.registerUser(bob);
        server.registerUser(charlie);

        umar.sendMessage(List.of(charlie), "Hello Charlie!");

        Iterator<Message> iterator = umar.iterator(bob);

        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorWithEmptyHistory() {
        User umar = new User("Umar");
        User bob = new User("Bob");

        Iterator<Message> iterator = umar.iterator(bob);

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorSkipsUndoneMessages() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        umar.sendMessage(List.of(bob), "First message");
        umar.sendMessage(List.of(bob), "Second message");

        assertTrue(umar.undoLastMessage());

        Iterator<Message> iterator = umar.iterator(bob);

        assertTrue(iterator.hasNext());
        assertEquals("First message", iterator.next().getContent());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testChatHistoryIteratorDirectly() {
        ChatHistory history = new ChatHistory();

        User umar = new User("Umar");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        history.addMessage(
                new Message(umar, List.of(bob), "For Bob")
        );

        history.addMessage(
                new Message(umar, List.of(charlie), "For Charlie")
        );

        Iterator<Message> iterator = history.iterator(bob);

        assertTrue(iterator.hasNext());
        assertEquals("For Bob", iterator.next().getContent());
        assertFalse(iterator.hasNext());
    }
}
