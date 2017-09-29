/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package rip.anticheat.anticheat.learn;

import java.io.PrintStream;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.anticheat.anticheat.AntiCheat;
import rip.anticheat.anticheat.checks.Check;

public class LearningProccess {
    public static HashMap<String, Double> cheatingpre = new HashMap();

    public LearningProccess(AntiCheat antiCheat) {
    }

    public static void startLearning(Check check, Player player) {
        double d = cheatingpre.get(player.getName());
        if (d == 20.0) {
            System.out.println((Object)player + " has sent an amount of cheating logs, and now he is being checked for possible cheating [MACHINE LEARNING]");
        } else if (d == 50.0) {
            System.out.println((Object)player + " has sent a big amount of cheating logs and now he is being heavily checked.");
        } else if (d == 90.0) {
            System.out.println((Object)player + " is most likely cheating, and he is about to be banned. If you would like to cancel this action, freeze the player, or get him banned yourself [MACHINE LEARNING]");
        } else if (d == 100.0) {
            System.out.println((Object)player + " is cheating, and he has been removed from the network [MACHINE LEARNING]");
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + (Object)player + "Unfair Advantage [ML]");
        }
    }
}

