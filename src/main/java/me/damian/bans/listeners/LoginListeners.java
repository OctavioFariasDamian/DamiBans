package me.damian.bans.listeners;

import me.damian.bans.managers.DataManager;
import me.damian.bans.model.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.time.Duration;
import java.time.LocalDateTime;

import static me.damian.core.DamiUtils.colorize;

public class LoginListeners implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();

        if(DataManager.getFirstBanPunishment(p.getName()) != null){
            Punishment pu = DataManager.getFirstBanPunishment(p.getName());
            assert pu != null;
            String expiresString = pu.isPermanent() ? "Nunca" : DataManager.getReamingTime(pu.getExpiresAt());
            String kickMessage = "&x&F&F&F&2&0&0&lS&x&F&F&F&5&3&0&lu&x&F&F&F&7&6&0&lp&x&F&F&F&9&9&0&lr&x&F&F&F&A&A&0&le&x&F&F&F&9&9&0&lm&x&F&F&F&7&6&0&le&x&8&0&F&7&F&F&lM&x&0&0&E&F&F&F&lC\n\n" +
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
}

