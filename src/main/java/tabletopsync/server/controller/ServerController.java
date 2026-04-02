package tabletopsync.server.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.model.ServerModel;
import tabletopsync.server.view.ServerView;
import tabletopsync.server.controller.ServerController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class ServerController {

    // Chat controller persistence read
    public List<MessageObject> getChatHistory() {
        return ServerModel.loadChatMessages();
    }
    public List<MessageObject> getSystemHistory() {
        return ServerModel.loadSystemMessages();
    }
    // chat controller persistence write
    public static void sendChatMessage(MessageObject message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendSystemMessage(MessageObject message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
