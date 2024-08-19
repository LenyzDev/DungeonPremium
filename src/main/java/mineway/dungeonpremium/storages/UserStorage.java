package mineway.dungeonpremium.storages;

import mineway.dungeonpremium.database.UserDAO;
import mineway.dungeonpremium.objects.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class UserStorage {

    private HashMap<UUID, User> users;
    private UserDAO userDAO;

    public UserStorage(UserDAO userDAO){
        this.userDAO = userDAO;
        users = new HashMap<>();
    }

    public User getUser(Player player) {
        if (!users.containsKey(player.getUniqueId())){
            User user = new User();
            users.put(player.getUniqueId(), user);
        }
        return users.get(player.getUniqueId());
    }

    public void loadUser(Player player){
        users.put(player.getUniqueId(), userDAO.get(player.getUniqueId()));
    }

    public void saveUser(Player player, User user){
        userDAO.asyncInsert(player.getUniqueId(), user);
    }

    public void removeUser(Player player){
        users.remove(player.getUniqueId());
    }

    public boolean hasUser(Player player){
        return users.containsKey(player.getUniqueId());
    }
    

}
