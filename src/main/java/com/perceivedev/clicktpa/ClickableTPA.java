package com.perceivedev.clicktpa;

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

    private JSONMessage getLine(Player player, String configPath) {
        FileConfiguration config = getConfig();
        boolean useDisplayName = config.getBoolean("message.use-display-name");
        JSONMessage message = JSONMessage.create();
        if (config.getString("message." + configPath) != "") {
            String[] words = config.getString("message." + configPath).split(" ");
            for (int i = 0; i < words.length; i++) {
                String word = trans(words[i].replace("%player%", useDisplayName ? player.getDisplayName() : player.getName()));
                if (word.equalsIgnoreCase("%accept%")) {
                    message.then(trans(config.getString("message.accept"))).runCommand("/tpaccept");
                } else if (word.equalsIgnoreCase("%deny%")) {
                    message.then(trans(config.getString("message.deny"))).runCommand("/tpdeny");
                } else {
                    message.then(word).color(ChatColor.GOLD);
                }
                message.then(" ");
            }
        }
        return message;
    }

    private void sendMessages(Player player) {
        String spacer = getConfig().getString("message.spacer");
        sendSpacer(player, spacer);
        getLine(player, "line").send(player);
        sendSpacer(player, spacer);
    }

    private void sendSpacer(Player player, String spacer) {
        if (spacer.equals("")) {
            return;
        } else if (spacer.equals("bar")) {
            JSONMessage.create().bar().send(player);
        } else {
            player.sendMessage(trans(spacer));
        }
    }

    private String trans(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}