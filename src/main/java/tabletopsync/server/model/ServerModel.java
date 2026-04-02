package tabletopsync.server.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import tabletopsync.chatbox.model.MessageObject;
//import server dependents
import tabletopsync.server.controller.ServerController;
import tabletopsync.server.view.ServerView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ServerModel {

    // TODO: figure out a better server flow for controller -> model and model -> controller

    //create mapper object
    public static final ObjectMapper mapper = new ObjectMapper();

    // create file path objects
    private static final File SYSTEM_FILE = new File("logs/chatlogs/systemLogs.json");
    private static final File MESSAGE_FILE = new File("logs/chatlogs/messages.json");

    // ~~~~~~~~~~~~~~~~~~~~~~~~
    // SAVE METHODS
    // ~~~~~~~~~~~~~~~~~~~~~~~~

    public static void saveChatMessage(MessageObject messageObject) {
        saveToFile(MESSAGE_FILE, messageObject);
    }

    public static void saveSystemMessage(MessageObject messageObject) {
        saveToFile(SYSTEM_FILE, messageObject);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~
    // LOAD METHODS
    // ~~~~~~~~~~~~~~~~~~~~~~~~

    public static List<MessageObject> loadChatMessages() { return loadFromFile(MESSAGE_FILE); }

    public static List<MessageObject> loadSystemMessages() { return loadFromFile(SYSTEM_FILE); }



    // ========================
    // HELPER METHODS
    // ========================
    /**
     * General save method where you enter your file location and the messages you wanna save to it
     * @param file specific filepath
     * @param messageObject message you want to save
     */
    public static void saveToFile(File file, MessageObject messageObject) {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            String json = mapper.writeValueAsString(messageObject);
            fos.write((json + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads data from file.
     * Used in SERVER MODEL to load from file persistence folders.
     * @param file
     * @return file data
     */
    public static List<MessageObject> loadFromFile(File file) {
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
