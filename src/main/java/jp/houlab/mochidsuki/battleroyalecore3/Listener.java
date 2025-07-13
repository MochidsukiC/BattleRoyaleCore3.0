package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.config;
import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;
import static jp.houlab.mochidsuki.battleroyalecore3.V.getGameround;

/**
 * イベントリスナー
 * @author Mochidsuki
 */
public class Listener implements org.bukkit.event.Listener {

    /**
     *ブロックが破壊できないようにする
     * @param event イベント
     */
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
    public void BlockPlaceEvent(BlockPlaceEvent event){
        if(event.getBlockPlaced().getType().equals(Material.FIRE)){
            return;
        }

        if(!event.getPlayer().isOp()){
            event.setCancelled(true);
        }
    }

    /**
     * ノックダウン時にアイテムを拾えないようにする
     * @param event イベント
     */
    @EventHandler
    public void EntityPickupItemEvent(EntityPickupItemEvent event){
        if(event.getEntity().hasPotionEffect(PotionEffectType.UNLUCK)){
            event.setCancelled(true);
        }
    }

    /**
     * ポーション効果を受けたときインベントリを更新する
     * @param event イベント
     */
    @EventHandler
    public void EntityPotionEffectEvent(EntityPotionEffectEvent event){
        if(event.getEntity().getType() == EntityType.PLAYER) {
            ((Player) event.getEntity()).updateInventory();
        }
    }

    /**
     * 試合中のログインを拒否する
     * @param event イベント
     */
    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent event){
        event.setResult(PlayerLoginEvent.Result.ALLOWED);
        if(getGameround() != 0 && !event.getPlayer().isOp()) {
            event.setKickMessage("試合中であるため入室できません");
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    /**
     * ダメージを経験値に変換し攻撃者に与える
     * @param event イベント
     */
    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(event.getEntity().getType() == EntityType.PLAYER && (event.getDamager().getType() == EntityType.PLAYER || event.getDamager().getType() == EntityType.ARROW || event.getDamager().getType() == EntityType.FIREBALL || event.getDamager().getType() == EntityType.SPECTRAL_ARROW || event.getDamager().getType() == EntityType.TRIDENT)){
            Player damager = null;
            switch (event.getDamager().getType()){
                case PLAYER:{
                    damager = (Player) event.getDamager();
                    break;
                }
                case ARROW:{
                    if(((Arrow) event.getDamager()).getShooter() instanceof Player) {
                        damager = (Player) ((Arrow) event.getDamager()).getShooter();
                    }
                    break;
                }
                case FIREBALL:{
                    if(((Fireball) event.getDamager()).getShooter() instanceof Player) {
                        damager = (Player) ((Fireball) event.getDamager()).getShooter();
                    }
                    break;
                }
                case SPECTRAL_ARROW:{
                    if(((SpectralArrow) event.getDamager()).getShooter() instanceof Player){
                        damager = (Player) ((SpectralArrow) event.getDamager()).getShooter();
                    }
                    break;
                }
                case TRIDENT:{
                    if(((Trident) event.getDamager()).getShooter() instanceof Player){
                        damager = (Player) ((Trident) event.getDamager()).getShooter();
                    }
                    break;
                }
            }



            Player player = (Player) event.getEntity();
            if(damager != null && !damager.getUniqueId().equals(player.getUniqueId())) {
                if ((damager.getLevel() < 10 && event.getDamage() >= 10 - damager.getLevel()) || (damager.getLevel() < 30 && event.getDamage() >= 30 - damager.getLevel()) || (damager.getLevel() < 70 && event.getDamage() >= 70 - damager.getLevel()) || (damager.getLevel() < 140 && event.getDamage() >= 140 - damager.getLevel())) {
                    damager.playSound(damager.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, SoundCategory.MASTER, 1, 1);
                }

                damager.setLevel((int) event.getDamage() + damager.getLevel());
            }

            if(player.hasPotionEffect(PotionEffectType.INVISIBILITY) && !damager.getType().equals(EntityType.FIREBALL)){
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }

        }
    }

    /**
     * ドアロックする
     * @param event イベント
     * しゃがんでいるプレイヤーがドアの前にいるときドアが操作できなくなる
     */
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

    /**
     * プレイヤーが死んだら部隊数を確認する
     * @param event イベント
     */
    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event) {
        new BukkitRunnable() {
            public void run() {
                GameMainController.watchTeamCount(1);
            }
        }.runTaskLater(plugin,10);
    }
}
