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

    public static synchronized void saveChatMessage(MessageObject messageObject) {
        saveToFile(ServerModel.MESSAGE_FILE, messageObject);
    }

    public static synchronized void saveSystemMessage(MessageObject messageObject) {
        saveToFile(ServerModel.SYSTEM_FILE, messageObject);
    }

    private static void saveToFile(File file, MessageObject messageObject) {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            String json = mapper.writeValueAsString(messageObject);
            fos.write((json + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized List<MessageObject> loadChatMessages() {
        return loadFromFile(ServerModel.MESSAGE_FILE);
    }

    public static synchronized List<MessageObject> loadSystemMessages() {
        return loadFromFile(ServerModel.SYSTEM_FILE);
    }

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