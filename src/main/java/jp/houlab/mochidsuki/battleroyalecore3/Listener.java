package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.config;
import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;

public class Listener implements org.bukkit.event.Listener {


    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent event){
        Block block = event.getBlock();
        if (!(block.getType() == Material.FIRE || block.getType() == Material.OAK_DOOR || block.getType() == Material.SPRUCE_DOOR || block.getType() == Material.BIRCH_DOOR || block.getType() == Material.JUNGLE_DOOR || block.getType() == Material.ACACIA_DOOR || block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.MANGROVE_DOOR || block.getType() == Material.CRIMSON_DOOR || block.getType() == Material.WARPED_DOOR)) {
            if(!event.getPlayer().isOp()) {
                event.setCancelled(true);
            }
        }

    }
    @EventHandler
    public void EntityPickupItemEvent(EntityPickupItemEvent event){
        if(event.getEntity().hasPotionEffect(PotionEffectType.UNLUCK)){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void EntityPotionEffectEvent(EntityPotionEffectEvent event){
        ((Player)event.getEntity()).updateInventory();
    }
    @EventHandler
    public void PlayerItemConsumeEvent(PlayerItemConsumeEvent event){
        if(event.getItem().getType().equals(Material.ENCHANTED_GOLDEN_APPLE)){
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,1200,0,false,true,true));
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                    event.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
                    event.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    event.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,1200,1));
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,1200,3));
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,1200,0));
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,1200,0));
                }
            });
        }
        if(event.getItem().getType().equals(Material.MILK_BUCKET)&&event.getPlayer().hasPotionEffect(PotionEffectType.LUCK)){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent event){
        event.setResult(PlayerLoginEvent.Result.ALLOWED);
        if(V.gameround != 0 && !event.getPlayer().isOp()) {
            event.setKickMessage("試合中であるため入室できません");
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }


    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(event.getEntity().getType() == EntityType.PLAYER && (event.getDamager().getType() == EntityType.PLAYER || event.getDamager().getType() == EntityType.ARROW)){
            Player damager = null;
            if(event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            }else if(event.getDamager() instanceof Arrow) {
                damager = (Player) ((Arrow) event.getEntity()).getShooter();
            }
            Player player = (Player) event.getEntity();

            damager.setLevel((int) event.getDamage() + damager.getLevel());
        }
    }

    //Door Lock
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.OAK_DOOR || block.getType() == Material.SPRUCE_DOOR || block.getType() == Material.BIRCH_DOOR || block.getType() == Material.JUNGLE_DOOR || block.getType() == Material.ACACIA_DOOR || block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.MANGROVE_DOOR || block.getType() == Material.CRIMSON_DOOR || block.getType() == Material.WARPED_DOOR) {

                Player[] players = event.getPlayer().getServer().getOnlinePlayers().toArray(new Player[0]);

                if ((block.getBlockData().isFaceSturdy(BlockFace.NORTH, BlockSupport.FULL))) {
                    for (Player player : players) {
                        Location location = player.getLocation();
                        if (Math.abs(block.getX() + 0.5 - location.getX()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getZ() - location.getZ() < 0.8 && block.getZ() - location.getZ() >= -0.6) {
                            if (player.isSneaking()) {
                                event.setCancelled(true);
                            }
                        }

                    }
                }
                if ((block.getBlockData().isFaceSturdy(BlockFace.EAST, BlockSupport.FULL))) {
                    for (Player player : players) {
                        Location location = player.getLocation();
                        if (Math.abs(block.getZ() + 0.5 - location.getZ()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getX() - location.getX() < -0.4 && block.getX() - location.getX() >= -1.6) {
                            if (player.isSneaking()) {
                                event.setCancelled(true);
                            }
                        }

                    }
                }
                if ((block.getBlockData().isFaceSturdy(BlockFace.WEST, BlockSupport.FULL))) {
                    for (Player player : players) {
                        Location location = player.getLocation();
                        if (Math.abs(block.getZ() + 0.5 - location.getZ()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getX() - location.getX() < 0.8 && block.getX() - location.getX() >= -0.6) {
                            if (player.isSneaking()) {
                                event.setCancelled(true);
                            }
                        }

                    }
                }
                if ((block.getBlockData().isFaceSturdy(BlockFace.SOUTH, BlockSupport.FULL))) {
                    for (Player player : players) {
                        Location location = player.getLocation();
                        if (Math.abs(block.getX() + 0.5 - location.getX()) < 0.7 && Math.abs(block.getY() - location.getBlockY()) <= 1 && block.getZ() - location.getZ() < -0.4 && block.getZ() - location.getZ() >= -1.6) {
                            if (player.isSneaking()) {
                                event.setCancelled(true);
                            }
                        }

                    }
                }

            }
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event) {
        GameMainController.watchTeamCount();
    }
}
