package tabletopsync.chatbox.controller;

import tabletopsync.chatbox.model.ChatModel;
import tabletopsync.chatbox.view.ChatView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles chat sending and receiving on client side
 */
public class ChatController {

    private final ChatModel model;
    private final ChatView view;
    private final Socket socket;
    private final PrintWriter out;

    public ChatController(ChatModel model, ChatView view, Socket socket, PrintWriter out) {
        this.model = model;
        this.view = view;
        this.socket = socket;
        this.out = out;

        model.addListener(view::appendMessage);
    }

    /**
     * Receives message from server
     */
    public void receive(String message) {
        model.addMessage(message);
    }

    public void sendMessage(String message) { out.println(message); }
}