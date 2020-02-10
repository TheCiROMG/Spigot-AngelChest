package de.jeffclan.AngelChest;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AngelChest {

	final ItemStack[] armorInv;
	final ItemStack[] storageInv;
	final ItemStack[] extraInv;
	final Inventory overflowInv;
	Block block;
	UUID owner;
	Hologram hologram;
	boolean isProtected;
	
	
	public AngelChest(UUID owner,Block block, PlayerInventory playerItems, AngelChestPlugin plugin) {
		
		this.owner=owner;
		this.block=block;
		this.isProtected = plugin.getServer().getPlayer(owner).hasPermission("angelchest.protect");
		
		String hologramText = String.format(plugin.messages.HOLOGRAM_TEXT, plugin.getServer().getPlayer(owner).getName());
		String inventoryName = String.format(plugin.messages.ANGELCHEST_INVENTORY_NAME, plugin.getServer().getPlayer(owner).getName());
		overflowInv = Bukkit.createInventory(null, 54, inventoryName);
		createChest(block);
		hologram = new Hologram(block, hologramText,plugin);
		
		armorInv = playerItems.getArmorContents();
		storageInv = playerItems.getStorageContents();
		extraInv = playerItems.getExtraContents();
		
		AngelChest me = this;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if(plugin.isAngelChest(block)) {
					Utils.destroyAngelChest(block, me, plugin);
					Player player = plugin.getServer().getPlayer(owner);
					if(player != null) {
						player.sendMessage(plugin.messages.MSG_ANGELCHEST_DISAPPEARED);
					}
				}
			}
		}, plugin.getConfig().getLong("angelchest-duration")*20);
	}
	
	private void createChest(Block block) {
		block.setType(Material.CHEST);
	}
	
	public void unlock() {
		this.isProtected = false;
	}
	
	public void saveToFile() {
		
	}
	
}