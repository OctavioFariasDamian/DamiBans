package me.damian.bans.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.damian.bans.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Punishment {

    private final int id;
    private final OfflinePlayer player;
    private String reason;
    private final String staff;
    private final boolean permanent;
    private final LocalDateTime createdAt;
    @Nullable
    private final LocalDateTime expiresAt;
    private final PunishmentType type;

    public static Punishment fromConfig(ConfigurationSection section) {
        int id = Integer.parseInt(section.getName());
        String pName = section.getString("player");
        assert pName != null;
        OfflinePlayer player = Bukkit.getOfflinePlayer(pName);
        String reason = section.getString("reason", "Ninguna");
        boolean permanent = section.getBoolean("permanent", true);
        String staff = section.getString("staff", "Consola");
        LocalDateTime createdAt = LocalDateTime.parse(section.getString("createdAt", LocalDateTime.now().toString()));
        LocalDateTime expiresAt = section.isSet("expiresAt") ? LocalDateTime.parse(Objects.requireNonNull(section.getString("expiresAt"))) : null;
        PunishmentType type = PunishmentType.valueOf(section.getString("type", "BAN").toUpperCase());

        return new Punishment(id, player, reason, staff, permanent, createdAt, expiresAt, type);
    }

    public String getCreatedTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        return createdAt.format(formatter);
    }
}
