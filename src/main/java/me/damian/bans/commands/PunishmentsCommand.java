package me.damian.bans.commands;

import me.damian.bans.managers.DataManager;
import me.damian.bans.model.Punishment;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static me.damian.bans.DamiBans.prefix;
import static me.damian.core.DamiUtils.*;

public class PunishmentsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("dami-bans.punishments")) {
            sendMessageWithPrefix(sender, "&cNo tienes permisos para ver las sanciones.", prefix);
            return false;
        }

        if (args.length < 1) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /punishments <jugador>", prefix);
            return false;
        }

        String playerName = args[0];
        List<Punishment> punishments = DataManager.getPlayerPunishments(playerName);

        if (punishments.isEmpty()) {
            sendMessageWithPrefix(sender, "&cEl jugador &e" + playerName + "&c no tiene sanciones registradas.", prefix);
            return false;
        }

        sendMessageWithPrefix(sender, "&fSanciones del jugador &e" + playerName + ":", prefix);
        for (Punishment p : punishments) {
            String message = "&7 > &fID: &e" + p.getId() + " &f| &cTipo: &e"+ StringUtils.capitalise(p.getType().name().toLowerCase())+" &f| &cRazón: &e" + p.getReason() + " &f| &cStaff: &e" + p.getStaff() + " &f| &cFecha: &e" + p.getCreatedTimeFormatted()+ " UTC &f| &cExpira: &e" + (p.isPermanent() ? "Nunca" : DataManager.getReamingTime(p.getExpiresAt()));
            sendMessage(sender, message);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(
                    Arrays.stream(commandSender.getServer().getOfflinePlayers()).map(OfflinePlayer::getName).toList(), args[0]
            );
        }
        return List.of();
    }
}
