package me.damian.bans.commands;

import me.damian.bans.DamiBans;
import me.damian.bans.managers.DataManager;
import me.damian.bans.model.Punishment;
import me.damian.bans.model.PunishmentType;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.bans.DamiBans.prefix;
import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class UnmuteCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("dami-bans.unmute")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos para desmutear a jugadores.", prefix);
            return false;
        }
        if(args.length < 1) {
            sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /unmute <jugador>", prefix);
            return false;
        }

        String playerName = args[0];
        if (!DataManager.unmutePlayer(playerName)) {
            sendMessageWithPrefix(commandSender, "&cEl jugador &e" + playerName + "&c no está silenciado o no existe.", prefix);
            return false;
        }

        sendMessageWithPrefix(commandSender, "&fHas desmuteado al jugador &e" + playerName, prefix);
        DamiBans.sendMessageToLog("**"+playerName+"** ha sido desmuteado por **"+(commandSender instanceof Player ? commandSender.getName() : "Consola")+"**");

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 1) {
            return filterSuggestions(
                    DataManager.getAllPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.MUTE)
                            .map(Punishment::getPlayer)
                            .map(OfflinePlayer::getName).toList(), args[0]
            );
        }
        return List.of();
    }
}
