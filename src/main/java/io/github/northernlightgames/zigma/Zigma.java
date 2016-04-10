package io.github.northernlightgames.zigma;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class Zigma extends JavaPlugin {
	private CommandExecutor testCommand;
	
	@Override
    public void onEnable() {
		testCommand = new TestCommandExecutor(this);
		this.getCommand("test").setExecutor(testCommand);
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("Zigma has been disabled :(");
    }

}
