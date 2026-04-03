package tabletopsync.chatbox.view;

import tabletopsync.chatbox.model.UserMenuModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Displays the list of users in the chat
 */
public class UserMenu extends JPanel {

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> userList = new JList<>(listModel);
    private final UserMenuModel model;

    public UserMenu(UserMenuModel model) {
        this.model = model;
        setLayout(new BorderLayout());
        add(new JScrollPane(userList), BorderLayout.CENTER);
    }

    public void updateUsers(List<String> users) {
        listModel.clear();
        for (String user : users) listModel.addElement(user);
    }
}