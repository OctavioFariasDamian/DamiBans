package me.damian.bans.commands;

import me.damian.bans.managers.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.bans.DamiBans.prefix;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class UnbanCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("dami-bans.unban")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos para desbanear a jugadores.", prefix);
            return false;
        }
        if(args.length < 1) {
            sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /unban <jugador>", prefix);
            return false;
        }

        String playerName = args[0];
        if (!DataManager.unbanPlayer(playerName)) {
            sendMessageWithPrefix(commandSender, "&cEl jugador &e" + playerName + "&c no está baneado o no existe.", prefix);
            return false;
        }

        sendMessageWithPrefix(commandSender, "&fHas desbaneado al jugador &e" + playerName, prefix);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
