package io.github.northernlightgames.zigma.minigame.parkour;

import org.bukkit.ChatColor;

public class ParkourStick {
	
	private int stage;
	private ParkourGame game;

	public ParkourStick(ParkourGame game, int i) {
		this.game = game;
		this.stage = i;
	}
	
	public int getStage() {
		return stage;
	}
	
	public void setStage(int i) {
		this.stage = i;
	}

	public static String getLoreName() {
		return ChatColor.GRAY + "Parkour stick :D";
	}

}
