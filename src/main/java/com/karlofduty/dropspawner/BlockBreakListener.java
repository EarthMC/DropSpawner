package com.karlofduty.dropspawner;

import java.util.Objects;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class BlockBreakListener implements Listener {

	private final DropSpawner plugin;

	public BlockBreakListener(final DropSpawner plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) {
		// Abort if not a mob spawner
		final Block block = event.getBlock();
		if (block.getType() != Material.SPAWNER) {
			return;
		}

		final Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE) {
			plugin.getLogger().info(player.getName() + " broke a spawner but was in creative mode.");
			return;
		}

		// Abort if not the required tool
		final Location location = block.getLocation();
		final String locationStr = formatLocation(block);
		final ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if (plugin.getConfig().getBoolean("require-pickaxe") && !isPickaxe(itemInHand.getType())) {
			plugin.getLogger().info(player.getName() + " broke a spawner at " + locationStr + ", but did not use a pickaxe.");
			return;
		}

		// Abort if not the required enchant
		if (plugin.getConfig().getBoolean("require-silktouch") && !itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
			plugin.getLogger().info(player.getName() + " broke a spawner at " + locationStr + ", but did not use silk touch.");
			return;
		}

		// Abort if the player does not have permission to get spawner drops
		if (!player.hasPermission("dropspawner.allowdrop")) {
			plugin.getLogger().info(player.getName() + " broke a spawner at " + locationStr + ", but did not have permission to drop it.");
			return;
		}

		block.getWorld().dropItemNaturally(location, new ItemStack(Material.SPAWNER)); // ItemStack has an amount of 1 by default.
		plugin.getLogger().info(player.getName() + " broke a spawner successfully at " + locationStr + ".");

		final EntityType spawnerType = ((CreatureSpawner) block.getState()).getSpawnedType();
		if (spawnerType != EntityType.UNKNOWN && spawnerType != EntityType.PIG) {
			// Use Bukkit's in-built name reference map.
			block.getWorld().dropItemNaturally(location, new ItemStack(Objects.requireNonNull(Material.getMaterial(spawnerType + "_SPAWN_EGG"))));
			plugin.getLogger().info(player.getName() + " got a spawn egg.");
		}

		event.setExpToDrop(0);
		event.setDropItems(false);
	}

	private String formatLocation(final Block block) {
		// Block's world will never be null.
		return "{world=" + block.getWorld() + ", x=" + block.getX() + ", y=" + block.getY() + ", z=" + block.getZ() + "}";
	}

	private boolean isPickaxe(final Material material) {
		switch (material) {
			case WOODEN_PICKAXE:
			case STONE_PICKAXE:
			case IRON_PICKAXE:
			case GOLDEN_PICKAXE:
			case DIAMOND_PICKAXE:
			case NETHERITE_PICKAXE:
				return true;
		}
		return false;
	}

}