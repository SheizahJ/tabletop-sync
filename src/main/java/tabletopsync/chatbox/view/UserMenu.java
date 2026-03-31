package tabletopsync.chatbox.view;

import tabletopsync.chatbox.controller.ChatService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserMenu extends JPanel {

    private DefaultListModel<String> userListModel;

    /**
     * TODO:
     *  User List Panel Feature:
     *  - Toggle visibility of a sidebar panel showing connected users
     *  - Button updates label based on panel state (Show / Hide Users)
     *  - Panel subscribes to user model updates and refreshes automatically
     *  - User list remains consistent regardless of visibility state
     */
    public UserMenu() {
        setLayout(new BorderLayout());

        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);

        add(new JScrollPane(userList), BorderLayout.CENTER);
        refreshList();
    }

    public void refreshList() {
        userListModel.clear();

        // directly loop through the concurrent hashmap, then put it into a list
        for (String username : ChatService.clients.keySet()) {
            userListModel.addElement(username);
        }
    }

    public void updateUsers(List<String> users) {
        userListModel.clear();
        for (String user : users) {
            userListModel.addElement(user);
        }
    }

}
