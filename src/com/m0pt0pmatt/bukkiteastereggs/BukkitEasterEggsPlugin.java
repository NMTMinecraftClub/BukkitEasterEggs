package com.m0pt0pmatt.bukkiteastereggs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitEasterEggsPlugin extends JavaPlugin implements Listener{

	private final String filename = "config.yml";
	private final File configFile = new File(getDataFolder(), filename);
	private Map<Location, EasterEgg> eggLocations;
	private Map<String, EasterEgg> eggNames;
	private Map<String, Set<EasterEgg>> players;
	
	@Override
	public void onEnable(){
		
		eggNames = new HashMap<String, EasterEgg>();
		eggLocations = new HashMap<Location, EasterEgg>();
		players = new HashMap<String, Set<EasterEgg>>();
		
		try{
			createFiles();
			load();
		}
		catch(Exception e){
			getLogger().warning("Unable to load");
			e.printStackTrace();
		}
		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable(){
		save();
	}
	
	private void createFiles() throws IOException{
		if (!this.getDataFolder().exists()){
			this.getDataFolder().mkdir();
		}
		if (!configFile.exists()){
			configFile.createNewFile();
		}		
	}
	
	private void load() throws IOException, InvalidConfigurationException{
		FileConfiguration config = this.getConfig();	
		config.load(configFile);
		
		if (config.contains("eggs")){
			ConfigurationSection eggsSection = config.getConfigurationSection("eggs");
			
			for (String eggPath: eggsSection.getKeys(false)){
				ConfigurationSection eggSection = eggsSection.getConfigurationSection(eggPath);
				
				String name = (eggSection.contains("name")) ? eggSection.getString("name") : "Easter Egg!";
				String world = (eggSection.contains("world")) ? eggSection.getString("world") : "HomeWorld";
				String colorName = (eggSection.contains("color")) ? eggSection.getString("color") : "WHITE";
								
				int x = (eggSection.contains("x")) ? eggSection.getInt("x") : 0;
				int y = (eggSection.contains("y")) ? eggSection.getInt("y") : 0;
				int z = (eggSection.contains("z")) ? eggSection.getInt("z") : 0;
				
				EasterEgg egg = new EasterEgg(name, world, colorName, x, y, z);
				
				this.getLogger().info("Loaded Egg: " + egg);
				
				eggLocations.put(new Location(Bukkit.getWorld(world), x, y, z), egg);
				eggNames.put(name, egg);
			}
		}
		
		if (config.contains("players")){
			ConfigurationSection playersSection = config.getConfigurationSection("players");
			for (String playerName: playersSection.getKeys(false)){
				List<String> eggList = playersSection.getStringList(playerName);
				Set<EasterEgg> eggs = new HashSet<EasterEgg>();
				for (String eggName: eggList){
					if (eggNames.containsKey(eggName)){
						eggs.add(eggNames.get(eggName));
					}
				}
				players.put(playerName, eggs);
			}
		}
	}
	
	public void save(){
		FileConfiguration config = this.getConfig();
		ConfigurationSection playerSection = config.createSection("players");
		
		for (Entry<String, Set<EasterEgg>> entry: players.entrySet()){
			List<String> eggNames = new LinkedList<String>();
			for (EasterEgg egg: entry.getValue()){
				eggNames.add(egg.getName());
			}
			playerSection.set(entry.getKey(), eggNames);
		}
		
		try {
			config.save(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void playerUsesEasterEgg(PlayerInteractEvent event){
		
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			return;
		}
				
		if (!event.hasItem()){
			return;
		}
		
		if (!event.getItem().getType().equals(Material.MONSTER_EGG)){
			return;
		}
		
		ItemMeta meta = event.getItem().getItemMeta();
		if (meta == null) return;
		
		if (meta.getDisplayName() == null){
			return;
		}
		
		if (meta.getDisplayName().toLowerCase().startsWith("easter egg")){
			event.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void playerRenamesEgg(InventoryClickEvent event){
		if (event.getView().getType() == InventoryType.ANVIL) {
			if (event.getRawSlot() == 2) {
				if (event.getView().getItem(0).getType() != Material.AIR && event.getView().getItem(2).getType() != Material.AIR) {
					if (event.getView().getItem(0).getItemMeta().getDisplayName() != event.getView().getItem(2).getItemMeta().getDisplayName()) {
						if (event.getView().getItem(0).getItemMeta().getDisplayName().toLowerCase().startsWith("easter egg")){
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onEggFound(PlayerInteractEvent event){
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Location loc = event.getClickedBlock().getLocation();
			
			if (eggLocations.containsKey(loc)){
				EasterEgg egg = eggLocations.get(loc);
				
				Player player = event.getPlayer();
				if (!players.containsKey(player.getName())){
					players.put(player.getName(), new HashSet<EasterEgg>());
				}
				
				Set<EasterEgg> eggs = players.get(player.getName());
				if (!eggs.contains(egg)){
					if (player.getInventory().firstEmpty() == -1){
						player.sendMessage("No room in your inventory.");
					}
					else{
						player.sendMessage("You found an Easter Egg!");
						Bukkit.getServer().broadcastMessage(player.getName() + " found the egg " + egg.getName() + "!");
						player.getInventory().addItem(egg.getItem().clone());
						player.updateInventory();
						eggs.add(egg);
						players.put(player.getName(), eggs);
					}
				}		
			}
		}
	}
}
