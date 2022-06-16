package com.karlofduty.dropspawner;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class DropSpawner extends JavaPlugin
{
    public static FileConfiguration config;
    private static DropSpawner instance;

    @Override
    public void onEnable()
    {
        instance = this;
        saveDefaultConfig();
        config = this.getConfig();
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    public static DropSpawner getInstance() {
        return instance;
    }

    public static ConsoleCommandSender getConsole()
    {
        return instance.getServer().getConsoleSender();
    }

    public static void executeCommand(String command)
    {
        instance.getServer().dispatchCommand(getConsole(), command);
    }

    public static void log(String message)
    {
        instance.getLogger().info(message);
    }
    public static void logColoured(String message)
    {
        getConsole().sendMessage(message);
    }
    public static void logWarning(String message)
    {
        instance.getLogger().warning(message);
    }
}
