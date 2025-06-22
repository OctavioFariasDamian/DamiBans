package me.damian.bans.managers;

import me.damian.bans.DamiBans;
import me.damian.bans.model.Punishment;
import me.damian.bans.model.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.damian.bans.DamiBans.prefix;
import static me.damian.core.DamiUtils.colorize;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class DataManager {

    private static final List<Punishment> punishments = new ArrayList<>();
    private static int nextId = 0;

    public static void load(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        punishments.clear();
        int maxId = 0;
        for (String key : section.getKeys(false)) {
            ConfigurationSection punishmentSection = section.getConfigurationSection(key);
            if (punishmentSection != null) {
                Punishment punishment = Punishment.fromConfig(punishmentSection);
                punishments.add(punishment);
                int id = Integer.parseInt(key);
                if (id >= maxId) {
                    maxId = id + 1;
                }
            }
        }
        nextId = maxId;
    }

    public static List<Punishment> getPlayerPunishments(String playerName) {
        List<Punishment> playerPunishments = new ArrayList<>();
        for (Punishment punishment : punishments) {
            if (Objects.requireNonNull(punishment.getPlayer().getName()).equals(playerName)) {
                playerPunishments.add(punishment);
            }
        }
        return playerPunishments;
    }

    public static List<Punishment> getPlayerPunishmentsForType(String playerName, PunishmentType type) {
        List<Punishment> playerPunishments = new ArrayList<>();
        for (Punishment punishment : punishments) {
            if (Objects.requireNonNull(punishment.getPlayer().getName()).equals(playerName) && punishment.getType() == type) {
                playerPunishments.add(punishment);
            }
        }
        return playerPunishments;
    }

    public static int getPunishmentCount(String playerName) {
        return getPlayerPunishments(playerName).size();
    }

    public static int getPunishmentCountForReason(String playerName, String reason) {
        int count = 0;
        for (Punishment punishment : getPlayerPunishments(playerName)) {
            if (punishment.getReason().equalsIgnoreCase(reason)) {
                count++;
            }
        }
        return count;
    }

    public static int getPunishmentCountForReasonAndType(String playerName, String reason, PunishmentType type) {
        int count = 0;
        for (Punishment punishment : getPlayerPunishments(playerName)) {
            if (punishment.getReason().equalsIgnoreCase(reason) && punishment.getType() == type) {
                count++;
            }
        }
        return count;
    }

    public static List<Punishment> getAllPunishments() {
        return new ArrayList<>(punishments);
    }

    @SuppressWarnings("deprecation")
    public static void createPunishment(OfflinePlayer player, String reason, String staff, boolean permanent, long secondsDuration, PunishmentType type) {
        int id = nextId++;
        LocalDateTime now = LocalDateTime.now();

        Punishment punishment = new Punishment(
                id,
                player,
                reason,
                staff,
                permanent,
                now,
                permanent ? null : now.plusSeconds(secondsDuration),
                type
        );
        punishments.add(punishment);

        ConfigurationSection section = DamiBans.getInstance().getConfig().createSection(String.valueOf(id));
        section.set("player", player.getName());
        section.set("reason", reason);
        section.set("staff", staff);
        section.set("permanent", permanent);
        section.set("createdAt", now.toString());

        if (!permanent) {
            section.set("expiresAt", now.plusSeconds(secondsDuration).toString());
        }

        section.set("type", type.name());
        DamiBans.getInstance().saveConfig();

        if(player.isOnline() && player.getPlayer() != null && type == PunishmentType.BAN) {
            String expiresString = permanent ? "Nunca" : getReamingTime(punishment.getExpiresAt());
            String kickMessage = "#FFFF00&lS#FFFC0C&lu#FFF818&lp#FFF524&lr#FFF230&le#FFEE3C&lm#FFEB48&le#40FAF5&lM#00F5FF&lC\n\n" +
                    "&cHas sido baneado del servidor.\n\n\n"+
                    "&cRazón: &f" + punishment.getReason() + "\n" +
                    "&cStaff: &f" + punishment.getStaff() + "\n" +
                    "&cFecha: &f" + punishment.getCreatedAt().toLocalDate() + "\n" +
                    "&cExpira: &f" + (expiresString != null ? expiresString : "Expirado (Espere un minuto para poder ingresar)") + "\n\n"+
                    "&cSi crees que esto es un error, contacta con el staff del servidor." +
                    "\n&c¡Gracias por tu comprensión!";

            player.getPlayer().kickPlayer(colorize(kickMessage));
        }
    }

    public static void scheduleExpirations(){
        LocalDateTime now = LocalDateTime.now();
        punishments.removeIf(punishment -> !punishment.isPermanent() && punishment.getExpiresAt() != null && punishment.getExpiresAt().isBefore(now));
    }

    public static void scheduleExpirationsTask(DamiBans plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                DataManager::scheduleExpirations,
                0L,
                20L * 60
        );
    }

    public static long convertToSeconds(String durationString) {
        long totalSeconds = 0;

        String[] parts = durationString.replace(" ", "").split(",");

        for (String part : parts) {
            if (part.isEmpty()) continue;

            String unit = part.substring(part.length() - 1);
            String valueStr = part.substring(0, part.length() - 1);

            if (unit.equals("mo")) {
                unit = part.substring(part.length() - 2);
                valueStr = part.substring(0, part.length() - 2);
            }

            long value;
            try {
                value = Long.parseLong(valueStr);
            } catch (NumberFormatException e) {
                System.err.println("Error: Valor numérico inválido en '" + part + "'");
                return -1;
            }

            switch (unit) {
                case "s":
                    totalSeconds += value;
                    break;
                case "m":
                    totalSeconds += value * 60;
                    break;
                case "d":
                    totalSeconds += value * 24 * 60 * 60;
                    break;
                case "mo":
                    totalSeconds += (long) (value * 30.4375 * 24 * 60 * 60);
                    break;
                case "y":
                    totalSeconds += (long) (value * 365.25 * 24 * 60 * 60);
                    break;
                default:
                    System.err.println("Unidad de tiempo desconocida: '" + unit + "' en '" + part + "'");
                    return -1;
            }
        }
        return totalSeconds;
    }

    public static Punishment getFirstBanPunishment(String name) {
        for (Punishment punishment : punishments) {
            if (Objects.equals(punishment.getPlayer().getName(), name) && punishment.getType() == PunishmentType.BAN) {
                return punishment;
            }
        }
        return null;
    }

    public static boolean deleteWarn(int id) {
        for (Punishment punishment : punishments) {
            if (punishment.getId() == id && punishment.getType() == PunishmentType.WARN) {
                punishments.remove(punishment);
                DamiBans.getInstance().getConfig().set(String.valueOf(id), null);
                DamiBans.getInstance().saveConfig();
                return true;
            }
        }
        return false;
    }

    public static boolean unmutePlayer(String playerName) {
        List<Punishment> playerMutes = getPlayerPunishmentsForType(playerName, PunishmentType.MUTE);
        if (playerMutes.isEmpty()) {
            return false;
        }
        Punishment mute = playerMutes.getFirst();
        punishments.remove(mute);
        DamiBans.getInstance().getConfig().set(String.valueOf(mute.getId()), null);
        DamiBans.getInstance().saveConfig();
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendMessageWithPrefix(onlinePlayer, "&fEl jugador &e" + playerName + " &fha sido desmuteado.", prefix);
        }
        return true;
    }

    public static boolean unbanPlayer(String playerName) {
        List<Punishment> playerBans = getPlayerPunishmentsForType(playerName, PunishmentType.BAN);
        if (playerBans.isEmpty()) {
            return false;
        }
        Punishment ban = playerBans.getFirst();
        punishments.remove(ban);
        DamiBans.getInstance().getConfig().set(String.valueOf(ban.getId()), null);
        DamiBans.getInstance().saveConfig();
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendMessageWithPrefix(onlinePlayer, "&fEl jugador &e" + playerName + " &fha sido desbaneado.", prefix);
        }
        return true;
    }

    public static String getReamingTime(LocalDateTime objetivo) {
        LocalDateTime ahora = LocalDateTime.now();
        if (!objetivo.isAfter(ahora)) {
            return null;
        }

        Period periodo = Period.between(ahora.toLocalDate(), objetivo.toLocalDate());
        LocalDateTime intermedio = ahora.plusYears(periodo.getYears()).plusMonths(periodo.getMonths()).plusDays(periodo.getDays());
        Duration duration = Duration.between(intermedio, objetivo);

        StringBuilder sb = new StringBuilder();
        if (periodo.getYears() > 0) sb.append(periodo.getYears()).append("año(s) ");
        if (periodo.getMonths() > 0) sb.append(periodo.getMonths()).append("mes(es) ");
        if (periodo.getDays() > 0) sb.append(periodo.getDays()).append("día(s) ");
        long horas = duration.toHours();
        if (horas > 0) sb.append(horas).append("hora(s) ");
        int minutos = duration.toMinutesPart();
        if (minutos > 0) sb.append(minutos).append("min ");
        int segundos = duration.toSecondsPart();
        if (segundos > 0) sb.append(segundos).append("seg");

        return sb.toString().trim();
    }
}
