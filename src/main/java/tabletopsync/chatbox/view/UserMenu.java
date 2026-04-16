package tabletopsync.chatbox.view;

import tabletopsync.chatbox.model.UserMenuModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A sidebar panel displaying the list of currently connected users.
 * Renders the data held in UserMenuModel via a scrollable JList.
 */
public class UserMenu extends JPanel {

    private final UserMenuModel model;

    /**
     * Constructs the user menu panel backed by the given model.
     *
     * @param model the data model providing the list of connected usernames
     */
    public UserMenu(UserMenuModel model) {
        this.model = model;
        setLayout(new BorderLayout());
        JList<String> userList = new JList<>(model.getListModel());
        add(new JScrollPane(userList), BorderLayout.CENTER);
    }

    /**
     * Updates the displayed user list with the provided usernames
     *
     * @param users the current list of connected usernames
     */
    public void updateUsers(List<String> users) {
        model.setUsers(users);
    }
}