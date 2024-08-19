package mineway.dungeonpremium.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import mineway.dungeonpremium.objects.User;
import mineway.dungeonpremium.storages.PremiumStorage;
import mineway.dungeonpremium.storages.UserStorage;
import mineway.dungeonpremium.utils.FormatTime;
import mineway.dungeonpremium.utils.ItemBuilder;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class PremiumInventory implements InventoryProvider {

    private UserStorage userStorage;
    private PremiumStorage premiumStorage;

    public PremiumInventory(UserStorage userStorage, PremiumStorage premiumStorage){
        this.userStorage = userStorage;
        this.premiumStorage = premiumStorage;
    }

    public static SmartInventory getInventory(UserStorage userStorage, PremiumStorage premiumStorage){
        return SmartInventory.builder()
                .id("premiumInventory")
                .provider(new PremiumInventory(userStorage, premiumStorage) {
                })
                .size(3, 9)
                .title("Premium")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        if(!userStorage.hasUser(player)) {
            player.closeInventory();
            return;
        }
        User user = userStorage.getUser(player);
        net.luckperms.api.model.user.User PermissionUser = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        if(PermissionUser == null) return;

        List<Node> nodeList = PermissionUser.getNodes(NodeType.INHERITANCE)
                .stream()
                .filter(node -> node.hasExpiry() && !node.hasExpired())
                .filter(node -> node.getKey().equalsIgnoreCase("group.premium"))
                .collect(Collectors.toList());

        FormatTime formatTime = new FormatTime();

        ItemStack premiumItem;
        ItemStack rewardItem;
        if(!nodeList.isEmpty()){
            Node group = nodeList.get(0);
            Instant expire = group.getExpiry();
            long duration = expire.toEpochMilli() - Instant.now().toEpochMilli();
            premiumItem = new ItemBuilder(Material.EMERALD)
                    .setDisplayName("§x§1§4§F§F§7§A§lSEU PREMIUM")
                    .setLore(
                            "",
                            "§fExpira em: §x§1§4§F§F§7§A"+formatTime.formatBigTime(duration),
                            ""
                    )
                    .build();
            if(ChronoUnit.DAYS.between(user.getLastReward(), LocalDateTime.now()) >= 1){
                rewardItem = new ItemBuilder(Material.CHEST_MINECART)
                        .setDisplayName("§x§1§4§F§F§7§A§lRECOLHER RECOMPENSA")
                        .setLore(
                                "",
                                "§fVocê pode reinvindicar",
                                "§fa Recompensa do Premium.",
                                "",
                                "§x§1§4§F§F§7§AClique para reinvindicar."
                        )
                        .build();
            }else{
                rewardItem = new ItemBuilder(Material.MINECART)
                        .setDisplayName("§c§lAGUARDANDO RECOMPENSA")
                        .setLore(
                                "",
                                "§fVocê já reivindicou a",
                                "§fRecompensa do Premium.",
                                "",
                                "§cEspere: "+formatTime.formatBigTime(
                                        user.getLastReward().plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                        - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                )
                        )
                        .build();
            }
        }else{
            premiumItem = new ItemBuilder(Material.EMERALD)
                    .setDisplayName("§x§1§4§F§F§7§A§lADQUIRA O PREMIUM")
                    .setLore(
                            "",
                            "§fAdquira o Premium no §x§1§4§F§F§7§A/shop§f e",
                            "§fobtenha benefícios exclusivos!",
                            ""
                    )
                    .build();
            rewardItem = new ItemBuilder(Material.MINECART)
                    .setDisplayName("§c§lRECOMPENSA BLOQUEADA")
                    .setLore(
                            "",
                            "§fAdquira o Premium no §x§1§4§F§F§7§A/shop",
                            "§fpara reinvindicar a Recompensa.",
                            ""
                    )
                    .build();
        }

        contents.set(1, 3, ClickableItem.empty(premiumItem));
        contents.set(1, 5, ClickableItem.of(rewardItem, e -> {
            if(nodeList.isEmpty()){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                player.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §fVocê não possui o Premium.");
                player.sendActionBar("§x§1§4§F§F§7§A§lPREMIUM §fVocê não possui o Premium.");
                return;
            }
            if(ChronoUnit.DAYS.between(user.getLastReward(), LocalDateTime.now()) >= 1){
                LocalDateTime now = LocalDateTime.now();
                user.setLastReward(now);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §fVocê reivindicou sua Recompensa do Premium.");
                player.sendActionBar("§x§1§4§F§F§7§A§lPREMIUM §fVocê reivindicou sua Recompensa do Premium.");
                userStorage.saveUser(player, user);
                for(String command : premiumStorage.getRewardCommands()){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
                }
                init(player, contents);
            }else{
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                player.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §fVocê já reivindicou Recompensa do Premium.");
                player.sendActionBar("§x§1§4§F§F§7§A§lPREMIUM §fVocê já reivindicou Recompensa do Premium.");
            }
        }));


    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
