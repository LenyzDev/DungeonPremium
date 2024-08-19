package mineway.dungeonpremium.storages;

import mineway.dungeonpremium.DungeonPremium;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.List;

public class PremiumStorage {

    private DungeonPremium plugin;
    private final List<String> rewardCommands;

    public PremiumStorage(DungeonPremium plugin, FileConfiguration config){
        this.plugin = plugin;
        this.rewardCommands = config.getStringList("reward-commands");
    }

    public List<String> getRewardCommands(){
        return rewardCommands;
    }

    public void alertPremium(String nick, int months){
        Player player = Bukkit.getPlayer(nick);
        for(Player online : Bukkit.getOnlinePlayers()){
            if(player == null) {
                sendToEveryone(online, nick, months);
                return;
            }
            if(online.getUniqueId().equals(player.getUniqueId())) sendToPremium(online, months);
            else sendToEveryone(online, nick, months);
        }
    }

    private void sendToEveryone(Player target, String player, int months){
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        target.sendTitle(
                "§x§1§4§F§F§7§A§lPREMIUM",
                "§fO jogador §x§1§4§F§F§7§A" + player + "§f se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                0, 40, 10
        );
        target.sendMessage("",
                "§x§1§4§F§F§7§A§lNOVO JOGADOR PREMIUM!",
                "§fO jogador §x§1§4§F§F§7§A" + player + "§f se tornou §x§1§4§F§F§7§A§lPREMIUM§f!",
                "§fTendo §x§1§4§F§F§7§ABeneficios§f e §x§1§4§F§F§7§ACash Diario§f por §x§1§4§F§F§7§A" + months + " meses§f.",
                ""
        );
        new BukkitRunnable() {
            @Override
            public void run() {
                target.sendTitle(
                        "§f§lPREMIUM",
                        "§fO jogador §x§1§4§F§F§7§A" + player + "§f se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }.runTaskLaterAsynchronously(plugin, 10);
        new BukkitRunnable() {
            @Override
            public void run() {
                target.sendTitle(
                        "§x§1§4§F§F§7§A§lPREMIUM",
                        "§fO jogador §x§1§4§F§F§7§A" + player + "§f se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }.runTaskLaterAsynchronously(plugin, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                target.sendTitle(
                        "§f§lPREMIUM",
                        "§fO jogador §x§1§4§F§F§7§A" + player + "§f se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }.runTaskLaterAsynchronously(plugin, 30);
        new BukkitRunnable() {
            @Override
            public void run() {
                target.sendTitle(
                        "§x§1§4§F§F§7§A§lPREMIUM",
                        "§fO jogador §x§1§4§F§F§7§A" + player + "§f se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }.runTaskLaterAsynchronously(plugin, 40);
    }

    private void sendToPremium(Player player, int months){
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.sendTitle(
                "§x§1§4§F§F§7§A§lPREMIUM",
                "§fParabéns!!! Você se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                0, 40, 10
        );
        player.sendMessage("",
                "§x§1§4§F§F§7§A§lAGORA VOCE E PREMIUM!",
                "§fParabéns!!! Você se tornou §x§1§4§F§F§7§A§lPREMIUM§f!",
                "§fTendo §x§1§4§F§F§7§ABeneficios§f e §x§1§4§F§F§7§ACash Diario§f por §x§1§4§F§F§7§A" + months + " meses§f.",
                ""
        );
        plugin.getParticleNativeAPI().LIST_1_13.HAPPY_VILLAGER.packet(true, player.getLocation().add(0, 1, 0), 1D, 1D, 1D, 100).sendTo(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(
                        "§f§lPREMIUM",
                        "§fParabéns!!! Você se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                plugin.getParticleNativeAPI().LIST_1_13.HAPPY_VILLAGER.packet(true, player.getLocation().add(0, 1, 0), 1D, 1D, 1D, 100).sendTo(player);
            }
        }.runTaskLaterAsynchronously(plugin, 10);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(
                        "§x§1§4§F§F§7§A§lPREMIUM",
                        "§fParabéns!!! Você se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                plugin.getParticleNativeAPI().LIST_1_13.HAPPY_VILLAGER.packet(true, player.getLocation().add(0, 1, 0), 1D, 1D, 1D, 100).sendTo(player);
            }
        }.runTaskLaterAsynchronously(plugin, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(
                        "§f§lPREMIUM",
                        "§fParabéns!!! Você se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                plugin.getParticleNativeAPI().LIST_1_13.HAPPY_VILLAGER.packet(true, player.getLocation().add(0, 1, 0), 1D, 1D, 1D, 100).sendTo(player);
            }
        }.runTaskLaterAsynchronously(plugin, 30);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(
                        "§x§1§4§F§F§7§A§lPREMIUM",
                        "§fParabéns!!! Você se tornou §x§1§4§F§F§7§APremium§f por §x§1§4§F§F§7§A" + months + " meses§f!",
                        0, 40, 10
                );
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                plugin.getParticleNativeAPI().LIST_1_13.HAPPY_VILLAGER.packet(true, player.getLocation().add(0, 1, 0), 1D, 1D, 1D, 100).sendTo(player);
            }
        }.runTaskLaterAsynchronously(plugin, 40);
    }
}
