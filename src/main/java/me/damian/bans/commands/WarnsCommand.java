package me.damian.bans.commands;

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
import static me.damian.core.DamiUtils.*;

public class WarnsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 0){
            if(!(commandSender instanceof Player player)) {
                sendMessageWithPrefix(commandSender, "&cEste comando solo puede ser ejecutado por un jugador.", prefix);
                return false;
            }
            List<Punishment> warns = DataManager.getPlayerPunishmentsForType(player.getName(), PunishmentType.WARN);
            if (warns.isEmpty()) {
                sendMessageWithPrefix(commandSender, "&cNo tienes advertencias.", prefix);
                return false;
            }
            sendMessageWithPrefix(commandSender, "&fTus advertencias:", prefix);
            for (Punishment warn : warns) {
                String message = "&7 > &fID: &e" + warn.getId() + " &f| &cRazón: &e" + warn.getReason() + " &f| &cStaff: &e" + warn.getStaff() + " &f| &cFecha: &e" + warn.getCreatedTimeFormatted()+ " UTC";
                sendMessage(commandSender, message);
            }
            return true;
        }
        if(!commandSender.hasPermission("dami-bans.warns-others")){
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos de ver las advertencias de los demás. Para ver las tuyas has solo /warns", prefix);
            return false;
        }

        OfflinePlayer player = commandSender.getServer().getOfflinePlayer(args[0]);
        if (!player.hasPlayedBefore()) {
            sendMessageWithPrefix(commandSender, "&cEl jugador no existe o nunca ha jugado en el servidor.", prefix);
            return false;
        }

        List<Punishment> warns = DataManager.getPlayerPunishmentsForType(player.getName(), PunishmentType.WARN);
        if (warns.isEmpty()) {
            sendMessageWithPrefix(commandSender, "&cEl jugador no tiene advertencias.", prefix);
            return false;
        }
        sendMessageWithPrefix(commandSender, "&fAdvertencias de &e" + player.getName() + "&f:", prefix);
        for (Punishment warn : warns) {
            String message = "&7 > &fID: &e" + warn.getId() + " &f| &cRazón: &e" + warn.getReason() + " &f| &cStaff: &e" + warn.getStaff() + " &f| &cFecha: &e" + warn.getCreatedTimeFormatted()+" UTC";
            sendMessage(commandSender, message);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(
                    commandSender.getServer().getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                            .toList(),
                    args[0]
            );
        }
        return List.of();
    }
}
