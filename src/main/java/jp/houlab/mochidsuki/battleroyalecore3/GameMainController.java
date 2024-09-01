package jp.houlab.mochidsuki.battleroyalecore3;

import jp.houlab.mochidsuki.border.BorderDamager;
import jp.houlab.mochidsuki.border.BorderInfo;
import jp.houlab.mochidsuki.border.BorderShrinkSystem;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.config;
import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;
import static jp.houlab.mochidsuki.battleroyalecore3.V.*;

public class GameMainController {

    static int time;
    static int LastRingX;
    static int LastRingZ;
    static int[] RingXs = new int[config.getInt("Ring.Times")+1];
    static int[] RingZs = new int[config.getInt("Ring.Times")+1];


    public static void startGame(){

        for(Player player : plugin.getServer().getOnlinePlayers()){

            Color c = Color.fromRGB(player.getScoreboard().getPlayerTeam(player).getColor().asBungee().getColor().getRed(),player.getScoreboard().getPlayerTeam(player).getColor().asBungee().getColor().getGreen(),player.getScoreboard().getPlayerTeam(player).getColor().asBungee().getColor().getBlue());
            ItemStack i = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
            meta.setColor(c);
            i.setItemMeta(meta);
            player.getInventory().setItem(35,i);
            player.getInventory().setItem(22,new ItemStack(Material.LEATHER_HELMET));
            player.getInventory().setItem(23,new ItemStack(Material.LEATHER_CHESTPLATE));
            player.getInventory().setItem(24,new ItemStack(Material.LEATHER_BOOTS));


        }
        gameround = 1;
        List<String> strings = config.getStringList("Ring.LastCenter");
        String[] strings1 = strings.get(new Random().nextInt(strings.size())).split(" ");
        LastRingX = Integer.parseInt(strings1[0]);
        LastRingZ = Integer.parseInt(strings1[1]);
        RingXs[0] = config.getInt("Map.Center.x");
        RingZs[0] = config.getInt("Map.Center.z");
        int lastRadius = config.getInt("Ring."+config.getInt("Ring.Times")+".Radius");
        for(int i = 1;i < config.getInt("Ring.Times") ;i++){
            int px = 0;
            int mx = 0;
            int nextRadius = config.getInt("Ring." + (i + 1) + ".Radius");
            int nowRadius = config.getInt("Ring." + (i) + ".Radius");
            if((LastRingX+lastRadius- nextRadius)/*MXで、次の安置が範囲に入る値*/ > (RingXs[i-1] - nowRadius + nextRadius)/*MXで、安置が前の安置の範囲に入る値*/){
                mx = (LastRingX+lastRadius- nextRadius);/*MXで、次の安置が範囲に入る値*/
            }else {
                mx = (RingXs[i-1] - nowRadius + nextRadius);/*MXで、安置が前の安置の範囲に入る値*/
            }
            if(LastRingX-lastRadius+ nextRadius/*PXで同上*/ < (RingXs[i-1] + nowRadius - nextRadius)/*PXで同上*/){
                px = LastRingX-lastRadius+ nextRadius;
            }else {
                px = (RingXs[i-1] + nowRadius - nextRadius);
            }
            if(px-mx>0) {
                RingXs[i] = mx + new Random().nextInt(px - mx);
            }else {
                RingXs[i] = mx;
            }

            int pz ;
            int mz;
            if((LastRingZ+lastRadius- nextRadius)/*MZで、次の安置が範囲に入る値*/ > (RingZs[i-1] - nowRadius + nextRadius)/*MZで、安置が前の安置の範囲に入る値*/){
                mz = (LastRingZ+lastRadius- nextRadius);/*MZで、次の安置が範囲に入る値*/
            }else {
                mz = (RingZs[i-1] - nowRadius + nextRadius);/*MZで、安置が前の安置の範囲に入る値*/
            }
            if(LastRingZ-lastRadius+ nextRadius/*PZで同上*/ < (RingZs[i-1] + nowRadius - nextRadius)/*PZで同上*/){
                pz = LastRingZ-lastRadius+ nextRadius;
            }else {
                pz = (RingZs[i-1] + nowRadius - nextRadius);
            }
            if(pz-mz>0) {
                RingZs[i] = mz + new Random().nextInt(pz - mz);
            }else {
                RingZs[i] = mz;
            }
        }
        RingXs[config.getInt("Ring.Times")] = LastRingX;
        RingZs[config.getInt("Ring.Times")] = LastRingZ;

        BorderShrinkSystem.Initializer(config.getInt("MAP.Center.x"),config.getInt("MAP.Center.z"),config.getInt("MAP.Radius")/2);

        plugin.getServer().getScoreboardManager().getMainScoreboard().getObjective("teams").getScore("system").setScore(10);

        watchTeamCount();
        BorderDamager.setPower(true);
        startRound();
    }
    public static void startRound(){


        for(Player player : plugin.getServer().getOnlinePlayers()){
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "ラウンド" + V.gameround);
            player.sendMessage("ボーダーの収縮待機を開始");
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK,1,0);

