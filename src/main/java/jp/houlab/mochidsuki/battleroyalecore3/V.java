package jp.houlab.mochidsuki.battleroyalecore3;

import jp.houlab.mochidsuki.border.BorderShrinkSystem;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class V {

    static int gameround;

    static World world;

    static int TeamCount;

    static List<BukkitTask> RoundTasks = new ArrayList<BukkitTask>();

    static BukkitTask borderShrinkSystem;
}
