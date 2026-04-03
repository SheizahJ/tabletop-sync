package tabletopsync.chatbox.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains the list of active users
 */
public class UserMenuModel {

    private final List<String> users = new ArrayList<>();

    public void setUsers(List<String> userList) {
        users.clear();
        users.addAll(userList);
    }

    public List<String> getUsers() { return new ArrayList<>(users); }
}