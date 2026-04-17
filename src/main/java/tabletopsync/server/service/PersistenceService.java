package tabletopsync.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.model.ServerModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles data persistence
 * Thread-safe
 */
public class PersistenceService {

    private static final ObjectMapper mapper = ServerModel.mapper;

    /**
     * Saves a chat message to JSON file
     *
     * @param messageObject the message to save
     */
    public static synchronized void saveChatMessage(MessageObject messageObject) {
        saveToFile(ServerModel.MESSAGE_FILE, messageObject);
    }

    /**
     * Saves a system message to JSON file
     *
     * @param messageObject the system message to save
     */
    public static synchronized void saveSystemMessage(MessageObject messageObject) {
        saveToFile(ServerModel.SYSTEM_FILE, messageObject);
    }

    // GitHub doesn't like empty files or directories, so I make them on runtime -rami.
    private static void ensureFileExistence(File file) {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * File write method
     *
     * @param file          target file
     * @param messageObject message object to serialize
     */
    private static void saveToFile(File file, MessageObject messageObject) {
        ensureFileExistence(file);
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            String json = mapper.writeValueAsString(messageObject);
            fos.write((json + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads chat messages from saved file
     *
     * @return list of chat messages
     */
    public static synchronized List<MessageObject> loadChatMessages() {
        return loadFromFile(ServerModel.MESSAGE_FILE);
    }

    /**
     * Loads system messages from saved file
     *
     * @return list of system messages
     */
    public static synchronized List<MessageObject> loadSystemMessages() {
        return loadFromFile(ServerModel.SYSTEM_FILE);
    }

    /**
     * File read
     *
     * @param file target file
     * @return list of messages read
     */
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