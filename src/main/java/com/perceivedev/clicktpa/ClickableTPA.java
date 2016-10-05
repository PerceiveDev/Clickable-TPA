package com.perceivedev.clicktpa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
						sendMessages(Bukkit.getPlayer(command.split(" ")[1]));
					}
				}
			}
		}
	}
	
	private JSONMessage getFirstLine(Player player) {
		boolean useDisplayName = getConfig().getBoolean("message.use-display-name");
		JSONMessage message = JSONMessage.create();
		if (getConfig().getString("message.line-1") != "") {
			String[] words = getConfig().getString("message.line-1").split(" ");
			for (int i = 0; i < words.length; i++) {
				String word = trans(words[i].replace("%player%", useDisplayName ? player.getDisplayName() : player.getName()));
				if (word.equalsIgnoreCase("%accept%")) {
					message.then(word).runCommand("tpaccept");
				} else if (word.equalsIgnoreCase("%deny%")) {
					message.then(word).runCommand("tpdeny");
				} else {
					message.then(word);
				}
				message.then(" ");
			}
		}
		return message;
	}
	
	private JSONMessage getSecondLine(Player player) {
		boolean useDisplayName = getConfig().getBoolean("message.use-display-name");
		JSONMessage message = JSONMessage.create();
		if (getConfig().getString("message.line-2") != "") {
			String[] words = getConfig().getString("message.line-2").split(" ");
			for (int i = 0; i < words.length; i++) {
				String word = trans(words[i].replace("%player%", useDisplayName ? player.getDisplayName() : player.getName()));
				if (word.equalsIgnoreCase("%accept%")) {
					message.then(word).runCommand("tpaccept");
				} else if (word.equalsIgnoreCase("%deny%")) {
					message.then(word).runCommand("tpdeny");
				} else {
					message.then(word);
				}
				message.then(" ");
			}
		}
		return message;
	}
	
	private void sendMessages(Player player) {
		getFirstLine(player).send(player);
		getSecondLine(player).send(player);
	}
	
	private String trans(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}