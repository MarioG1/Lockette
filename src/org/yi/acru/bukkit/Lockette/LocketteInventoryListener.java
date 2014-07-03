package org.yi.acru.bukkit.Lockette;

import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.minecart.HopperMinecart;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import org.bukkit.inventory.InventoryHolder;

import org.bukkit.plugin.PluginManager;

public class LocketteInventoryListener implements Listener {
	private static Lockette plugin;

	public LocketteInventoryListener(Lockette instance) {
		plugin = instance;
	}

	protected void registerEvents() {

		PluginManager pm = plugin.getServer().getPluginManager();

		pm.registerEvents(this, plugin);
	}

	//********************************************************************************************************************
	// Start of event section

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		InventoryHolder destHolder = event.getDestination().getHolder();
		if (destHolder instanceof HopperMinecart) {
			InventoryHolder sourceHolder = event.getSource().getHolder();
                        if (sourceHolder instanceof DoubleChest) {
                              sourceHolder = ((DoubleChest)sourceHolder).getLeftSide();
                        }
                        if (sourceHolder instanceof BlockState) {
                            if (Lockette.isProtected( ((BlockState)sourceHolder).getBlock() )) {
				event.setCancelled(true);
                            }
                        }
		}
            
	}
}