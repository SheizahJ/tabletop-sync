package tabletopsync.chatbox.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PersistenceModel {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File FILE = new File("chat_log.json");

    public static void saveMessage(MessageObject messageObject) {
        try {
            mapper.writeValue(new FileOutputStream(FILE, true), messageObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}