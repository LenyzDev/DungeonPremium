package mineway.dungeonpremium.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import mineway.dungeonpremium.objects.User;
import mineway.dungeonpremium.storages.UserStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHook extends PlaceholderExpansion {

    @NotNull
    public String getIdentifier() {
        return "dungeonpremium";
    }

    @NotNull
    public String getAuthor() {
        return "lenyzin";
    }

    @NotNull
    public String getVersion() {
        return "0.1-ALPHA";
    }

    private UserStorage userStorage;

    public PlaceholderHook(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (!userStorage.hasUser(player)) return "";
        switch (params.toLowerCase()) {
            case "premium": {
                if(player.hasPermission("group.premium")){
                    return "§a⭐ §f";
                }
                return "";
            }
            case "has_premium": {
                if(player.hasPermission("group.premium")){
                    return "1";
                }
                return "0";
            }
        }
        return "";
    }
}
