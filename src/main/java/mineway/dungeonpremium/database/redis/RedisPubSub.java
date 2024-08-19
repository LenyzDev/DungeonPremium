package mineway.dungeonpremium.database.redis;

import mineway.dungeonpremium.storages.PremiumStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Logger;

public class RedisPubSub {

    private Plugin plugin;
    private JedisPool jedisPool;
    private PremiumStorage premiumStorage;

    public RedisPubSub(Plugin plugin, FileConfiguration config, PremiumStorage premiumStorage, Logger logger) {
        logger.warning("Connecting to Redis...");
        this.plugin = plugin;
        this.premiumStorage = premiumStorage;
        this.jedisPool = new JedisPool(
                buildPoolConfig(100),
                config.getString("redis.host"),
                config.getInt("redis.port"),
                500,
                config.getString("redis.password"));
        logger.warning("Redis Connected.");
        startMessageListener(logger);
    }

    public void sendMessage(String playerName, String messageId) {
        try (Jedis jedis = getJedis()) {
            jedis.publish("premiumMessage", playerName+";"+messageId);
        }
    }

    private void startMessageListener(Logger logger) {
        logger.warning("Starting Redis Listener...");
        new Thread(() -> {
            JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String messageId) {
                    if (!plugin.isEnabled()) return;
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        String[] values = messageId.split(";");
                        if(values.length != 2) {
                            return;
                        }
                        String nick = values[0];
                        int months;
                        try {
                            months = Integer.parseInt(values[1]);
                        } catch (NumberFormatException e) {
                            return;
                        }
                        System.out.println("("+channel+") Received message: ("+nick+") " + months);
                        premiumStorage.alertPremium(nick, months);
                    });
                }
            };
            getJedis().subscribe(jedisPubSub, "premiumMessage");
        }).start();
        logger.warning("Redis Listener Started.");
    }

    protected JedisPoolConfig buildPoolConfig(int maxConnections) {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxConnections);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public void close() {
        this.jedisPool.close();
    }
}
