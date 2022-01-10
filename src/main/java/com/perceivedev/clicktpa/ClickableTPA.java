package com.perceivedev.clicktpa;

import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ClickableTPA extends JavaPlugin implements Listener {

    public void onEnable() {
        // Register the listener
        getServer().getPluginManager().registerEvents(this, this);
        // Save the default config
        saveDefaultConfig();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandRun(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }

        String command = event.getMessage().substring(1);
        String[] split = command.split(" ");
        if (split[0].equalsIgnoreCase("tpa")) {
            // Command run is "/tpa"
            handleCommand(event.getPlayer(), split, "tpa");
        } else if (split[0].equalsIgnoreCase("tpahere")) {
            // Command run is "/tpahere"
            handleCommand(event.getPlayer(), split, "tpahere");
        }
    }

    private void handleCommand(Player player, String[] command, String permission) {
        if (player.hasPermission(getConfig().getString("permission." + permission))) {
            // Sender has permission
            if (command.length == 2) {
                // Has player as argument
                if (Bukkit.getPlayer(command[1]) != null) {
                    // Valid player (and online)
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sendMessages(Bukkit.getPlayer(command[1]));
                        }
                    }.runTaskLater(this, 1);
                }
            }
        }
    }

    private void sendClickableMessage(Player player) {
        FileConfiguration config = getConfig();
        String configMessage = config.getString("message.line", "");
        if (configMessage.isEmpty()) {
            return;
        }

        JSONMessage message = JSONMessage.create();
        boolean useDisplayName = config.getBoolean("message.use-display-name");
        for (String word : configMessage.split(" ")) {
            if (word.equalsIgnoreCase("%accept%")) {
                message.then(colorize(config.getString("message.accept"))).runCommand("/tpaccept");
            } else if (word.equalsIgnoreCase("%deny%")) {
                message.then(colorize(config.getString("message.deny"))).runCommand("/tpdeny");
            } else {
                message.then(colorize(word.replace("%player%", useDisplayName ? player.getDisplayName() : player.getName()))).color(ChatColor.GOLD);
            }
            message.then(" ");
        }
        message.send(player);
    }

    private void sendMessages(Player player) {
        sendSpacer(player);
        sendClickableMessage(player);
        sendSpacer(player);
    }

    private void sendSpacer(Player player) {
        String spacer = getConfig().getString("message.spacer", "");
        if (spacer.equals("bar")) {
            JSONMessage.create().bar().send(player);
        } else if (!spacer.isEmpty()) {
            player.sendMessage(colorize(spacer));
        }
    }

    private String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}