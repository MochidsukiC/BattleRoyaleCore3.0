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

import static org.bukkit.Bukkit.getMap;
import static org.bukkit.Bukkit.getScoreboardManager;

public final class Main extends JavaPlugin {

    private static Plugin plugin;
    private static FileConfiguration config;

    private ProtocolManager protocolManager;
    @Override
    public void onEnable() {
        getLogger().info("Battle Royale 2 Pluginが目を覚ました！");
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getCommand("gameround").setExecutor(new CommandListener()); //gameround
        getCommand("debugerb").setExecutor(new CommandListener()); //debugerb
        getCommand("brc").setExecutor(new CommandListener()); //brgame
        getCommand("mapgenerator").setExecutor(new CommandListener()); //mapgenerator
        getCommand("openelytra").setExecutor(new CommandListener()); //openelytra

        v.rtime = 0;
        v.stime =0;
        plugin = this;

        //Config
        saveDefaultConfig();
        config = getConfig();

        //PlaceholderAPI
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SomeExpansion(this).register();
        }

        //ProtocolLib
        protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.SPYGLASS){
                    v.useSniper.remove(event.getPlayer());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> v.useSniper.remove(event.getPlayer()),5L);
                }
            }
        });





        //BossBar
        b.bossBar = this.getServer().createBossBar("開始までお待ちください。。。", BarColor.WHITE, BarStyle.SEGMENTED_10);
        b.bossBar.setVisible(true);



        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();
        getMap(v.bigmapdata).addRenderer(new OriginMapRender());
        mapMetaB.setMapId(v.bigmapdata);
        getMap(v.bigmapdata).setTrackingPosition(true);

        mapItemB.setItemMeta(mapMetaB);

        try {
            for(int i = 0;i<=10;i++) {
                getScoreboardManager().getMainScoreboard().getTeam(i+"").getEntries().clear();
            }

        }catch (Exception ignored){}


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
class V {

    static double[] now = new double[4];
    static int gameround;
    static int mr;
    static int mcx;
    static int mcz;
    static long[] roundstime = new long[7];
    static int[] roundrtime = new int[7];
    static long stime;
    static int rtime;
    static int bigmapdata;
    static Color[] colors = new Color[16384];
    static double exVector;
    static double eyVector;
    static double esLimit;
    static int mapScale;
    static boolean inv;
    static HashMap<Player, Location> pin = new HashMap<>();
    static HashMap<Player, Location> pinRed = new HashMap<>();
    static HashMap<Player, ItemStack[]> knockDownBU = new HashMap<>();
    static List<Player> useSniper = new ArrayList<Player>();
}