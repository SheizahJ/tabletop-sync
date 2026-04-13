package tabletopsync.server.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import tabletopsync.server.controller.ClientHandler;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerModel holds server configuration constants, file paths, and utility objects.
 * All persistence and networking are handled elsewhere (PersistenceService, ServerMainController).
 */
public class ServerModel {

    // ========================
    // HOST & PORT
    // ========================
    public static final String HOST = "localhost";
    public static final int PORT = 8080;

    // Thread-safe collection of all connected clients
    public static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    // ========================
    // LOG FILE ROOT
    // ========================
    public static final File LOG_ROOT = new File("logs");

    // ========================
    // JSON MAPPER
    // ========================
    public static final ObjectMapper mapper = new ObjectMapper();

    // ========================
    // FILE PATHS
    // ========================
    public static final File SYSTEM_FILE = new File("logs/chatlogs/systemLogs.json");
    public static final File MESSAGE_FILE = new File("logs/chatlogs/messages.json");
    public static final File NOTES_FOLDER = new File("logs/notes");


    // ========================
    // SPRITE
    // ========================
    public static HashMap<String, String> sprites = new HashMap<>();
}