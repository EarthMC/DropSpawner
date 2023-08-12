package com.karlofduty.dropspawner;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener
{
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        //Abort if not a mob spawner
        if (!event.getBlock().getType().equals(Material.SPAWNER))
            return;

        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            DropSpawner.log(event.getPlayer().getName() + " broke a spawner but was in creative mode.");
            return;
        }

        //Abort if not the required tool
        if(DropSpawner.config.getBoolean("require-pickaxe") && !isPickaxe(event.getPlayer().getInventory().getItemInMainHand().getType()))
        {
            DropSpawner.log(event.getPlayer().getName() + " broke a spawner at " + formatLocation(event.getBlock().getLocation()) + ", but did not use a pickaxe.");
            return;
        }

        //Abort if not the required enchant
        if (DropSpawner.config.getBoolean("require-silktouch") && !event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            DropSpawner.log(event.getPlayer().getName() + " broke a spawner at " + formatLocation(event.getBlock().getLocation()) + ", but did not use silk touch.");
            return;
        }

        // Abort if the player does not have permission to get spawner drops
        if(!event.getPlayer().hasPermission("dropspawner.allowdrop"))
        {
            DropSpawner.log(event.getPlayer().getName() + " broke a spawner at " + formatLocation(event.getBlock().getLocation()) + ", but did not have permission to drop it.");
            return;
        }

        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.SPAWNER, 1));
        DropSpawner.log(event.getPlayer().getName() + " broke a spawner succesfully at " + formatLocation(event.getBlock().getLocation()) + ".");

        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
        EntityType spawnerType = spawner.getSpawnedType();
        if (spawnerType != null && !spawnerType.equals(EntityType.PIG)) {
            ItemStack mobEggToSpawn = new ItemStack(Material.valueOf(spawnerType + "_SPAWN_EGG"), 1);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), mobEggToSpawn);
            DropSpawner.log(event.getPlayer().getName() + " got a spawn egg.");
        }

        event.setExpToDrop(0);
        event.setDropItems(false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSpawnerPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.SPAWNER || !DropSpawner.config.getBoolean("new-spawners-are-pig-type", true))
            return;

        if (event.getBlock().getState() instanceof CreatureSpawner spawner) {
            spawner.setSpawnedType(EntityType.PIG);
            spawner.update();
        }
    }

    private boolean isPickaxe(Material material) {
        switch(material) {
            case WOODEN_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLDEN_PICKAXE:
            case DIAMOND_PICKAXE:
            case NETHERITE_PICKAXE:
                return true;
            default:
                return false;
        }
    }

    private String formatLocation(Location location) {
        String world = location.getWorld() == null ? "null" : location.getWorld().getName();
        return "{world=" + world + ", x=" + location.getBlockX() + ", y=" + location.getBlockY() + ", z=" + location.getBlockZ() + "}";
    }
}
