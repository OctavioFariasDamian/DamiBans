package me.damian.bans.commands;

import me.damian.bans.DamiBans;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.*;

public class KickCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!sender.hasPermission("dami-bans.kick")){
            sendMessageWithPrefix(sender, "&cNo tienes permisos.", DamiBans.prefix);
            return false;
        }
        if (args.length < 2) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /kick <jugador> <razón>", DamiBans.prefix);
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        String reason = sb.toString().trim();
        if (player.isOnline()) {
            String kickMessage = "&x&F&F&F&2&0&0&lS&x&F&F&F&5&3&0&lu&x&F&F&F&7&6&0&lp&x&F&F&F&9&9&0&lr&x&F&F&F&A&A&0&le&x&F&F&F&9&9&0&lm&x&F&F&F&7&6&0&le&x&8&0&F&7&F&F&lM&x&0&0&E&F&F&F&lC\n\n" +
                    "&cHas sido expulsado del servidor.\n\n\n"+
                    "&cRazón: &f" + reason + "\n" +
                    "&cStaff: &f" + (sender instanceof Player ? sender.getName() : "Consola");
            player.getPlayer().kickPlayer(colorize(kickMessage));
            sendMessageWithPrefix(sender, "&fHas expulsado a &e" + player.getName() + "&f por: &e" + reason, DamiBans.prefix);
        } else {
            sendMessageWithPrefix(sender, "&cEl jugador no está conectado.", DamiBans.prefix);
            return false;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList(), args[0]);
        }else if (args.length > 1) {
            return List.of("<Razón...>");
        }
        return List.of();
    }
}
