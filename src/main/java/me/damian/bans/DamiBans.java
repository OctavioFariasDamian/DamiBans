package me.damian.bans;

import com.supreme.bot.SupremeBot;
import lombok.Getter;
import me.damian.bans.commands.*;
import me.damian.bans.listeners.PlayerListeners;
import me.damian.bans.listeners.MuteListener;
import me.damian.bans.managers.DataManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamiBans extends JavaPlugin {

    public static String prefix = "#FF0000&lM#FF170C&lo#FF2E18&ld#FF4624&le#FF5D30&lr#FF743C&la#FF8B48&lc#FF8B48&li#FF8B48&ló#FF8B48&ln &7» &f";
    public static TextChannel logsChannel;

    @Getter
    private static DamiBans instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        DataManager.load(getConfig());
        DataManager.scheduleExpirationsTask(this);

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new MuteListener(), this);
        registerCommands();

        if(SupremeBot.getJda().getTextChannelById(1362878828422238459L) != null) {
            DamiBans.logsChannel = SupremeBot.getJda().getTextChannelById(1362878828422238459L);
        }
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

        getCommand("ban").setExecutor(new BanCommand());
        getCommand("ban").setTabCompleter(new BanCommand());

        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("unban").setTabCompleter(new UnbanCommand());

        getCommand("checkplayer").setExecutor(new CheckPlayerCommand());
        getCommand("checkplayer").setTabCompleter(new CheckPlayerCommand());

        getCommand("punishments").setExecutor(new PunishmentsCommand());
        getCommand("punishments").setTabCompleter(new PunishmentsCommand());
    }

    public static void sendMessageToLog(String s){
        if (logsChannel != null) {
            logsChannel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(0x78ff44)
                            .setDescription(s)
                            .build()
            ).queue();
        } else {
            getInstance().getLogger().warning("No se pudo enviar el mensaje al canal de logs: " + s);
        }
    }
}
