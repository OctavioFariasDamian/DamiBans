package me.damian.bans.commands;

import me.damian.bans.managers.DataManager;
import me.damian.bans.model.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static me.damian.bans.DamiBans.prefix;
import static me.damian.core.DamiUtils.*;

public class WarnCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!sender.hasPermission("dami-bans.warn")){
            sendMessageWithPrefix(sender, "&cNo tienes permisos.", prefix);
            return false;
        }
        if(args.length < 2) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /warn <jugador> <razón>", prefix);
            return false;
        }
        OfflinePlayer player = sender.getServer().getOfflinePlayer(args[0]);
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();

        DataManager.createPunishment(
                player,
                reason,
                sender instanceof Player ? sender.getName() : "Consola",
                true,
                -1,
                PunishmentType.WARN
        );

        sendMessageWithPrefix(sender, "&fHas advertido a &e" + player.getName() + "&f por: &e" + reason, prefix);
        Bukkit.broadcastMessage(colorize("#C96FFF&m&l*                                     *"));
        Bukkit.broadcastMessage(colorize("        #C96FFF&lADVERTENCIA"));
        Bukkit.broadcastMessage(colorize(""));
        Bukkit.broadcastMessage(colorize("        #C96FFFJugador"));
        Bukkit.broadcastMessage(colorize("         &7» &f" + player.getName()));
        Bukkit.broadcastMessage(colorize("        #C96FFFRazón"));
        Bukkit.broadcastMessage(colorize("         &7» &f" + reason+" ("+DataManager.getPunishmentCountForReasonAndType(player.getName(), reason, PunishmentType.WARN)+"/5)"));
        Bukkit.broadcastMessage(colorize("        #C96FFFStaff"));
        Bukkit.broadcastMessage(colorize("         &7» &f" + (sender instanceof Player ? sender.getName() : "Consola")));
        Bukkit.broadcastMessage(colorize("#C96FFF&m&l*                                     *"));
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(
                    Arrays.stream(sender.getServer().getOfflinePlayers()).map(OfflinePlayer::getName).toList(), args[0]
            );
        } else if (args.length > 1) {
            return List.of("Mal Comportamiento", "Toxicidad", "Spam", "Flood");
        }
        return List.of();
    }
}
