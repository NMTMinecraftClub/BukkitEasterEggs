package com.m0pt0pmatt.bukkiteastereggs;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.SpawnEgg;

public class EasterEgg {

	private final String name;
	private final String world;
	private final String colorName;
	private final int x, y, z;
	private ItemStack item;
	
	public EasterEgg(String name, String world, String colorName, int x, int y, int z){
		this.name = name;
		this.colorName = colorName;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		
		ChatColor colorPrefix;
		
		switch (colorName.toLowerCase()){
		case "green":
			colorPrefix = ChatColor.DARK_GREEN;
			item = new SpawnEgg(EntityType.CREEPER).toItemStack(1);
			break;
		case "gray":
			colorPrefix = ChatColor.GRAY;
			item = new SpawnEgg(EntityType.SKELETON).toItemStack(1);
			break;
		case "red":
			colorPrefix = ChatColor.DARK_RED;
			item = new SpawnEgg(EntityType.MUSHROOM_COW).toItemStack(1);
			break;
		case "blue":
			colorPrefix = ChatColor.BLUE;
			item = new SpawnEgg(EntityType.ZOMBIE).toItemStack(1);
			break;
		case "white":
			colorPrefix = ChatColor.WHITE;
			item = new SpawnEgg(EntityType.GHAST).toItemStack(1);
			break;
		case "lime":
			colorPrefix = ChatColor.GREEN;
			item = new SpawnEgg(EntityType.SLIME).toItemStack(1);
			break;
		case "pink":
			colorPrefix = ChatColor.RED;
			item = new SpawnEgg(EntityType.PIG).toItemStack(1);
			break;
		case "black":
			colorPrefix = ChatColor.BLACK;
			item = new SpawnEgg(EntityType.ENDERMAN).toItemStack(1);
			break;
		case "yellow":
			colorPrefix = ChatColor.YELLOW;
			item = new SpawnEgg(EntityType.BLAZE).toItemStack(1);
			break;
		case "brown":
			colorPrefix = ChatColor.GOLD;
			item = new SpawnEgg(EntityType.VILLAGER).toItemStack(1);
			break;
		default:
			colorPrefix = ChatColor.WHITE;
			item = new SpawnEgg(EntityType.GHAST).toItemStack(1);		
		}
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName("Easter Egg: " + name);
		
		List<String> lore = new LinkedList<String>();
		lore.add(colorPrefix + "A Special Easter Egg!");
		lore.add("Collect them all for a prize!");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
	}

	public String getName() {
		return name;
	}
	
	public String getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public ItemStack getItem() {
		return item;
	}
	
	@Override
	public String toString(){
		return "[Name: " + name + " Color: " + colorName + "]";
	}
	
	@Override
	public boolean equals(Object object){
		return object.equals(name);
	}
	
}
