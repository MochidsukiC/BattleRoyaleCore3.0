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

import java.util.Objects;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if(command.getName().equalsIgnoreCase("gameround")) {
            if (args[0].equalsIgnoreCase("0")) {
                return false;
            }

            try {
                V.gameround = Integer.parseInt(args[0]);
            } catch (Exception e) {
                V.gameround = 0;
            }
            if (args[0].equalsIgnoreCase("stop")) {
                Border.stop = true;
                BorderShiver.stop = true;

                for (Player player : sender.getServer().getOnlinePlayers()) {
                    int rank = 1;
                    int kill = 0;
                    int assist = 0;
                    int damage = 0;
                    if (ui.ranking.containsKey(player.getScoreboard().getPlayerTeam(player))) {
                        rank = ui.ranking.get(player.getScoreboard().getPlayerTeam(player));
                    }
                    if (ui.kill.containsKey(player)) {
                        kill = ui.kill.get(player);
                    }
                    if (ui.assist.containsKey(player)) {
                        assist = ui.assist.get(player);
                    }
                    if (ui.damage.containsKey(player)) {
                        damage = ui.damage.get(player);
                    }
                    player.sendMessage("試合終了!!|第" + rank + "位");
                    player.sendMessage(kill + "キル|" + assist + "アシスト|" + damage + "ダメージ");
                    int ranking = 0;
                    switch (rank) {
                        default:
                        case 1:
                            ranking = ranking + 15;
                        case 2:
                            ranking = ranking + 8;
                        case 3:
                            ranking = ranking + 6;
                        case 4:
                            ranking = ranking + 5;
                        case 5:
                            ranking = ranking + 4;
                        case 6:
                        case 7:
                            ranking = ranking + 3;
                        case 8:
                        case 9:
                            ranking = ranking + 2;
                            break;

                    }

                    int xp = ranking + (kill + assist) * damage / 10;
                    player.sendMessage("XP:" + xp + "!!");
                    ui.kill.clear();
                    ui.killed.clear();
                    ui.damage.clear();
                    ui.ranking.clear();
                    ui.assist.clear();
                    ui.assisted.clear();
                    ui.knockDown.clear();
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(BattleRoyaleCore.getPlugin(), () -> {
                    World world = sender.getServer().getWorld("cxm");
                    try {
                        Block b = (Block) sender;
                        world = b.getWorld();
                    } catch (Exception e) {
                    }

                    try {
                        Player player = (Player) sender;
                        world = player.getWorld();
                    } catch (Exception e) {
                    }
                    world.getWorldBorder().setSize(10000000);
                },2L);


                return true;
            } else {


                Border.stop = false;
                BorderShiver.stop = false;


                Roundsystemc r = new Roundsystemc();
                World world = sender.getServer().getWorld("cxm");
                if (args.length == 1) {
                    try {
                        Block b = (Block) sender;
                        world = b.getWorld();
                    } catch (Exception e) {
                    }

                    try {
                        Player player = (Player) sender;
                        world = player.getWorld();
                    } catch (Exception e) {
                    }
                    r.Roundsystem(world);
                } else {
                    r.Roundsystem(sender.getServer().getWorld(args[1]));
                }
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("brc")){
            if(args[0].equalsIgnoreCase("reload")){
                Config config = new Config(BattleRoyaleCore.getPlugin());
                config.load();
                return true;
            }
            if(args[0].equalsIgnoreCase("setstart")){
                switch (args[1]) {
                    case "@a":
                        Player[] players = sender.getServer().getOnlinePlayers().toArray(new Player[0]);
                        for (Player player : players) {
                            GameStart g = new GameStart();
                            g.player(player);
                        }
                        return true;
                    case "@s":
                        GameStart g = new GameStart();
                        g.player((Player) sender);
                        return true;
                    default:
                        GameStart ga = new GameStart();
                        ga.player((Player) sender);
                        return true;
                }
            }
            /*
            if(args[0].equalsIgnoreCase("glow")){
                GlowAPI.setGlowing((Player)sender, GlowAPI.Color.WHITE , Bukkit.getOnlinePlayers());
                return true;
            }

             */



        }
        if(command.getName().equalsIgnoreCase("openelytra")) {
            switch (args[0]) {
                case "@a":
                    Player[] players = sender.getServer().getOnlinePlayers().toArray(new Player[0]);
                    for (Player player : players) {
                        player.setGliding(true);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,1000000,0,true,true));
                    }
                    return true;
                case "@s":
                    ((Player)sender).setGliding(true);
                    ((Player)sender).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,1000000,0,true,true));
                    return true;
                default:
                    ((Player)sender).setGliding(true);
                    return true;
            }
        }
        if(command.getName().equalsIgnoreCase("mapgenerator")){
            Player player = (Player) sender;

            ItemStack mapItem = new ItemStack(Material.FILLED_MAP,1);
            MapMeta mapMeta = (MapMeta)mapItem.getItemMeta();
            MapView view = player.getServer().createMap(player.getWorld());
            Objects.requireNonNull(mapMeta).setMapView(view);
            mapItem.setItemMeta(mapMeta);
            switch (args[0]) {
                case "0":
                    view.setScale(MapView.Scale.CLOSEST);
                    break;
                case "1":
                    view.setScale(MapView.Scale.CLOSE);
                    break;
                case "2":
                    view.setScale(MapView.Scale.NORMAL);
                    break;
                case "3":
                    view.setScale(MapView.Scale.FAR);
                    break;
                case "4":
                    view.setScale(MapView.Scale.FARTHEST);
                    break;
            }
            view.setTrackingPosition(true);
            view.setCenterX(V.mcx);
            view.setCenterZ(V.mcz);
            player.getInventory().setItem(8,mapItem);
        }
        if(command.getName().equalsIgnoreCase("debugerb")){
            sender.sendMessage(V.now[0]+","+ V.now[1]+","+ V.now[2]+","+ V.now[3]+","+V.roundstime[1]);
            return true;
        }
        return false;
    }
}
