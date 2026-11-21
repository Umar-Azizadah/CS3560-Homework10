package edu.cpp.cs3560.chat;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MessageTest {

    @Test
    void testMessageProperties() {
        User sender = new User("Umar");
        User recipient = new User("Bob");

        Message message = new Message(sender, List.of(recipient), "Hello Bob!");

        assertSame(sender, message.getSender());
        assertEquals(List.of(recipient), message.getRecipients());
        assertEquals("Hello Bob!", message.getContent());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void testMultipleRecipients() {
        User umar = new User("Umar");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        Message message = new Message(umar, List.of(bob,charlie), "Hello everyone!");

        assertEquals(2, message.getRecipients().size());
        assertTrue(message.getRecipients().contains(bob));
        assertTrue(message.getRecipients().contains(charlie));
    }

    @Test
    void testTimestamp() {
        User umar = new User("Umar");
        User bob = new User("Bob");

        LocalDateTime before = LocalDateTime.now();

        Message message = new Message(umar, List.of(bob), "Test message");

        LocalDateTime after = LocalDateTime.now();

        assertFalse(message.getTimestamp().isBefore(before));
        assertFalse(message.getTimestamp().isAfter(after));
    }

    @Test
    void testRecipientsCannotBeModified() {
        User umar = new User("Umar");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        List<User> recipients = new ArrayList<>();
        recipients.add(bob);

        Message message = new Message(umar, recipients, "Hello!");

        recipients.add(charlie);

        assertEquals(1, message.getRecipients().size());

        assertThrows(UnsupportedOperationException.class, () -> message.getRecipients().add(charlie));

    }

}
