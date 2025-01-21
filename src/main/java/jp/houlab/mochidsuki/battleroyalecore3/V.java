package jp.houlab.mochidsuki.battleroyalecore3;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class V {
    static private int gameRound;

    static private int TeamCount;

    static private List<BukkitTask> RoundTasks = new ArrayList<BukkitTask>();

    static private BukkitTask borderShrinkSystem;

    /**
     * ゲームラウンドを取得
     * @return ラウンド数
     */
    public static int getGameround() {
        return gameRound;
    }

    /**
     * ラウンド数を設定
     * @param gameRound ラウンド数
     */
    protected static void setGameround(int gameRound) {
        V.gameRound = gameRound;
    }

    /**
     * 生存しているチーム数を取得
     * @return チーム数
     */
    public static int getTeamCount() {
        return TeamCount;
    }

    /**
     * 生存しているチーム数を指定
     * @param teamCount チーム数
     */
    public static void setTeamCount(int teamCount) {
        TeamCount = teamCount;
    }

    /**
     * ラウンドを管理しているタスクのリストを取得
     * @return タスクのリスト
     */
    public static List<BukkitTask> getRoundTasks() {
        return RoundTasks;
    }

    /**
     * ボーダーの収縮を行うタスクを取得
     * @return ボーダーの収縮を行うタスク
     */
    public static BukkitTask getBorderShrinkSystem() {
        return borderShrinkSystem;
    }

    /**
     * ボーダーの収縮を行うタスクを設定
     * @param borderShrinkSystem　ボーダーの収縮を行うタスク
     */
    public static void setBorderShrinkSystem(BukkitTask borderShrinkSystem) {
        V.borderShrinkSystem = borderShrinkSystem;
    }


}
