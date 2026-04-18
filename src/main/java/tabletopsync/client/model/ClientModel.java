package tabletopsync.client.model;

import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.controller.ChatController;
import tabletopsync.chatbox.model.ChatModel;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.client.model.commands.CanvasClientCmds;
import tabletopsync.client.model.commands.ChatClientCmds;
import tabletopsync.login.LoginModel;
import tabletopsync.login.LoginView;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientModel {
    public CanvasModel canvasModel;
    public CanvasView canvasView;
    public CanvasController canvasController;

    public ChatModel chatModel;
    public ChatView chatView;
    public ChatController chatController;
    public Color colorSelection;

    private final PrintWriter out;

    public ClientModel(Socket socket, PrintWriter out, String username, Color colorSelection) throws IOException {
        // Initialize Canvas
        this.canvasModel = new CanvasModel();
        canvasModel.setUsername(username);

        this.canvasController = getCanvasController(canvasModel);

        this.canvasView = new CanvasView();
        canvasView.add(canvasController);

        // Initialize Chat
        this.chatModel = new ChatModel();
        this.chatView = new ChatView(username, colorSelection);
        this.chatController = new ChatController(chatView, socket, out);
        this.colorSelection = colorSelection;

        this.out = out;
    }

    private CanvasController getCanvasController(CanvasModel canvasModel) throws IOException {
        CanvasController canvasController = new CanvasController(canvasModel, colorSelection);
        canvasController.setCanvasListener(new CanvasController.CanvasUpdateListener() {
            @Override
            public void onSpriteAdded(String uuid, String resourcePath, int x, int y) {
                String command = "SPRITE_ADD;" + uuid + ";" + resourcePath + ";" + x + ";" + y;
                out.println(command);
            }

            @Override
            public void onSpriteMoved(String uuid, int x, int y) {
                String command = "SPRITE_MOVE;" + uuid + ";" + x + ";" + y;
                out.println(command);
            }

            @Override
            public void onSpriteRemoved(String uuid) {
                String command = "SPRITE_DELETE;" + uuid;
                out.println(command);
            }

            @Override
            public void onMapUpdate(String resourcePath, int width, int height) {
                String command = "UPDATE_MAP;" + resourcePath + ";" + width + ";" + height;
                out.println(command);
            }
        });
        return canvasController;
    }
}
