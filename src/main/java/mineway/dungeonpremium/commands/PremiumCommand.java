package mineway.dungeonpremium.commands;

import mineway.dungeonpremium.database.redis.RedisPubSub;
import mineway.dungeonpremium.inventories.PremiumInventory;
import mineway.dungeonpremium.storages.PremiumStorage;
import mineway.dungeonpremium.storages.UserStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PremiumCommand implements CommandExecutor {

    private UserStorage userStorage;
    private PremiumStorage premiumStorage;
    private RedisPubSub redisPubSub;

    public PremiumCommand(UserStorage userStorage, PremiumStorage premiumStorage, RedisPubSub redisPubSub) {
        this.userStorage = userStorage;
        this.premiumStorage = premiumStorage;
        this.redisPubSub = redisPubSub;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0){
            if (!(sender instanceof Player)) {
                sender.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §cVocê não pode executar esse comando no console.");
                return false;
            }
            Player player = (Player) sender;
            PremiumInventory.getInventory(userStorage, premiumStorage).open(player);
            return true;
        }else{
            if(!sender.hasPermission("dungeonpremium.admin")){
                sender.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §cVocê não tem permissão para executar esse comando.");
                return false;
            }
            switch (args[0].toLowerCase()){
                case "alert":{
                    if(args.length != 3){
                        sender.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §cUtilize /premium alert <player> <months>.");
                        return false;
                    }

                    Player target = sender.getServer().getPlayer(args[1]);
                    if(target == null){
                        sender.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §cJogador não encontrado.");
                        return false;
                    }

                    int months;
                    try {
                        months = Integer.parseInt(args[2]);
                    }catch (NumberFormatException e){
                        sender.sendMessage("§x§1§4§F§F§7§A§lPREMIUM §cUtilize um número válido.");
                        return false;
                    }

                    redisPubSub.sendMessage(target.getName(), String.valueOf(months));
                    return true;
                }
            }
            return false;
        }
    }
}
