package me.gleeming.elevator;

import lombok.Getter;
import me.gleeming.elevator.listener.SignListener;
import me.gleeming.elevator.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ElevatorSigns extends JavaPlugin {
    @Getter private static ElevatorSigns instance;

    @Getter private Config configuration;
    public void onEnable() {
        instance = this;
        configuration = new Config("config.yml", this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Elevator Signs] Enabling hcf-elevator-signs by Gleeming.");
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
    }

    public void onDisable() { Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Elevator Signs] Enabling hcf-elevator-signs by Gleeming."); }
}
