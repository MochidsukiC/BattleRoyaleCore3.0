package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.config;
import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;
import static jp.houlab.mochidsuki.battleroyalecore3.V.getGameround;
import static jp.houlab.mochidsuki.border.Main.world;

/**
 * コマンドリスナー
 * @author Mochidsuki
 */
public class CommandListener implements CommandExecutor {
    static public HashMap<CommandSender, BukkitTask> HealthBarShowerTaskRegister = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("brc")) {
            if (args[0].equalsIgnoreCase("reload")) {

                return true;
            }
            if (args[0].equalsIgnoreCase("start")) {
                GameMainController.startGame();
            }
            if(args[0].equalsIgnoreCase("teams")) {
                GameMainController.watchTeamCount(0);
            }
            if(args[0].equalsIgnoreCase("debug")) {
                jp.houlab.mochidsuki.carePackage.SpawnPackage.randomSpawn(2, new Location(world, GameMainController.RingXs[getGameround()], 300, GameMainController.RingZs[getGameround()]), 2);
            }
            /*
            if(args[0].equalsIgnoreCase("glow")){
                GlowAPI.setGlowing((Player)sender, GlowAPI.Color.WHITE , Bukkit.getOnlinePlayers());
                return true;
            }

             */
        }
        if(command.getName().equalsIgnoreCase("watchHealth")) {
            if(args.length == 0) {
                HealthBarShower.sendMessage((Player) sender);
            } else if (args[0].equalsIgnoreCase("stop")) {
                HealthBarShowerTaskRegister.get(sender).cancel();
                HealthBarShowerTaskRegister.remove(sender);
            }else{
                HealthBarShowerTaskRegister.put(sender, new HealthBarShower((Player) sender).runTaskTimer(plugin,0L, Long.parseLong(args[0])));
            }
        }
        return true;
    }
}
