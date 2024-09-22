package jp.houlab.mochidsuki.battleroyalecore3;

import jp.houlab.mochidsuki.armorshield.ShieldUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.config;
import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;

/**
 * 生存しているプレイヤーの体力リストをチャットに送信する
 * @author Mochidsuki
 */
public class HealthBarShower extends BukkitRunnable {
    private final Player player;

    /**
     * コンストラクタ
     * @param player 送信するプレイヤー
     */
    public HealthBarShower(Player player) {
        this.player = player;
    }

    /**
     * 実行メソッド
     */
    @Override
    public void run() {
        sendMessage(player);
    }

    /**
     * メッセージを送信する
     * @param player 送信するプレイヤー
     */
    static public void sendMessage(Player player) {
        int i = 0;
        TextComponent[] textComponents = new TextComponent[10];

        for(Player enemy : plugin.getServer().getOnlinePlayers()) {
            if (i < 10) {
                if (enemy.getGameMode() == GameMode.SURVIVAL || enemy.getGameMode() == GameMode.ADVENTURE) {
                    ShieldUtil shieldUtil = new ShieldUtil(enemy.getInventory().getItem(jp.houlab.mochidsuki.armorshield.Main.config.getInt("ChestPlateSlot")));
                    int health = (int) enemy.getHealth();

                    TextComponent component = new net.md_5.bungee.api.chat.TextComponent();
                    String shieldS;
                    String half;
                    String openings;

                    shieldS = String.join("", Collections.nCopies((int) (shieldUtil.getShieldNow() / 2), "■"));

                    if (!(shieldUtil.getShieldNow() % 2 == 0)) {
                        half = "□";
                    } else {
                        half = "";
                    }
                    openings = String.join("", Collections.nCopies((int) ((shieldUtil.getShieldMax() - shieldUtil.getShieldNow()) / 2), "-"));

                    String healthH;
                    String halfH;
                    String openingsH;

                    healthH = String.join("", Collections.nCopies((health / 2), "■"));

                    if (!(health % 2 == 0)) {
                        halfH = "□";
                    } else {
                        halfH = "";
                    }
                    openingsH = String.join("", Collections.nCopies((int) ((enemy.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - health) / 2), "-"));

                    ChatColor teamColor = ChatColor.WHITE;
                    if(enemy.getScoreboard().getPlayerTeam(enemy) != null){
                        teamColor = enemy.getScoreboard().getPlayerTeam(enemy).getColor();
                    }

                    component.setText(teamColor + String.format("%-16s", enemy.getName()) + ChatColor.WHITE + "[" + shieldUtil.getShieldColor() + openings + half + shieldS + ChatColor.WHITE + healthH + halfH + openingsH + "]");
                    textComponents[i] = component;
                    i++;
                }
            }
        }
        for(TextComponent textComponent : textComponents){
            if(textComponent != null) {
                player.sendMessage(textComponent);
            }else {
                player.sendMessage(" ");
            }
        }
    }
}
