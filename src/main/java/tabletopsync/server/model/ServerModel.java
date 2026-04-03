package tabletopsync.server.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

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
}