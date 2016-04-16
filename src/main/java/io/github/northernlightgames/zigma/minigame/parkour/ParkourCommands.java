package io.github.northernlightgames.zigma.minigame.parkour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.northernlightgames.zigma.SaveData;
import io.github.northernlightgames.zigma.Zigma;
import io.github.northernlightgames.zigma.command.CommandInfo;
import io.github.northernlightgames.zigma.command.CommandMethod;

public class ParkourCommands implements SaveData, Listener {

	private final Zigma zigma;
	private final List<ParkourGame> games;
	
	public ParkourCommands(Zigma z) {
		this.zigma = z;
		this.games = loadGames();
		zigma.getServer().getPluginManager().registerEvents(this, zigma);
		zigma.getSaveDatas().add(this);
	}
	
	@SuppressWarnings("unchecked")
	private List<ParkourGame> loadGames() {
		File folder = new File(zigma.getDataFolder(), "parkour/");
		File file = new File(folder, "ParkourGames.dat");
		if(!file.exists()) {
			try {
				folder.mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new ArrayList<ParkourGame>();
		}
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(file));
			Object o = (Object)in.readObject();
			if(!(o instanceof List<?>))  return new ArrayList<ParkourGame>();
			return (List<ParkourGame>) o;
		} catch (ClassNotFoundException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {}
		}
		return games; 
	}
	
	public void saveData() {
		File file = new File(zigma.getDataFolder(), "parkour/ParkourGames.dat");
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file, false));
			out.writeObject(games);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {}
		}
	}

	@CommandMethod(description = "Creates a stick to edit the parkour game with", 
			name = "parkourstick", 
			permission = "zigma.parkour", 
			usage = "/parkourstick <parkourgame> [<stage>]",
			mustBePlayer = true)
	public boolean stick(CommandInfo info) {
		if(info.args().length < 1) return false;
		Player player = (Player) info.getSender();
		ParkourGame game = getGame(info.args()[0]);
		int stage = 0;
		if(info.args().length>1) try {
			stage = Integer.parseInt(info.args()[1]);
		} catch(Exception e) {
			player.sendMessage("Bad argument 1:" + info.args()[1] + ". Should be an integer.");
			return false;
		}
		ItemStack stick = createParkourStick(game, stage);
		player.getInventory().setItemInMainHand(stick);
		return true;
	}
	
	@CommandMethod(description = "Changes a stick stage", 
			name = "edparkourstick", 
			permission = "zigma.parkour", 
			usage = "/edparkourstick <stage>",
			mustBePlayer = true)
	public boolean edStick(CommandInfo info) {
		if(info.args().length < 1) return false;
		Player player = (Player) info.getSender();
		ItemStack i = player.getInventory().getItemInMainHand();
		try {
			setParkourStickStage(i, Integer.parseInt(info.args()[0]), player);
		} catch(NumberFormatException e) {
			player.sendMessage("Bad argument 1:" + info.args()[0] + ". Should be an integer.");
		}
		return true;
	}

	private void setParkourStickStage(ItemStack ist, int new_stage, Player caller) {
		if(ist.getType() != Material.STICK) return;
		ItemMeta meta = ist.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore == null) {
			caller.sendMessage("Invalid parkour stick");
			return;
		}
		if(lore.size() < 2) {
			caller.sendMessage("Invalid parkour stick");
			return;
		}
		ParkourGame game = getGame(lore.get(0));
		if(game == null) {
			caller.sendMessage("Invalid parkour stick: The parkour game "
					+ lore.get(0) + "wasn't found. "
					+ "Maybe it has been deleted?");
			return;
		}
		lore.set(1, String.valueOf(new_stage));
		meta.setLore(lore);
		ist.setItemMeta(meta);
	}

	private ParkourGame getGame(String string) {
		for(ParkourGame game: games) {
			if(game.getName().equals(string))
				return game;
		}
		return null;
	}

	private ItemStack createParkourStick(ParkourGame game, int stage) {
		ItemStack stack = new ItemStack(Material.STICK);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("Parkour stick");
		List<String> lore = new ArrayList<String>();
		lore.add(game != null ? game.getName() : "null");
		lore.add(String.valueOf(stage));
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return null;
	}

	@CommandMethod(description = "Creates a new parkour game", 
			name = "parkourgame", 
			permission = "zigma.parkour", 
			usage = "/parkourgame <name>",
			mustBePlayer = true)
	public boolean newGame(CommandInfo info) {
		if(info.args().length < 1) return false;
		String game_name = info.args()[0];
		if(getGame(game_name) != null) {
			info.getSender().sendMessage("A parkour game with name " + game_name + " already exists!");
			return true;
		}
		games.add(new ParkourGame(game_name, zigma));
		info.getSender().sendMessage("Created " + game_name + "!");
		return true;
	}
	

	@CommandMethod(description = "Deletes an existing parkour game", 
			name = "delparkour", 
			permission = "zigma.parkour", 
			usage = "/delparkour <name>",
			mustBePlayer = true)
	public boolean delGame(CommandInfo info) {
		if(info.args().length < 1) return false;
		ParkourGame game = getGame(info.args()[0]);
		
		if(game == null) {
			info.getSender().sendMessage("Unable to find parkour game: " + info.args()[0]);
			return true;
		}
		games.remove(game);
		info.getSender().sendMessage("Removed: " + game.getName());
		return true;
	}
	@CommandMethod(description = "Lists all parkour games on this server", 
			name = "parkourgames", 
			permission = "zigma.parkour", 
			usage = "/parkourgames <name>",
			mustBePlayer = true)
	public boolean listParkourGames(CommandInfo info) {
		StringBuilder sb = new StringBuilder();
		sb.append("Parkour games: ");
		for(int i = 0; i < games.size(); i++) {
			if(i != 0) sb.append(", ");
			sb.append(games.get(i).getName());
		}
		info.getSender().sendMessage(sb.toString());
		return true;
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) 
			useParkourStick(event.getItem(), event.getClickedBlock(), event.getPlayer());
	}
	
	private void useParkourStick(ItemStack ist, Block block, Player player) {
		if(ist == null) return;
		if(ist.getType() != Material.STICK) return;
		ItemMeta meta = ist.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore == null) return;
		if(lore.size() < 2) return;
		ParkourGame game = getGame(lore.get(0));
		if(game == null) {
			player.sendMessage("Invalid parkour stick: The parkour game "
					+ lore.get(0) + "wasn't found. "
					+ "Maybe it has been deleted?");
			return;
		}
		int i = 0;
		try {
			i = Integer.parseInt(lore.get(1));
		} catch(Exception e) {
			player.sendMessage("Invalid parkour stick");
			return;
		}
		game.setBlock(block, i, player);
	}
}
