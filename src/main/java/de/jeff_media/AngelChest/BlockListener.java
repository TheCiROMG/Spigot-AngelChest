package de.jeff_media.AngelChest;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener {

    final Main plugin;

    public BlockListener(Main plugin) {

        this.plugin = plugin;

        plugin.debug("BlockListener created");
    }


    /*@EventHandler(priority = EventPriority.MONITOR)
    public void onHologramSpawn(EntitySpawnEvent e) {
        if(!plugin.hookUtils.hologramToBeSpawned) return;
            if(!e.isCancelled()) return;
            plugin.debug("Trying to prevent cancellation of EntitySpawnEvent for the hologram");
            e.setCancelled(false);
    }*/

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isAngelChest(event.getBlock()))
            return;
        AngelChest angelChest = plugin.getAngelChest(event.getBlock());
        if (!angelChest.owner.equals(event.getPlayer().getUniqueId())
                && !event.getPlayer().hasPermission("angelchest.protect.ignore") && angelChest.isProtected) {
            event.getPlayer().sendMessage(plugin.messages.MSG_NOT_ALLOWED_TO_BREAK_OTHER_ANGELCHESTS);
            event.setCancelled(true);
            return;
        }
        angelChest.destroy(false);
        angelChest.remove();
    }

    @EventHandler
    public void onLiquidDestroysChest(BlockFromToEvent event) {
        // Despite the name, this event only fires when liquid or a teleporting dragon egg changes a block
        if (plugin.isAngelChest(event.getToBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakingBlockThatThisIsAttachedTo(BlockBreakEvent e) {
        if (!plugin.isAngelChest(e.getBlock().getRelative(BlockFace.UP))) return;
        if(e.getBlock().getRelative(BlockFace.UP).getPistonMoveReaction()!= PistonMoveReaction.BREAK) return;

            e.setCancelled(true);
            plugin.debug("Preventing BlockBreakEvent because it interferes with AngelChest.");

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(plugin::isAngelChest);
    }

	/*@EventHandler
	public void onRightClickHologram(PlayerInteractAtEntityEvent e) {
		if(!(e.getRightClicked() instanceof ArmorStand)) {
			return;
		}

		ArmorStand as = (ArmorStand) e.getRightClicked();
		plugin.blockArmorStandCombinations.forEach((combination) -> {
			if(combination.armorStand.equals(as)) {
				plugin.getAngelChest(combination.block)

			}
		});

	}*/

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(plugin::isAngelChest);
    }

    @EventHandler
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
        Block block = event.getBlock();
        if (plugin.isAngelChest(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
        Block block = event.getBlock();
        if (plugin.isAngelChest(block)) {
            event.setCancelled(true);
        }
    }
}