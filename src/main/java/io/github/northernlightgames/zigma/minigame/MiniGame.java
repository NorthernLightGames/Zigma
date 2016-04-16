package io.github.northernlightgames.zigma.minigame;

import java.io.Serializable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MiniGame implements Serializable {
	private static final long serialVersionUID = -613118748443271072L;

	public MiniGame() {
		
	}
	
	public abstract void stopGame(CommandSender caller);
	
	public abstract void startGame(CommandSender caller, Player[] players, Object... extra);
	
	public abstract GameInstance getActiveGame();
	
	public boolean isRunning() {
		return getActiveGame() != null;
	}
}
