package jp.houlab.mochidsuki.battleroyalecore3;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.bukkit.Bukkit.*;

public final class Main extends JavaPlugin {

    public static Plugin plugin;
    public static FileConfiguration config;

    private ProtocolManager protocolManager;
    @Override
    public void onEnable() {
        getLogger().info("Battle Royale 2 Pluginが目を覚ました！");
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getCommand("brc").setExecutor(new CommandListener()); //brgame

        plugin = this;

        //Config
        saveDefaultConfig();
        config = getConfig();

        //World
        V.world = getWorld(config.getString("World"));

        //BossBar
        BossBar.createBossBar();

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getLogger().info("Battle Royale 2 Pluginがおねんねした");
        // Plugin shutdown logic
    }
    public static Plugin getPlugin() {
        return plugin;
    }




}

class B{
    static int[] target = new int[4];

    static double[] center = new double[3];

}