package io.github.northernlightgames.zigma.minigame.parkour;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.northernlightgames.zigma.Zigma;
import io.github.northernlightgames.zigma.minigame.GameInstance;
import io.github.northernlightgames.zigma.minigame.MiniGame;

public class ParkourGame extends MiniGame {
	private static final long serialVersionUID = 8438617405365082916L;
	private String name;
	transient Zigma zigma;
	private transient ParkourGameInstance game;
	private final ArrayList<ParkourBlock> blocks;
	
	public ParkourGame(String name, Zigma zigma) {
		this.name = name;
		this.zigma = zigma;
		this.blocks = new ArrayList<ParkourBlock>();
	}

	public String getName() {
		return name;
	}
	
	public int getStage(Block block) {
		for(ParkourBlock b: blocks) {
			if(b.x == block.getX() 
			&& b.y == block.getY() 
			&& b.z == block.getZ()) 
				return b.stage;
		}
		return -1;
	}
	
	private ParkourBlock getBlock(Block block) {
		for(ParkourBlock b: blocks) {
			if(b.x == block.getX() 
			&& b.y == block.getY() 
			&& b.z == block.getZ()) 
				return b;
		}
		return null;
	}

	public void setBlock(Block block, int i, Player player) {
		if(isRunning()) {
			player.sendMessage("You can't edit the parkour game while the game is running.");
			return;
		}
		ParkourBlock pb = getBlock(block);
		if(pb == null) {
			if(i < 0) return;
			Location l = block.getLocation();
			pb = new ParkourBlock(l, i);
			blocks.add(pb);
		}
		if(i < 0) blocks.remove(pb);
		else pb.stage = i;
	}
	
	@Override
	public void startGame(CommandSender caller, Player[] players, Object... extra) {
		if(isRunning()) {
			caller.sendMessage("An instance of the game is already running!");
			return;
		}
		game = new ParkourGameInstance(players, this);
	}
	
	private class ParkourBlock implements Serializable {
		public ParkourBlock(Location l, int i) {
			x = l.getBlockX();
			y = l.getBlockY();
			z = l.getBlockZ();
			stage = i;
		}
		private static final long serialVersionUID = -6143341353786331026L;
		public int stage;
		public final long x;
		public final long y;
		public final long z;
	}

	@Override
	public void stopGame(CommandSender caller) {
		if(!isRunning()) {
			caller.sendMessage("No instance of the game is running!");
			return;
		}
		game.stop();
		game = null;
	}

	@Override
	public GameInstance getActiveGame() {
		return game;
	}
}
