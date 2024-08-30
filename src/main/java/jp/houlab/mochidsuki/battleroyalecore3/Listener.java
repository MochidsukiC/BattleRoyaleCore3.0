package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Optional;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.config;
import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        //Inventory Locker
        if (config.getBoolean("ADVANCED_SETTING.Inventory_Locker")) {
            Optional<Material> cursor = Optional.of(Objects.requireNonNull(event.getCursor()).getType());
            int slot = event.getSlot();
            if(event.getClickedInventory() != null) {
                if (!(Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.PLAYER)) {
                    slot = 0;
                }
            }

            if (slot >= 11 && slot <= 17) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            if (slot == 20) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            if (slot >= 24 && slot <= 26) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            if (slot >= 29 && slot <= 39) {
                event.setCancelled(true);
                event.setResult(org.bukkit.event.Event.Result.DENY);
            }
            switch (slot) {
                case 21:
                    if (!(cursor.orElse(Material.LEATHER_HELMET) == Material.LEATHER_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.CHAINMAIL_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.GOLDEN_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.DIAMOND_HELMET || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_HELMET || event.getAction() == InventoryAction.DROP_ONE_SLOT || event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
                case 22:
                    if (!(cursor.orElse(Material.LEATHER_CHESTPLATE) == Material.LEATHER_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.CHAINMAIL_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.GOLDEN_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.DIAMOND_CHESTPLATE || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_CHESTPLATE || event.getAction() == InventoryAction.DROP_ONE_SLOT|| event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
                case 23:
                    if (!(cursor.orElse(Material.LEATHER_BOOTS) == Material.LEATHER_BOOTS || cursor.orElse(Material.LEATHER_BOOTS) == Material.CHAINMAIL_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.IRON_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.GOLDEN_BOOTS || cursor.orElse(Material.LEATHER_BOOTS) == Material.DIAMOND_BOOTS || cursor.orElse(Material.LEATHER_HELMET) == Material.NETHERITE_BOOTS || event.getAction() == InventoryAction.DROP_ONE_SLOT|| event.getAction() == InventoryAction.PICKUP_ALL)) {
                        event.setCancelled(true);
                        event.setResult(org.bukkit.event.Event.Result.DENY);
                    }
                    break;
            }
        }
    }

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
}
