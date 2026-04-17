package tabletopsync.server.model.commands;

import tabletopsync.server.controller.ClientHandler;
import tabletopsync.server.model.ServerModel;

public class CanvasServerCmds {
    private final ClientHandler clientHandler;

    public CanvasServerCmds(ClientHandler client) {
        this.clientHandler = client;
    }

    /**
     * Executes commands handled in the SERVER. (Client > Server communication)
     * @param message The command message
     */
    public void handleCommand(String message) {
        String[] parts = message.split(";");
        String command = parts[0];

        System.out.println(command);

        // COMMANDS --------------------------------------
        switch (command) {
            // -------------------------------
            // Initializes the canvas requested by the client
            // format: INIT_CANVAS
            // -------------------------------
            case "INIT_CANVAS" -> {
                System.out.println("Attempting to initialize " + clientHandler.username + "'s canvas...");
                System.out.println("Sprites in Server: " + ServerModel.sprites);
                if (ServerModel.sprites.isEmpty()) {
                    clientHandler.sendToClient("INIT_CANVAS");
                } else {
                    String spriteData = String.join(",", ServerModel.sprites.values());
                    clientHandler.sendToClient("INIT_CANVAS;" + spriteData);
                }
            }

            // -------------------------------
            // Sprite move update from client
            // format: SPRITE_MOVE:uuid:x:y
            // -------------------------------
            case "SPRITE_MOVE" -> {
                try {
                    String uuid = parts[1];

                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);

                    // update Server Model's sprite data
                    String[] spriteData = ServerModel.sprites.get(uuid).split(":");
                    String resourcePath = spriteData[1];
                    ServerModel.sprites.put(uuid, uuid + ":" + resourcePath + ":" + x + ":" + y);

                    System.out.println(ServerModel.sprites.get(uuid));

                    // broadcast sprite movement to all clients
                    clientHandler.broadcast("SPRITE_MOVE;" + uuid + ";" + x + ";" + y);
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Invalid sprite move format");
                }
            }

            // -------------------------------
            // Sprite add update from client
            // format: SPRITE_ADD:uuid:resourcePath:x:y
            // -------------------------------
            case "SPRITE_ADD" -> {
                try {
                    String uuid = parts[1];
                    String resourcePath = parts[2];
                    int x = Integer.parseInt(parts[3]);
                    int y = Integer.parseInt(parts[4]);

                    ServerModel.sprites.put(uuid, uuid + ":" + resourcePath + ":" + x + ":" + y);

                    // broadcast sprite creation to all clients
                    clientHandler.broadcast("SPRITE_ADD;" + uuid + ";" + resourcePath + ";" + x + ";" + y);
                } catch (Exception e) {
                    System.err.println("ERROR: Invalid sprite add format");
                }
            }
        }
    }
}
