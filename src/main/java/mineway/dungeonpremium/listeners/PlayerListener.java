package mineway.dungeonpremium.listeners;

import mineway.dungeonpremium.DungeonPremium;
import mineway.dungeonpremium.storages.UserStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private DungeonPremium plugin;
    private UserStorage userStorage;

    public PlayerListener(DungeonPremium plugin, UserStorage userStorage){
        this.plugin = plugin;
        this.userStorage = userStorage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                userStorage.loadUser(player);
            }
        }.runTaskLater(plugin, 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(userStorage.hasUser(player)){
            userStorage.removeUser(player);
        }
    }
}
