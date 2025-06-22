package me.damian.bans;

import lombok.Getter;
import me.damian.bans.commands.*;
import me.damian.bans.listeners.LoginListeners;
import me.damian.bans.listeners.MuteListener;
import me.damian.bans.managers.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamiBans extends JavaPlugin {

    public static String prefix = "#FF0000&lM#FF170C&lo#FF2E18&ld#FF4624&le#FF5D30&lr#FF743C&la#FF8B48&lc#FF8B48&li#FF8B48&ló#FF8B48&ln &7» &f";

    @Getter
    private static DamiBans instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        DataManager.load(getConfig());
        DataManager.scheduleExpirationsTask(this);

        getServer().getPluginManager().registerEvents(new LoginListeners(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        registerCommands();
    }

    @SuppressWarnings("DataFlowIssue")
    private void registerCommands() {
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("kick").setTabCompleter(new KickCommand());

        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("warn").setTabCompleter(new WarnCommand());

        getCommand("warns").setExecutor(new WarnsCommand());
        getCommand("warns").setTabCompleter(new WarnsCommand());

        getCommand("deletewarn").setExecutor(new DeleteWarnCommand());
        getCommand("deletewarn").setTabCompleter(new DeleteWarnCommand());

        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("mute").setTabCompleter(new MuteCommand());

        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("unmute").setTabCompleter(new UnmuteCommand());
    }

    @Override
    public void onDisable() {

    }
}
