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
            String kickMessage = "#FFFF00&lS#FFFC0C&lu#FFF818&lp#FFF524&lr#FFF230&le#FFEE3C&lm#FFEB48&le#40FAF5&lM#00F5FF&lC\n\n" +
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

