package com.perceivedev.clicktpa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickableTPA extends JavaPlugin implements Listener {

	public void onEnable() {
		// Register the listener
		getServer().getPluginManager().registerEvents(this, this);
		// Save the default config
		saveDefaultConfig();
	}

	@EventHandler
	public void onCommandRun(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage().substring(1);
		if (command.split(" ")[0].equalsIgnoreCase("tpa")) {
			// Command run is "/tpa"
			if (event.getPlayer().hasPermission(getConfig().getString("tpa-command-permission"))) {
				// Sender has permission
				if (command.split(" ").length == 2) {
					// Has player as argument
					if (Bukkit.getPlayer(command.split(" ")[1]) != null) {
						// Valid player (and online)
						String acceptText = getConfig().getString("message.accept");
						String denyText = getConfig().getString("message.deny");
						boolean useDisplayName = getConfig().getBoolean("message.use-display-name");
						String[] firstLine = getConfig().getString("message.line-1").split(" ");
						JSONMessage message;
						for (int i = 0; i < firstLine.length; i++) {
							if (i == 0) {
								message = JSONMessage.create();
								if (firstLine[i].contains("&")) {
									int index = firstLine[i].indexOf("&");
									ChatColor colour = ChatColor.getByChar(firstLine[i].substring(index, index + 1));
									// Add text to JSONMessage instance
								}
							}
						}
					}
				}
			}
		}
	}

}