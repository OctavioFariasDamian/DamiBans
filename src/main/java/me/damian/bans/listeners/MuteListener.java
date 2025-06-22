package me.damian.bans.listeners;

import me.damian.bans.DamiBans;
import me.damian.bans.managers.DataManager;
import me.damian.bans.model.PunishmentType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class MuteListener implements Listener {
    final List<String> mutedCommands = List.of(
            "/msg",
            "/tell",
            "/whisper",
            "/r",
            "/reply"
    );

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        if(command.split(" ")[0].equalsIgnoreCase("/kingdoms") || command.split(" ")[0].equalsIgnoreCase("/kingdom") ||
            command.split(" ")[0].equalsIgnoreCase("/k")) {
            if(command.split(" ").length > 1 && command.split(" ")[1].equalsIgnoreCase("chat")) {
                event.setCancelled(true);
                sendMessageWithPrefix(event.getPlayer(), "&cEstás silenciado y no puedes hablar en el chat de tu reino.", DamiBans.prefix);
            }
        }
        if (mutedCommands.contains(command.split(" ")[0])) {
            if (!DataManager.getPlayerPunishmentsForType(event.getPlayer().getName(), PunishmentType.MUTE).isEmpty()) {
                event.setCancelled(true);
                sendMessageWithPrefix(event.getPlayer(), "&cEstás silenciado y no puedes hablar por susurro.", DamiBans.prefix);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e){
        if(e.isCancelled()) return;
        if (!DataManager.getPlayerPunishmentsForType(e.getPlayer().getName(), PunishmentType.MUTE).isEmpty()) {
            e.setCancelled(true);
            sendMessageWithPrefix(e.getPlayer(), "&cEstás silenciado y no puedes hablar en el chat.", DamiBans.prefix);
        }
    }
}
