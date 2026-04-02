package tabletopsync.server.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import tabletopsync.canvas.controller.CanvasController;
//import tabletopsync.canvas.model.CanvasModel;
//import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.controller.ChatController;
import tabletopsync.chatbox.controller.ChatService;
import tabletopsync.chatbox.controller.ClientViewHandler;
//import tabletopsync.chatbox.model.MessageObject;
//import server dependents
//import tabletopsync.chatbox.view.ChatArea;
//import tabletopsync.chatbox.view.ChatView;
//import tabletopsync.chatbox.view.UserMenu;
import tabletopsync.linker.TableTopGUI;
import tabletopsync.server.controller.ClientAppController;
import tabletopsync.server.controller.ServerController;
//import tabletopsync.server.view.ServerView;

//import javax.swing.filechooser.FileView;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerModel {

    // TODO: figure out a better server flow for controller -> model and model -> controller
    // ~~~~~~~~~~~~~~~~~~~~~~~~
    // CONTROLLER OBJECTS
    // ~~~~~~~~~~~~~~~~~~~~~~~~
    // SERVER:
    /// Controllers
    private ServerController serverController;
    private ClientAppController clientController;

    // ~~~~~~~~~~~~~~~~~~~~~~~~

    // LINKER (links modules together):
    private TableTopGUI  tableTopGUI;

    // ~~~~~~~~~~~~~~~~~~~~~~~~

    // CHATBOX:
    // Controllers
    private ChatController chatController;
    private ChatService chatService;
    private ClientViewHandler clientViewHandler;

    // ~~~~~~~~~~~~~~~~~~~~~~~~

    // CANVAS
    /// Controllers:
    private CanvasController canvasController;

    // ~~~~~~~~~~~~~~~~~~~~~~~~


    // ~~~~~~~~~~~~~~~~~~~~~~~~
    // OTHER OBJECTS:
    /// Host & Port
    public static final String HOST = "localhost";
    public static final int PORT = 8080;

    /// log file root
    private static final File LOG_ROOT = new File("logs");

    /// socket, reader, and writer
    public Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /// mapper
    public static final ObjectMapper mapper = new ObjectMapper();

    // create file path objects
    private static final File SYSTEM_FILE = new File("logs/chatlogs/systemLogs.json");
    private static final File MESSAGE_FILE = new File("logs/chatlogs/messages.json");
    // ~~~~~~~~~~~~~~~~~~~~~~~~


    // TODO: for chat service (and all other user state functions), update a runnning list of users as a public
    //  dictionary/keyset etc (data structure)



















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
