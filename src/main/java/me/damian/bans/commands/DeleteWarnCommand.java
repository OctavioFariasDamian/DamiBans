package me.damian.bans.commands;

import me.damian.bans.managers.DataManager;
import me.damian.bans.model.Punishment;
import me.damian.bans.model.PunishmentType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;
import static me.damian.bans.DamiBans.prefix;

public class DeleteWarnCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("dami-bans.delete-warn")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos para eliminar advertencias.", prefix);
            return false;
        }
        if(args.length < 1) {
            sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /delwarn <ID>", prefix);
            return false;
        }

        try {
            int id = Integer.parseInt(args[0]);
            if (id < 0) {
                sendMessageWithPrefix(commandSender, "&cEl ID debe ser un número positivo.", prefix);
                return false;
            }
            if (!DataManager.deleteWarn(id)) {
                sendMessageWithPrefix(commandSender, "&cNo se encontró ninguna advertencia con el ID: " + id, prefix);
                return false;
            }
            sendMessageWithPrefix(commandSender, "&fHas eliminado la advertencia con ID: &e" + id, prefix);
        } catch (NumberFormatException e) {
            sendMessageWithPrefix(commandSender, "&cEl ID debe ser un número válido.", prefix);
            return false;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 1){
            return filterSuggestions(
                    DataManager.getAllPunishments()
                            .stream().filter(punishment -> punishment.getType() == PunishmentType.WARN)
                            .map(Punishment::getId)
                            .map(String::valueOf)
                            .toList(), args[0]
            );
        }
        return List.of();
    }
}
