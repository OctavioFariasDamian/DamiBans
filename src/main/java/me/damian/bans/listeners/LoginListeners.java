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
            String expiresString;
            if (pu.isPermanent()) {
                expiresString = "Nunca :>";
            } else {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiresAt = pu.getExpiresAt();
                Duration duration = Duration.between(now, expiresAt);
                if (duration.isNegative()) {
                    expiresString = "Expirado";
                } else {
                    long totalSeconds = duration.getSeconds();
                    long years = (long) (totalSeconds / (60 * 60 * 24 * 365.25));
                    totalSeconds %= (60 * 60 * 24 * 30 * 12);
                    long months = (long) (totalSeconds / (60 * 60 * 24 * 30.4375));
                    totalSeconds %= (60 * 60 * 24 * 30);
                    long days = totalSeconds / (60 * 60 * 24);
                    totalSeconds %= (60 * 60 * 24);
                    long hours = totalSeconds / (60 * 60);
                    totalSeconds %= (60 * 60);
                    long minutes = totalSeconds / 60;
                    long seconds = totalSeconds % 60;
                    expiresString = String.format("%dy %dmo %dd %dh %dm %ds", years, months, days, hours, minutes, seconds);
                }
            }
            String kickMessage = "#FFFF00&lS#FFFC0C&lu#FFF818&lp#FFF524&lr#FFF230&le#FFEE3C&lm#FFEB48&le#40FAF5&lM#00F5FF&lC\n\n" +
                    "&cHas sido baneado del servidor.\n\n\n"+
                    "&cRazón: &f" + pu.getReason() + "\n" +
                    "&cStaff: &f" + pu.getStaff() + "\n" +
                    "&cFecha: &f" + pu.getCreatedAt().toLocalDate() + "\n" +
                    "&cExpira: &f" + expiresString + "\n\n"+
                    "&cSi crees que esto es un error, contacta con el staff del servidor." +
                    "\n&c¡Gracias por tu comprensión!";

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, colorize(kickMessage));
        }
    }
}

