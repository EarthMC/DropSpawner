package com.karlofduty.dropspawner;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

public class BlockBreakListener implements Listener
{
    @EventHandler
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
            return;
        }

        //Abort if not the required enchant
        if(DropSpawner.config.getBoolean("require-silktouch") && !event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
        {
            return;
        }

        // Abort if the player does not have permission to get spawner drops
        if(!event.getPlayer().hasPermission("dropspawner.allowdrop"))
        {
            return;
        }

        event.setExpToDrop(0);

        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.MOB_SPAWNER, 1));

        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
        EntityType spawnerType = spawner.getSpawnedType();
        if(!spawnerType.equals(EntityType.UNKNOWN) && !spawnerType.equals(EntityType.PIG))
        {
            ItemStack mobEggToSpawn = new ItemStack(Material.MONSTER_EGG, 1, spawnerType.getTypeId());
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), mobEggToSpawn);
        }

        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
    }
    private boolean isPickaxe(Material material)
    {
        if(material.equals(Material.DIAMOND_PICKAXE))
        {
            return true;
        }
        if(material.equals(Material.IRON_PICKAXE))
        {
            return true;
        }
        if(material.equals(Material.STONE_PICKAXE))
        {
            return true;
        }
        if(material.equals(Material.WOOD_PICKAXE))
        {
            return true;
        }
        return false;
    }
}
