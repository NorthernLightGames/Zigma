package io.github.northernlightgames.zigma.command;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandInfo {

	private final org.bukkit.command.Command command;
	private final org.bukkit.command.CommandSender sender;
	private final String label;
	private final String[] args;
	private final JavaPlugin plugin;
	
	public CommandInfo(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args, JavaPlugin plugin) {
		this.command = command;
		this.sender = sender;
		this.label = label;
		this.args = args;
		this.plugin = plugin;
	}

	public org.bukkit.command.Command getCommand() {
		return command;
	}

	public org.bukkit.command.CommandSender getSender() {
		return sender;
	}

	public String getCommandLabel() {
		return label;
	}

	public String[] getCommandArguments() {
		return args;
	}
	
	public String[] args() {
		return args;
	}
	
	public JavaPlugin getPlugin() {
		return plugin;
	}
}
