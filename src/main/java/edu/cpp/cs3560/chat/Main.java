package edu.cpp.cs3560.chat;

import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ChatServer server = new ChatServer();

        User umar = new User("Umar", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(umar);
        server.registerUser(bob);
        server.registerUser(charlie);

        System.out.println("CHAT APPLICATION");

        // 1. Mediator: Send messages through the server
        System.out.println("\nMEDIATOR PATTERN");

        umar.sendMessage(List.of(bob), "Hello Bob!");
        bob.sendMessage(List.of(umar), "Hey Umar!");
        umar.sendMessage(
                List.of(bob, charlie),
                "Hello everyone!"
        );

        System.out.println("Umar sent messages to Bob and Charlie.");
        printHistory("Umar", umar);
        printHistory("Bob", bob);
        printHistory("Charlie", charlie);

        // 2. Mediator: Block a user
        System.out.println("\nBLOCKING");

        bob.blockUser(umar);
        umar.sendMessage(
                List.of(bob, charlie),
                "Can everyone see this?"
        );

        System.out.println("Bob blocked Umar.");
        System.out.println("Bob received: "
                + bob.getReceivedMessages().size()
                + " messages");

        System.out.println("Charlie received: "
                + charlie.getReceivedMessages().size()
                + " messages");

        // 3. Unblock the user
        System.out.println("\nUNBLOCKING");

        bob.unblockUser(umar);
        umar.sendMessage(
                List.of(bob),
                "You can see my messages again!"
        );

        printHistory("Bob", bob);

        // 4. Memento: Undo the last sent message
        System.out.println("\nMEMENTO PATTERN");

        System.out.println("Umar's last sent message: "
                + umar.getLastSentMessage().getContent());

        boolean undone = umar.undoLastMessage();

        System.out.println("Undo successful: " + undone);
        printHistory("Bob after undo", bob);

        // 5. Iterator: Search messages by a user
        System.out.println("\nITERATOR PATTERN");

        System.out.println("Umar's messages involving Bob:");

        Iterator<Message> iterator = umar.iterator(bob);

        while (iterator.hasNext()) {
            Message message = iterator.next();

            System.out.println(
                    message.getSender().getUsername()
                            + ": " + message.getContent()
            );
        }

        System.out.println("\nDEMO COMPLETE");
    }

    private static void printHistory(String label, User user) {
        System.out.println("\n" + label + "'s chat history:");

        for (Message message : user.getChatHistory()) {
            System.out.println(
                    message.getSender().getUsername()
                            + ": " + message.getContent()
            );
        }
    }
}
