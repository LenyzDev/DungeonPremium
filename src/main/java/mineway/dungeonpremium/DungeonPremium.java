package mineway.dungeonpremium;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.plugin.ParticleNativePlugin;
import mineway.dungeonpremium.commands.PremiumCommand;
import mineway.dungeonpremium.configuration.ConfigManager;
import mineway.dungeonpremium.database.Database;
import mineway.dungeonpremium.database.UserDAO;
import mineway.dungeonpremium.database.redis.RedisPubSub;
import mineway.dungeonpremium.hooks.PlaceholderHook;
import mineway.dungeonpremium.listeners.PlayerListener;
import mineway.dungeonpremium.storages.PremiumStorage;
import mineway.dungeonpremium.storages.UserStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class DungeonPremium extends JavaPlugin {

    private Logger logger;
    private ConfigManager configManager;
    private RedisPubSub redisPubSub;
    private Database database;
    private UserDAO userDAO;
    private UserStorage userStorage;
    private PremiumStorage premiumStorage;
    private ParticleNativeAPI particleNativeAPI;

    @Override
    public void onEnable() {
        logger = getLogger();
        configManager = new ConfigManager(this);

        database = new Database(this, configManager, logger);
        userDAO = new UserDAO(database, logger);

        userStorage = new UserStorage(userDAO);
        premiumStorage = new PremiumStorage(this, configManager.getConfig("config"));
        redisPubSub = new RedisPubSub(this, configManager.getConfig("config"), premiumStorage, getLogger());

        Plugin particlePlugin = this.getServer().getPluginManager().getPlugin("ParticleNativeAPI");
        if (particlePlugin != null) {
            if (!ParticleNativePlugin.isValid()) getLogger().warning("ParticleNativeAPI is not enabled!");
            particleNativeAPI = ParticleNativePlugin.getAPI();
        }else{
            getLogger().warning("ParticleNativeAPI is not installed!");
        }

        for(Player player : Bukkit.getOnlinePlayers()){
            userStorage.loadUser(player);
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this, userStorage), this);
        this.getCommand("premium").setExecutor(new PremiumCommand(userStorage, premiumStorage, redisPubSub));

        (new PlaceholderHook(userStorage)).register();
    }

    @Override
    public void onDisable() {
        database.disconnect();
        redisPubSub.close();
    }

    public ParticleNativeAPI getParticleNativeAPI() {
        return particleNativeAPI;
    }

}
