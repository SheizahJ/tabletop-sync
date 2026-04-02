package tabletopsync.server.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

/**
 * Holds config const. and filepaths + other related
 */
public class ServerModel {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final File LOG_ROOT = new File("logs");
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final File SYSTEM_FILE = new File("logs/chatlogs/systemLogs.json");
    public static final File MESSAGE_FILE = new File("logs/chatlogs/messages.json");
}