package me.damian.bans.listeners;

import it.unimi.dsi.fastutil.Pair;
import me.damian.bans.managers.DataManager;
import me.damian.bans.menu.BanMenu;
import me.damian.bans.model.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.damian.core.DamiUtils.*;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();

        if(DataManager.getFirstBanPunishment(p.getName()) != null){
            Punishment pu = DataManager.getFirstBanPunishment(p.getName());
            assert pu != null;
            String expiresString = pu.isPermanent() ? "Nunca" : DataManager.getReamingTime(pu.getExpiresAt());
            String kickMessage = "#FFFF00&lS#FFEB00&lu#FFD700&lp#FFC200&lr#FFAE00&le#FF9A00&lm#FF8600&le#FF7100&lM#FF5D00&lC\n\n" +
                    "&cHas sido baneado del servidor.\n\n\n"+
                    "&cRazón: &f" + pu.getReason() + "\n" +
                    "&cStaff: &f" + pu.getStaff() + "\n" +
                    "&cFecha: &f" + pu.getCreatedAt().toLocalDate() + "\n" +
                    "&cExpira: &f" + (expiresString != null ? expiresString : "Expirado (Espere un minuto para poder ingresar)") + "\n\n"+
                    "&cSi crees que esto es un error, contacta con el staff del servidor." +
                    "\n&c¡Gracias por tu comprensión!";

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, colorize(kickMessage));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        BanMenu.otherReasons.remove(e.getPlayer());
        BanMenu.durationInput.remove(e.getPlayer());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if(BanMenu.otherReasons.containsKey(e.getPlayer())){
            player.clearTitle();
            BanMenu.durationInput.put(player, Pair.of(BanMenu.otherReasons.get(e.getPlayer()), e.getMessage()));
            sendMessage(player, "");
            sendMessage(player, "<center>&a&lINGRESA LA DURACIÓN");
            sendMessage(player, "<center>&fEscribe la duración del baneo con el formato:");
            sendMessage(player, "<center>&ay, mo, d, h, m o s");
            sendMessage(player, "");

            sendTitle(player, "&a&lIngresa la duración", "&7Escribe en el chat la duración del ban", 20, 300, 20);
            BanMenu.otherReasons.remove(e.getPlayer());
        }else if(BanMenu.durationInput.containsKey(e.getPlayer())){
            Bukkit.dispatchCommand(e.getPlayer(), "ban " + BanMenu.durationInput.get(e.getPlayer()).first() + " " + e.getMessage().split(" ")[0] + " " + BanMenu.durationInput.get(e.getPlayer()).second());
            BanMenu.durationInput.remove(e.getPlayer());
            player.clearTitle();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }
}

