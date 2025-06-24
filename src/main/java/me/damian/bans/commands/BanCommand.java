package me.damian.bans.commands;

import me.damian.bans.managers.DataManager;
import me.damian.bans.menu.BanMenu;
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
import static me.damian.core.DamiUtils.filterSuggestions;

public class BanCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("dami-bans.ban")) {
            sendMessageWithPrefix(sender, "&cNo tienes permisos para banear jugadores.", prefix);
            return false;
        }

        if (args.length == 0) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /ban <jugador> <duración> <razón>", prefix);
            return false;
        }

        OfflinePlayer player;

        if(args.length == 1){
            if(!(sender instanceof Player)) {
                sendMessageWithPrefix(sender, "&cDebes especificar una duración.", prefix);
                return false;
            }

            player = sender.getServer().getOfflinePlayer(args[0]);
            if(!player.hasPlayedBefore()) {
                sendMessageWithPrefix(sender, "&cEl jugador no existe o nunca ha jugado en el servidor.", prefix);
                return false;
            }

            if (!DataManager.getPlayerPunishmentsForType(player.getName(), PunishmentType.BAN).isEmpty()) {
                sendMessageWithPrefix(sender, "&cEl jugador ya está baneado.", prefix);
                return false;
            }

            new BanMenu(player.getName()).open((Player) sender);
            return false;
        }

        player = sender.getServer().getOfflinePlayer(args[0]);

        if (!player.hasPlayedBefore()) {
            sendMessageWithPrefix(sender, "&cEl jugador no existe o nunca ha jugado en el servidor.", prefix);
            return false;
        }

        if (!DataManager.getPlayerPunishmentsForType(player.getName(), PunishmentType.BAN).isEmpty()) {
            sendMessageWithPrefix(sender, "&cEl jugador ya está baneado.", prefix);
            return false;
        }

        String duration = args[1];

        long seconds = DataManager.convertToSeconds(duration);

        if (duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("permanente")) {
            seconds = -1;
        } else if (seconds == -1) {
            sendMessageWithPrefix(sender, "&cLa duración debe ser un número válido seguido de &cy&l, &lmo&c, &ld&c, &lh&c, &lm &co &ls&c).", prefix);
            return false;
        }

        StringBuilder sb = new StringBuilder();
        if (args.length > 2) {
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
                PunishmentType.BAN
        );

        sendMessageWithPrefix(sender, "&fHas baneado a &e" + player.getName() + "&f por: &e" + (!reason.isBlank() ? reason : "Sin Razón") + " &fdurante: &e" + (seconds == -1 ? "tiempo permanente" : duration), prefix);
        Bukkit.broadcastMessage(colorize("#C96FFF&m&l*                                     *"));
        Bukkit.broadcastMessage(colorize("        #C96FFF&lBANEO "+
                (seconds == -1 ? "&cPERMA" : "")));
        Bukkit.broadcastMessage(colorize(""));
        Bukkit.broadcastMessage(colorize("        #C96FFFJugador"));
        Bukkit.broadcastMessage(colorize("         &7» &f" + player.getName()));
        Bukkit.broadcastMessage(colorize("        #C96FFFRazón"));
        Bukkit.broadcastMessage(colorize("         &7» &f" + reason+" ("+DataManager.getPunishmentCountForReasonAndType(player.getName(), reason, PunishmentType.WARN)+"/5)"));
        if(seconds != -1){
            Bukkit.broadcastMessage(colorize("        #C96FFFDuración"));
            Bukkit.broadcastMessage(colorize("         &7» &f" + duration));
        }
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
        } else if (args.length == 2) {
            return filterSuggestions(List.of("30s", "1m", "5m", "10m", "30m", "1h", "2h", "6h", "12h", "1d", "3d", "7d", "30d", "permanent", "permanente"), args[1]);
        } else if (args.length > 2) {
            return List.of("<Razón...>");
        }
        return List.of();
    }
}
