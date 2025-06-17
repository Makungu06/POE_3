package javaapplication40;


import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageApp {

    // Arrays to hold different categories of messages
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();

    static class Message {
        String recipient;
        String message;
        String messageID;
        String messageHash;
        String flag;

        public Message(String recipient, String message, String messageID, String messageHash, String flag) {
            this.recipient = recipient;
            this.message = message;
            this.messageID = messageID;
            this.messageHash = messageHash;
            this.flag = flag;
        }
    }

    // Utility to generate SHA-256 hash of a message string
    public static String sha256(String base) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Add message manually - you input all details including ID and hash
    public static void addMessage(String recipient, String message, String messageID, String messageHash, String flag) {
        Message msg = new Message(recipient, message, messageID, messageHash, flag);
        switch (flag) {
            case "Sent" -> {
                sentMessages.add(msg);
                messageIDs.add(messageID);
                messageHashes.add(messageHash);
            }
            case "Stored" -> storedMessages.add(msg);
            case "Disregard" -> disregardedMessages.add(msg);
            default -> System.out.println("Unknown flag, message not added.");
        }
    }

    // a) Display sender and recipient of all sent messages
    public static List<String> displaySentMessages() {
        List<String> result = new ArrayList<>();
        for (Message msg : sentMessages) {
            result.add("Recipient: " + msg.recipient + ", Message: " + msg.message);
        }
        return result;
    }

    // b) Display the longest sent message
    public static String getLongestSentMessage() {
        Message longest = null;
        for (Message msg : sentMessages) {
            if (longest == null || msg.message.length() > longest.message.length()) {
                longest = msg;
            }
        }
        return longest != null ? longest.message : null;
    }

    // c) Search message by ID
    public static String searchByMessageID(String id) {
        for (Message msg : sentMessages) {
            if (msg.messageID.equals(id)) {
                return "Recipient: " + msg.recipient + ", Message: " + msg.message;
            }
        }
        return "Message ID not found.";
    }

    // d) Search all messages sent or stored to a recipient
    public static List<String> searchByRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(recipient)) {
                results.add(msg.message);
            }
        }
        for (Message msg : storedMessages) {
            if (msg.recipient.equals(recipient)) {
                results.add(msg.message);
            }
        }
        return results;
    }

    // e) Delete a message by message hash from stored messages
    public static String deleteByMessageHash(String hash) {
        Iterator<Message> iterator = storedMessages.iterator();
        while (iterator.hasNext()) {
            Message msg = iterator.next();
            if (msg.messageHash.equals(hash)) {
                iterator.remove();
                return "Message \"" + msg.message + "\" successfully deleted.";
            }
        }
        return "Message not found.";
    }

    // f) Display full report of sent messages
    public static List<Map<String, String>> displayReport() {
        List<Map<String, String>> report = new ArrayList<>();
        for (Message msg : sentMessages) {
            Map<String, String> entry = new HashMap<>();
            entry.put("Message ID", msg.messageID);
            entry.put("Message Hash", msg.messageHash);
            entry.put("Recipient", msg.recipient);
            entry.put("Message", msg.message);
            report.add(entry);
        }
        return report;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter number of messages to input:");
            int n = Integer.parseInt(scanner.nextLine());
            
            for (int i = 0; i < n; i++) {
                System.out.println("Enter recipient:");
                String recipient = scanner.nextLine();
                
                System.out.println("Enter message:");
                String message = scanner.nextLine();
                
                System.out.println("Enter message ID:");
                String messageID = scanner.nextLine();
                
                System.out.println("Enter flag (Sent/Stored/Disregard):");
                String flag = scanner.nextLine();
                
                // You can calculate hash or input it manually - here we calculate:
                String messageHash = sha256(message);
                
                addMessage(recipient, message, messageID, messageHash, flag);
                System.out.println("Message added.\n");
            }
            
            // Demo operations:
            System.out.println("All Sent Messages:");
            for (String msg : displaySentMessages()) {
                System.out.println(msg);
            }
            
            System.out.println("\nLongest Sent Message:");
            System.out.println(getLongestSentMessage());
            
            System.out.println("\nSearch by Message ID:");
            System.out.println("Enter message ID to search:");
            String searchID = scanner.nextLine();
            System.out.println(searchByMessageID(searchID));
            
            System.out.println("\nSearch all messages to recipient:");
            String recipientSearch = scanner.nextLine();
            List<String> msgsToRecipient = searchByRecipient(recipientSearch);
            for (String m : msgsToRecipient) {
                System.out.println(m);
            }
            
            System.out.println("\nDelete a message by hash:");
            System.out.println("Enter message hash:");
            String hashToDelete = scanner.nextLine();
            System.out.println(deleteByMessageHash(hashToDelete));
            
            System.out.println("\nSent Messages Report:");
            List<Map<String, String>> report = displayReport();
            for (Map<String, String> entry : report) {
                System.out.println(entry);
            }
        }
    }
}