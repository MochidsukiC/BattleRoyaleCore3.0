package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import static jp.houlab.mochidsuki.battleroyalecore3.Main.plugin;

/**
 * ボスバーのコントロールを司るクラス
 * @author Mochidsuki
 */
public class BossBar {
    static org.bukkit.boss.BossBar bossBar;

    /**
     * ボスバーを作成する
     */
    static void createBossBar() {
        bossBar = plugin.getServer().createBossBar("開始までお待ちください。。。", BarColor.WHITE, BarStyle.SEGMENTED_10);
        bossBar.setVisible(true);
    }

    /**
     * ボスバーのタイトルを指定する
     * @param title タイトル
     */
    static void setBossBarTitle(String title) {
        bossBar.setTitle(title);
    }

    /**
     * ボスバーの色を指定する
     * @param color 色
     */
    static void setBossBarColor(BarColor color) {
        bossBar.setColor(color);
    }

    /**
     * ボスバーの進行状況を指定する
     * @param progress 進行状況を0~1で指定します。
     */
    static void setBossBarProgress(double progress) {
        bossBar.setProgress(progress);
    }

    /**
     * ボスバーを表示する
     */
    static void showBossBar(){
        for(Player player : plugin.getServer().getOnlinePlayers()){
            bossBar.addPlayer(player);
            bossBar.setVisible(true);
        }
    }

    /**
     * ボスバーを非表示にする
     */
    static void hideBossBar(){
        bossBar.setVisible(false);
    }
}
