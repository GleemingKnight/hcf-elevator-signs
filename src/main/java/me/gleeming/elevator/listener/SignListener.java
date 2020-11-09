package me.gleeming.elevator.listener;

import me.gleeming.elevator.ElevatorSigns;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    @EventHandler
    public void onSign(SignChangeEvent e) {
        int currentLine = 0;
        for (String line : e.getLines()) {
            if (line.equalsIgnoreCase("[Elevator]")) {
                if (currentLine < 3) {
                    if (e.getLine(currentLine + 1).equalsIgnoreCase("Up")) {
                        e.setLine(0, "");
                        e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"));
                        e.setLine(2, ChatColor.translateAlternateColorCodes('&', "Up"));
                        e.setLine(3, "");
                    } else if (e.getLine(currentLine + 1).equalsIgnoreCase("Down")) {
                        e.setLine(0, "");
                        e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"));
                        e.setLine(2, ChatColor.translateAlternateColorCodes('&', "Down"));
                        e.setLine(3, "");
                    } else {
                        Bukkit.broadcastMessage(e.getLine(currentLine + 1));
                        e.getBlock().breakNaturally();
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ElevatorSigns.getInstance().getConfiguration().getConfig().getString("messages.invalid-direction")));
                    }
                }
            }

            currentLine++;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();

                if (sign.getLine(1).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"))) {
                    boolean teleported = false;
                    if (sign.getLine(2).equalsIgnoreCase("Up")) {
                        for (int i = e.getClickedBlock().getY() + 2; i < 256; i++) {
                            BlockState state = e.getClickedBlock().getWorld().getBlockAt(new Location(e.getClickedBlock().getWorld(), e.getClickedBlock().getX(), i, e.getClickedBlock().getZ())).getState();
                            if (state instanceof Sign) {
                                if (((Sign) state).getLine(1).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"))) {
                                    e.getPlayer().teleport(new Location(state.getWorld(), state.getLocation().getBlockX() + 0.5, state.getLocation().getBlockY(), state.getLocation().getBlockZ() + 0.5));
                                    teleported = true;

                                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ElevatorSigns.getInstance().getConfiguration().getColoredString("messages.teleported-up")));
                                }
                            }
                        }
                    } else if (sign.getLine(2).equalsIgnoreCase("Down")) {
                        for (int i = e.getClickedBlock().getY() - 2; i > 0; i--) {
                            BlockState state = e.getClickedBlock().getWorld().getBlockAt(new Location(e.getClickedBlock().getWorld(), e.getClickedBlock().getX(), i, e.getClickedBlock().getZ())).getState();
                            if (state instanceof Sign) {
                                if (((Sign) state).getLine(1).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"))) {
                                    e.getPlayer().teleport(new Location(state.getWorld(), state.getLocation().getBlockX() + 0.5, state.getLocation().getBlockY(), state.getLocation().getBlockZ() + 0.5));
                                    teleported = true;

                                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ElevatorSigns.getInstance().getConfiguration().getColoredString("messages.teleported-down")));
                                }
                            }
                        }
                    }

                    if(!teleported) e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ElevatorSigns.getInstance().getConfiguration().getColoredString("messages.no-sign-found")));
                }
            }
        }
    }
}
