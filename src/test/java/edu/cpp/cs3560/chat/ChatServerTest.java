package edu.cpp.cs3560.chat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatServerTest {

    @Test
    void testRegisterUser() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice", server);

        server.registerUser(alice);

        assertTrue(server.getRegisteredUsers().contains(alice));
        assertEquals(1, server.getRegisteredUsers().size());
    }

    @Test
    void testDuplicateRegistration() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice", server);

        server.registerUser(alice);
        server.registerUser(alice);

        assertEquals(1, server.getRegisteredUsers().size());
    }

    @Test
    void testUnregisterUser() {
        ChatServer server = new ChatServer();
        User alice = new User("Alice", server);

        server.registerUser(alice);
        server.unregisterUser(alice);

        assertFalse(server.getRegisteredUsers().contains(alice));
    }

    @Test
    void testSendMessageToOneUser() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        server.registerUser(alice);
        server.registerUser(bob);

        alice.sendMessage(List.of(bob), "Hello Bob!");

        assertEquals(1, bob.getReceivedMessages().size());
        assertEquals("Hello Bob!",
                bob.getReceivedMessages().get(0).getContent());
        assertSame(alice,
                bob.getReceivedMessages().get(0).getSender());
    }

    @Test
    void testSendMessageToMultipleUsers() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

        alice.sendMessage(List.of(bob, charlie), "Hello everyone!");

        assertEquals(1, bob.getReceivedMessages().size());
        assertEquals(1, charlie.getReceivedMessages().size());

        assertEquals("Hello everyone!",
                charlie.getReceivedMessages().get(0).getContent());
    }

    @Test
    void testUnregisteredRecipientDoesNotReceiveMessage() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        server.registerUser(alice);

        alice.sendMessage(List.of(bob), "Hello!");

        assertTrue(bob.getReceivedMessages().isEmpty());
    }

    @Test
    void testUnregisteredSenderCannotSend() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        server.registerUser(bob);

        alice.sendMessage(List.of(bob), "Hello!");

        assertTrue(bob.getReceivedMessages().isEmpty());
    }
}
