package io.github.northernlightgames.zigma;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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
			if(sender instanceof Player) {
				Player player = (Player) sender;
				Location l = player.getLocation().add(new Vector(0, 100, 0));
				for(Entity e: player.getWorld().getEntities()) {
					e.teleport(l);
				}
			} else {
				sender.sendMessage("This command can only be run by a player.");
			}
			return true;
		}
		return false;
	}

}
