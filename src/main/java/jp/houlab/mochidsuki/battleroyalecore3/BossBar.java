package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;

public class BossBar {
    static org.bukkit.boss.BossBar bossBar;

    static void createBossBar() {
        bossBar = plugin.getServer().createBossBar("開始までお待ちください。。。", BarColor.WHITE, BarStyle.SEGMENTED_10);
        bossBar.setVisible(true);
    }

    static void setBossBarTitle(String title) {
        bossBar.setTitle(title);
    }

    static void setBossBarColor(BarColor color) {
        bossBar.setColor(color);
    }

    static void setBossBarProgress(double progress) {
        bossBar.setProgress(progress);
    }

    static void showBossbar(){
        for(Player player : plugin.getServer().getOnlinePlayers()){
            bossBar.addPlayer(player);
        }
    }
}