            BossBar.setBossBarColor(BarColor.GREEN);
        }

        time = config.getInt("Ring."+gameround+".sTime");

        BorderInfo.setTargetPX(RingXs[gameround]+config.getInt("Ring." + (gameround+1) + ".Radius"));
        BorderInfo.setTargetMX(RingXs[gameround]-config.getInt("Ring." + (gameround+1) + ".Radius"));
        BorderInfo.setTargetPZ(RingZs[gameround]+config.getInt("Ring." + (gameround+1) + ".Radius"));
        BorderInfo.setTargetMZ(RingZs[gameround]-config.getInt("Ring." + (gameround+1) + ".Radius"));

        BorderDamager.setDamage((float) config.getDouble("Ring." + gameround + ".Damage"));


        V.RoundTasks.add(
            new BukkitRunnable(){
                @Override
                public void run() {
                    BossBar.setBossBarTitle(ChatColor.YELLOW + "ラウンド" + V.gameround + ChatColor.GREEN + " - ボーダー収縮待機中・・・" + ChatColor.AQUA + (time - time % 60) / 60 + ":" + time % 60 + ChatColor.GRAY + " - 残り部隊数 :" + TeamCount);
                    BossBar.setBossBarProgress(time/config.getDouble("Ring."+gameround+".sTime"));
                    BossBar.showBossBar();

                    time--;

                    if(time <= 0) {
                        startMoveRound();
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0,20)
        );
    }
    public static void startMoveRound(){

        for(Player player : plugin.getServer().getOnlinePlayers()){
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "ラウンド" + V.gameround);
            player.sendMessage("ボーダーの収縮を開始");
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK,1,0);


            BossBar.setBossBarColor(BarColor.RED);
        }

        time = config.getInt("Ring."+gameround+".vTime");
        borderShrinkSystem = new BorderShrinkSystem(time*20,RingXs[gameround],RingZs[gameround],config.getInt("Ring." + (gameround + 1) + ".Radius")).runTaskTimer(jp.houlab.mochidsuki.border.Main.plugin,0,1);
        V.RoundTasks.add(
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        BossBar.setBossBarTitle(ChatColor.YELLOW + "ラウンド" + V.gameround + ChatColor.RED + " - ボーダー収縮中・・・" + ChatColor.AQUA + (time - time % 60) / 60 + ":" + time % 60 + ChatColor.GRAY + " - 残り部隊数 :" + TeamCount);
                        BossBar.setBossBarProgress(time/config.getDouble("Ring."+gameround+".vTime"));
                        BossBar.showBossBar();

                        time--;

                        if(time <= 0) {
                            gameround++;
                            if(gameround <= config.getInt("Ring.Times")) {
                                startRound();
                            }
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0,20)
        );
    }

    public static void endGame(){
        plugin.getServer().getScoreboardManager().getMainScoreboard().getObjective("teams").getScore("system").setScore(1);
        for(BukkitTask task : V.RoundTasks){
            task.cancel();
        }
        gameround = 0;
        BossBar.hideBossBar();
        if(borderShrinkSystem != null) {
            borderShrinkSystem.cancel();
        }
        BorderDamager.setPower(false);
    }

    public static void watchTeamCount(){
        Set<Team> teams = new HashSet<>();
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            if(player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) {
                try {
                    teams.add(player.getScoreboard().getEntityTeam(player));
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        V.TeamCount = teams.size();
        if(teams.size() <= 1){
            GameMainController.endGame();
        }
    }


}
