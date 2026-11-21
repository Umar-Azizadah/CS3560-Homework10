
package edu.cpp.cs3560.chat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatServerBlockingTest {

    @Test
    void testBlockUser() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        bob.blockUser(umar);

        assertTrue(server.isBlocked(bob, umar));
    }

    @Test
    void testBlockedMessageNotDelivered() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        bob.blockUser(umar);
        umar.sendMessage(List.of(bob), "Hello!");

        assertTrue(bob.getReceivedMessages().isEmpty());
        assertTrue(bob.getChatHistory().isEmpty());
    }

    @Test
    void testBlockingOnlyAffectsSpecificRecipient() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(umar);
        server.registerUser(bob);
        server.registerUser(charlie);

        bob.blockUser(umar);

        umar.sendMessage(
                List.of(bob, charlie),
                "Hello everyone!"
        );

        assertTrue(bob.getReceivedMessages().isEmpty());

        assertEquals(1, charlie.getReceivedMessages().size());
        assertEquals("Hello everyone!",
                charlie.getReceivedMessages().get(0).getContent());
    }

    @Test
    void testUnblockUser() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        bob.blockUser(umar);
        bob.unblockUser(umar);

        assertFalse(server.isBlocked(bob, umar));

        umar.sendMessage(List.of(bob), "Hello again!");

        assertEquals(1, bob.getReceivedMessages().size());
    }

    @Test
    void testBlockingIsNotMutual() {
        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);

        server.registerUser(umar);
        server.registerUser(bob);

        bob.blockUser(umar);

        assertFalse(server.isBlocked(umar, bob));

        bob.sendMessage(List.of(umar), "Hi Umar!");

        assertEquals(1, umar.getReceivedMessages().size());
    }
}
