package com.karlofduty.dropspawner;

import org.bukkit.plugin.java.JavaPlugin;


public final class DropSpawner extends JavaPlugin {

	@Override
	public void onLoad() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
	}

}