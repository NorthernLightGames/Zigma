package io.github.northernlightgames.zigma;

import io.github.northernlightgames.zigma.command.CommandInfo;
import io.github.northernlightgames.zigma.command.CommandMethod;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TestCommands {
	@SuppressWarnings("unused")
	private final Zigma plugin;

	public TestCommands(Zigma plugin) {
		this.plugin = plugin;
	}

	@CommandMethod(description = "Test command that teleports all entities in a radius of 1000 "
			+ "blocks 100 blocks up and to the callers location."
			+ "Carefull with this command! Note: can only be used by players because of security ;)",
			name = "zTestCommand", aliases = {"zTestCmd","zTest"}, permission = "Zigma.testcommand", usage = "/zTestCommand",
			mustBePlayer = true)
	public boolean testCmd1(CommandInfo info) {
		CommandSender sender = info.getSender();
		if(sender instanceof Player) {
			Player player = (Player) sender;
			Location l = player.getLocation().add(new Vector(0, 100, 0));
			for(Entity e: player.getWorld().getEntities()) {
				if(e.getLocation().distance(player.getLocation()) < 1000) e.teleport(l);
			}
		} else {
			sender.sendMessage("This command can only be run by a player.");
		}
		return true;
	}

}
