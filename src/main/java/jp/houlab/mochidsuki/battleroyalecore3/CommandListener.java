package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.Bukkit;
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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.config;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("brc")) {
            if (args[0].equalsIgnoreCase("reload")) {

                return true;
            }
            if (args[0].equalsIgnoreCase("start")) {
                GameMainController.startGame();
            }
            /*
            if(args[0].equalsIgnoreCase("glow")){
                GlowAPI.setGlowing((Player)sender, GlowAPI.Color.WHITE , Bukkit.getOnlinePlayers());
                return true;
            }

             */


        }
        return false;
    }
}
