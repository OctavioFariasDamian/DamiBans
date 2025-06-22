package me.damian.bans.commands;

import me.damian.bans.managers.DataManager;
import me.damian.bans.model.PunishmentType;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static me.damian.bans.DamiBans.prefix;
import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class CheckPlayerCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!commandSender.hasPermission("dami-bans.check-player")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos.", prefix);
            return false;
        }

        if (args.length < 1) {
            sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /checkplayer <jugador>", prefix);
            return false;
        }

        OfflinePlayer player = commandSender.getServer().getOfflinePlayer(args[0]);
        if(!player.hasPlayedBefore()) {
            sendMessageWithPrefix(commandSender, "&cEl jugador &e" + args[0] + "&c no ha jugado antes.", prefix);
            return false;
        }
        StringBuilder message = new StringBuilder();
        message.append("&fInformación del jugador &e").append(player.getName()).append("&f:\n");
        message.append("&7 > &fUUID: &e").append(player.getUniqueId()).append("\n");
        message.append("&7 > &fNombre: &e").append(player.getName()).append("\n");
        if (player.isOnline()) {
            message.append("&7 > &fEstado: &aEn línea\n");
        } else {
            message.append("&7 > &fEstado: &cDesconectado\n");
        }
        message.append("&7 > &fSe unió: &e").append(new java.text.SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new java.util.Date(player.getFirstPlayed()))).append(" UTC\n");
        message.append("&7 > &fÚltima conexión: &e").append(new java.text.SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new java.util.Date(player.getLastPlayed()))).append(" UTC\n");
        if(player.isOnline()){
            message.append("&7 > &fIP: &e").append(player.getPlayer().getAddress().getAddress().getHostAddress()).append("\n");
        } else {
            message.append("&7 > &fIP: &cJugador desconectado\n");
        }
        message.append("&7 > &fBaneado: &e").append(!DataManager.getPlayerPunishmentsForType(player.getName(), PunishmentType.BAN).isEmpty() ? "Sí" : "No").append("\n");
        message.append("&7 > &fSilenciado: &e").append(!DataManager.getPlayerPunishmentsForType(player.getName(), PunishmentType.MUTE).isEmpty() ? "Sí" : "No").append("\n");
        message.append("&7 > &fCant. Advertencias: &e").append(DataManager.getPlayerPunishmentsForType(player.getName(), PunishmentType.WARN).size()).append("\n");
        sendMessageWithPrefix(commandSender, message.toString(), prefix);
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
