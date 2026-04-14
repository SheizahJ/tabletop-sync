package tabletopsync.chatbox.model;

import javax.swing.DefaultListModel;
import java.util.List;

/**
 * Represents the data model for the connected users list.
 * Holds the list of usernames displayed in UserMenu.
 */
public class UserMenuModel {

    private final DefaultListModel<String> userListModel = new DefaultListModel<>();

    /**
     * Returns the list model used to back the JList in the view
     * @return DefaultListModel
     */
    public DefaultListModel<String> getListModel() {
        return userListModel;
    }

    /**
     * Replaces the current user list with a new list
     * @param users the updated list of connected usernames
     */
    public void setUsers(List<String> users) {
        userListModel.clear();
        for (String user : users) userListModel.addElement(user);
    }
}