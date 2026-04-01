package tabletopsync.chatbox.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceModel {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final File SYSTEM_FILE = new File("systemLogs.json");
    private static final File MESSAGE_FILE = new File("messages.json");

    // ========================
    // SAVE METHODS
    // ========================

    public static void saveChatMessage(MessageObject messageObject) {
        saveToFile(MESSAGE_FILE, messageObject);
    }

    public static void saveSystemMessage(MessageObject messageObject) {
        saveToFile(SYSTEM_FILE, messageObject);
    }

    /**
     * General save method where you enter your file location and the messages you wanna save to it
     * @param file specific filepath
     * @param messageObject message you want to save
     */
    private static void saveToFile(File file, MessageObject messageObject) {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            String json = mapper.writeValueAsString(messageObject);
            fos.write((json + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ========================
    // LOAD METHODS
    // ========================

    public static List<MessageObject> loadChatMessages() { return loadFromFile(MESSAGE_FILE); }

    public static List<MessageObject> loadSystemMessages() { return loadFromFile(SYSTEM_FILE); }

    private static List<MessageObject> loadFromFile(File file) {
        List<MessageObject> messages = new ArrayList<>();

        if (!file.exists()) return messages;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    MessageObject obj = mapper.readValue(line, MessageObject.class);
                    messages.add(obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }
}