package io.github.northernlightgames.zigma;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import io.github.northernlightgames.zigma.command.CommandGroupExecutor;

public class PluginYMLFile {

	private final String file;
	
	private PluginYMLFile() {
		StringBuilder sb = new StringBuilder();
		sb.append("name: Zigma\n");
		sb.append("main: " + Zigma.class.getName() + "\n");
		sb.append("version: " + Zigma.version + "\n"); 
		sb.append("author: " + Zigma.author + "\n"); 
		sb.append("commands:\n");
		for(Class<?> c: Zigma.command_groups) {
			sb.append(CommandGroupExecutor.getCommandTextYML(c));
		}
		file = sb.toString();
	}

	private void write(String file_path) throws FileNotFoundException {
		File file = new File(file_path);
		PrintWriter write = new PrintWriter(file);
		write.write(this.file);
		write.flush();
		write.close();
		System.out.println(file.getName() + " has been successfully generated!");
	}

	public static void main(String... args) throws FileNotFoundException {
		new PluginYMLFile().write(args[0]);
	}
}
