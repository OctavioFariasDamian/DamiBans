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
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendMessage(p, "<underline:#9131de");
            sendMessage(p, "<center>#9131de&lADVERTENCIA");
            sendMessage(p, "");
            sendMessage(p, "<center>#9131de&lJugador");
            sendMessage(p, "<center>&f" + player.getName());
            sendMessage(p, "");
            sendMessage(p, "<center>#9131de&lRazón");
            sendMessage(p, "<center>&f" + reason);
            sendMessage(p, "");
            sendMessage(p, "<center>#9131de&lStaff");
            sendMessage(p, "<center>&f" + (sender instanceof Player ? sender.getName() : "Consola"));
            sendMessage(p, "<underline:#9131de");

        }

        sendMessage(Bukkit.getConsoleSender(), "<underline:#9131de");
        sendMessage(Bukkit.getConsoleSender(), "<center>#9131de&lADVERTENCIA");
        sendMessage(Bukkit.getConsoleSender(), "");
        sendMessage(Bukkit.getConsoleSender(), "<center>#9131de&lJugador");
        sendMessage(Bukkit.getConsoleSender(), "<center>&f" + player.getName());
        sendMessage(Bukkit.getConsoleSender(), "");
        sendMessage(Bukkit.getConsoleSender(), "<center>#9131de&lRazón");
        sendMessage(Bukkit.getConsoleSender(), "<center>&f" + reason);
        sendMessage(Bukkit.getConsoleSender(), "");
        sendMessage(Bukkit.getConsoleSender(), "<center>#9131de&lStaff");
        sendMessage(Bukkit.getConsoleSender(), "<center>&f" + (sender instanceof Player ? sender.getName() : "Consola"));
        sendMessage(Bukkit.getConsoleSender(), "<underline:#9131de");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(
                    Arrays.stream(sender.getServer().getOfflinePlayers()).map(OfflinePlayer::getName).toList(), args[0]
            );
        } else if (args.length > 1) {
            return List.of("<Razón...>");
        }
        return List.of();
    }
}
