package io.github.northernlightgames.zigma.command;

import io.github.northernlightgames.zigma.Zigma;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class CommandGroupExecutor implements CommandExecutor {
	
	protected final Object commandGroup;
	protected final Map<String, Method> method_map;
	protected final Zigma zigma;
	
	public CommandGroupExecutor(Object commandGroup, Zigma zigma) {
		if(commandGroup == null) throw new NullPointerException("Error: CommandGroup can't be null");
		this.commandGroup = commandGroup;
		this.zigma = zigma;
		this.method_map = new HashMap<String, Method>();
		zigma.getLogger().info("CommandGroupExecutor created with " + commandGroup);
		initCommandMethodMap();
	}
	
	public static Method[] getCommandMethods(Object cmd_group) {
		Method[] all_methods;
		if(cmd_group instanceof Class) all_methods = ((Class<?>)cmd_group).getMethods();
		else all_methods = cmd_group.getClass().getMethods();
		
		List<Method> cmd_methods = new ArrayList<Method>();
		for(Method method: all_methods) {
			Object cmd = method.getAnnotation(
					CommandMethod.class);
			if(cmd == null) continue;
			if(!checkParams(method)) continue;
			System.out.println("Command method found");
			cmd_methods.add(method);
		}
		return (Method[]) cmd_methods.toArray(new Method[0]);
	}

	private void initCommandMethodMap() {
		for(Method method: commandGroup.getClass().getMethods()) {
			try {
				io.github.northernlightgames.zigma.command.CommandMethod cmd = method.getAnnotation(
						io.github.northernlightgames.zigma.command.CommandMethod.class);
				if(cmd == null) continue;
				if(!checkParams(method)) throw new CommandGroupException(
						"Bad CommandMethod declaration: " + method.getName());
				zigma.getLogger().info("Command method found: " + method.getName());
				method_map.put(cmd.name(), method);
				PluginCommand pCmd = zigma.getCommand(cmd.name());
				pCmd.setExecutor(this);
				
			} catch(CommandGroupException e) {
				e.printStackTrace();
			}
		}
	}

	private static final boolean checkParams(Method method) {
		if(method.getParameterCount() < 1) return false;
		if(method.getReturnType() != boolean.class) return false;
		if(method.getParameters()[0].getType() != CommandInfo.class) return false;
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	zigma.getLogger().info("Command recieved: " + label);
    	
		Method method = method_map.get(command.getName());
		if(method == null) throw new CommandException("Couldn't retrieve command method");
		
		CommandMethod cmd = method.getAnnotation(CommandMethod.class);
		if(cmd.mustBePlayer() && !(sender instanceof Player)) 
			sender.sendMessage("You must be a player to use this command, sorry! :-/ ");
		
		try {
			CommandInfo commandInfo = new CommandInfo(sender, command, label, args, zigma);
			return (boolean) method.invoke(commandGroup, commandInfo);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public static String getCommandTextYML(Object command_group) {
		StringBuilder string_builder = new StringBuilder();
		for(Method method: getCommandMethods(command_group)) {
			System.out.println(method);
			CommandMethod cmd = method.getAnnotation(CommandMethod.class);
			
			string_builder.append("   " + cmd.name() + ":\n");
			string_builder.append("      description: \"" + cmd.description() + "\"\n");
			string_builder.append("      usage: \"" + cmd.usage() + "\"\n");
			string_builder.append("      default: \"" + cmd.defAllowed() + "\"\n");
			string_builder.append("      permission: \"" + cmd.permission() + "\"\n");
			string_builder.append("      permission-message: \"" + cmd.permissionMessage() + "\"\n");
			
			if(cmd.aliases().length>0) {
				string_builder.append("      aliases: [");
				final int n = cmd.aliases().length;
				for(int i = 0; i < n; i++) {
					if(i != 0) string_builder.append(", ");
					string_builder.append("\"" + cmd.aliases()[i] + "\"");
				}
				string_builder.append("]\n");
			}
		}
		return string_builder.toString();
	}
	
}
