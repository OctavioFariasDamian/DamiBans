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

import java.util.List;

import static me.damian.bans.DamiBans.prefix;
import static me.damian.core.DamiUtils.*;
import static me.damian.core.DamiUtils.sendMessage;

public class MuteCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("dami-bans.mute")) {
            sendMessageWithPrefix(sender, "&cNo tienes permisos.", prefix);
            return false;
        }
        if (args.length < 2) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /mute <jugador> <duración> <razón>", prefix);
            return false;
        }

        OfflinePlayer player = sender.getServer().getOfflinePlayer(args[0]);
        String duration = args[1];

        long seconds = DataManager.convertToSeconds(duration);

        if(duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("permanente")) {
            seconds = -1;
        } else if (seconds == -1) {
            sendMessageWithPrefix(sender, "&cLa duración debe ser un número válido seguido de &cy&l, &lmo&c, &ld&c, &lh&c, &lm &co &ls&c).", prefix);
            return false;
        }

        StringBuilder sb = new StringBuilder();
        if(args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
        }
        String reason = sb.toString().trim();

        DataManager.createPunishment(
                player,
                !reason.isBlank() ? reason : "Ninguna",
                sender instanceof Player ? sender.getName() : "Consola",
                seconds == -1,
                seconds,
                PunishmentType.MUTE
        );
        sendMessageWithPrefix(sender, "&fHas silenciado a &e" + player.getName() + "&f por: &e" + (!reason.isBlank() ? reason : "Sin Razón") + " &fdurante: &e" + (seconds == -1 ? "tiempo permanente" : duration), prefix);
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendMessage(p, "<underline:#9131de");
            sendMessage(p, "<center>#9131de&lMUTEO");
            sendMessage(p, "");
            sendMessage(p, "<center>#9131de&lJugador");
            sendMessage(p, "<center>&f" + player.getName());
            sendMessage(p, "");
            sendMessage(p, "<center>#9131de&lRazón");
            sendMessage(p, "<center>&f" + reason);
            sendMessage(p, "");
            sendMessage(p, "<center>#9131de&lDuración");
            sendMessage(p, "<center>&f" + (seconds == -1 ? "Permanente" : duration));
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
            return filterSuggestions(sender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList(), args[0]);
        } else if (args.length == 2) {
            return filterSuggestions(List.of("30s", "1m", "5m", "10m", "30m", "1h", "2h", "6h", "12h", "1d", "3d", "7d", "30d", "permanent", "permanente"), args[1]);
        }else if (args.length > 2) {
            return List.of("<Razón...>");
        }
        return List.of();
    }
}
