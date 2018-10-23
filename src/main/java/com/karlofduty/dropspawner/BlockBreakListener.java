package com.karlofduty.dropspawner;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener
{
    @EventHandler (priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event)
    {
        //Abort if not mob spawner
        if(!event.getBlock().getType().equals(Material.MOB_SPAWNER))
        {
            return;
        }

        //Abort if cancelled
        if (event.isCancelled())
        {
            return;
        }

        //Abort if not the required tool
        if(DropSpawner.config.getBoolean("require-pickaxe") && !isPickaxe(event.getPlayer().getItemInHand().getType()))
        {
            DropSpawner.log(event.getPlayer().getName() + " broke a spawner but did not use a pickaxe.");
            return;
        }

        //Abort if not the required enchant
        if(DropSpawner.config.getBoolean("require-silktouch") && !event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
        {
            DropSpawner.log(event.getPlayer().getName() + " broke a spawner but did not use silk touch.");
            return;
        }

        // Abort if the player does not have permission to get spawner drops
        if(!event.getPlayer().hasPermission("dropspawner.allowdrop"))
        {
            DropSpawner.log(event.getPlayer().getName() + " broke a spawner but did not have permission to drop it.");
            return;
        }

        event.setExpToDrop(0);

        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.MOB_SPAWNER, 1));
        DropSpawner.log(event.getPlayer().getName() + " broke a spawner succesfully.");

        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
        EntityType spawnerType = spawner.getSpawnedType();
        if(!spawnerType.equals(EntityType.UNKNOWN) && !spawnerType.equals(EntityType.PIG))
        {
            ItemStack mobEggToSpawn = new ItemStack(Material.MONSTER_EGG, 1, spawnerType.getTypeId());
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), mobEggToSpawn);
            DropSpawner.log(event.getPlayer().getName() + " got a spawn egg.");
        }

        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
    }
    private boolean isPickaxe(Material material)
    {
        return (material.equals(Material.DIAMOND_PICKAXE) || material.equals(Material.IRON_PICKAXE) 
        || material.equals(Material.STONE_PICKAXE) || material.equals(Material.WOOD_PICKAXE));
    }
}
