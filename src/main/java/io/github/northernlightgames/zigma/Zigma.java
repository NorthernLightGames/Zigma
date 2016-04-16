package io.github.northernlightgames.zigma;

import java.util.ArrayList;
import java.util.List;

import io.github.northernlightgames.zigma.command.CommandGroupExecutor;
import io.github.northernlightgames.zigma.minigame.parkour.ParkourCommands;

import org.bukkit.plugin.java.JavaPlugin;

public class Zigma extends JavaPlugin {
	public static final String author = "Valdemar Melin";
	public static final String version = "1.0.0";
	public static final Class<?>[] command_groups = new Class[] {
		ParkourCommands.class,
		TestCommands.class
	};
	/* public static final Class<?>[] command_groups_static = new Class[] {
		ParkourCommands.class,
		TestCommands.class
	};*/
	
	private final CommandGroupExecutor[] cmdExecs = new CommandGroupExecutor[command_groups.length/* + command_groups_static.length*/];
	private final List<SaveData> saves = new ArrayList<SaveData>();
	
	@Override
    public void onEnable() {
		if(!this.getDataFolder().exists())
			this.getDataFolder().mkdirs();
		saves.clear();
		initCommands();
    }

	private void initCommands() {
		int C = 0;
    	for(Class<?> klass: command_groups) {
    		CommandGroupExecutor cmdExe = null;
			try {
				cmdExe = new CommandGroupExecutor(klass.getConstructor(getClass()).newInstance(this), this);
			} catch (NoSuchMethodException e) {
				try {
					cmdExe = new CommandGroupExecutor(klass.getConstructor(), this);
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    		cmdExecs[ C++ /*HAH*/] = cmdExe;
    	}
	}

	@Override
    public void onDisable() {
		for(SaveData save: saves) {
			save.saveData();
		}
		for(int i = 0; i < cmdExecs.length; i++) {
			cmdExecs[i] = null;
		}
    	getLogger().info("Zigma has been disabled :(");
    }

	public List<SaveData> getSaveDatas() {
		return saves;
	}

}
