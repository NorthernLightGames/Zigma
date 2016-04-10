package io.github.northernlightgames.zigma;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TestCommandExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Zigma plugin;

	public TestCommandExecutor(Zigma plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test")) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				Location l = player.getLocation();
				player.teleport(l.add(new Vector(0, 100, 0)));
			}
			return true;
		}
		return false;
	}

}
