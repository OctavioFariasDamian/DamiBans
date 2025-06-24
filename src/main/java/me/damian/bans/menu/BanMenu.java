package me.damian.bans.menu;

import it.unimi.dsi.fastutil.Pair;
import me.damian.core.menu.AbstractMenu;
import me.damian.core.menu.ActionMenuItem;
import me.damian.core.menu.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static me.damian.core.DamiUtils.sendMessage;
import static me.damian.core.DamiUtils.sendTitle;

public class BanMenu extends AbstractMenu {

    private final String playerName;
    public static final Map<Player, String> otherReasons = new HashMap<>();
    public static final Map<Player, Pair<String, String>> durationInput = new HashMap<>();

    public BanMenu(String playerName) {
        super("&0&nSeleccione una razón&r", 27);
        this.playerName = playerName;
    }

    @Override
    protected void setupMenuItems() {
        setItem(10,
                new ActionMenuItem(ItemBuilder.of(Material.NETHERITE_SWORD)
                    .withName("#00e1ffHacking")
                    .withLore(
                            "#00e1ff▎ &fUsar hacks o cheats",
                            "#00e1ff▎ &fEjemplo: KillAura, X-Ray, etc.",
                            "",
                            "&f» #00e1ffClick para elegir una razón con exactitud.").toItemStack(),
                    player -> {
                        new BanHackingMenu(playerName).open(player);
                    }));

        setItem(11,
                new ActionMenuItem(ItemBuilder.of(Material.BARRIER)
                    .withName("#ff9900Spam")
                    .withLore(
                            "#ff9900▎ &fEnviar mensajes repetitivos o molestos",
                            "#ff9900▎ &fEjemplo: Promoción, publicidad, etc.",
                            "",
                            "&f» #ff9900Click para seleccionar esta razón.").toItemStack(),
                    player -> {
                        player.chat("/ban "+playerName+" 3d Spam");
                        player.closeInventory();
                    }));

        setItem(12,
                new ActionMenuItem(ItemBuilder.of(Material.TNT)
                    .withName("#ffe600Flood")
                    .withLore(
                            "#ffe600▎ &fEnviar mensajes masivos o excesivos",
                            "#ffe600▎ &fEjemplo: Mensajes repetidos, spam masivo, etc.",
                            "",
                            "&f» #ffe600Click para seleccionar esta razón.").toItemStack(),
                    player -> {
                        player.chat("/ban "+playerName+" 3d Flood");
                        player.closeInventory();
                    }));

        setItem(13,
                new ActionMenuItem(ItemBuilder.of(Material.BEDROCK)
                    .withName("#ff0000Toxicidad")
                    .withLore(
                            "#ff0000▎ &fComportamiento tóxico o abusivo",
                            "#ff0000▎ &fEjemplo: Insultos, acoso, etc.",
                            "",
                            "&f» #ff0000Click para seleccionar esta razón.").toItemStack(),
                    player -> {
                        player.chat("/ban "+playerName+" 3d Toxicidad");
                        player.closeInventory();
                    }));

        setItem(14,
                new ActionMenuItem(ItemBuilder.of(Material.GOLDEN_AXE)
                    .withName("#bdbdbdMal Comportamiento")
                    .withLore(
                            "#bdbdbd▎ &fComportamiento inapropiado o irrespetuoso",
                            "#bdbdbd▎ &fEjemplo: Lenguaje ofensivo, actitudes negativas, etc.",
                            "",
                            "&f» #bdbdbdClick para seleccionar esta razón.").toItemStack(),
                    player -> {
                        player.chat("/ban "+playerName+" 1d Mal Comportamiento");
                        player.closeInventory();
                    }));

        setItem(15,
                new ActionMenuItem(ItemBuilder.of(Material.NETHERITE_BLOCK)
                    .withName("#00ff2aAbuso de Bug")
                    .withLore(
                            "#00ff2a▎ &fExplotar errores o fallos del juego",
                            "#00ff2a▎ &fEjemplo: Usar glitches para obtener ventaja.",
                            "",
                            "&f» #00ff2aClick para seleccionar esta razón.").toItemStack(),
                    player -> {
                        player.chat("/ban "+playerName+" 2d Abuso de Bug");
                        player.closeInventory();
                    }));
        setItem(16,
                new ActionMenuItem(ItemBuilder.of(Material.PAPER)
                    .withName("#ff00e6Otro")
                    .withLore(
                            "#ff00e6▎ &fAlguna otra razón no especificada",
                            "#ff00e6▎ &fEjemplo: Cualquier otra razón que no encaje en las anteriores.",
                            "",
                            "&f» #ff00e6Click para seleccionar esta razón.").toItemStack(),
                    player -> {
                        player.closeInventory();
                        sendMessage(player, "");
                        sendMessage(player, "<center>&a&lINGRESA LA RAZÓN");
                        sendMessage(player, "<center>&fEscribe la razón por la cual quieres banear");
                        sendMessage(player, "<center>&fa ese jugador.");
                        sendMessage(player, "");

                        sendTitle(player, "&a&lIngresa la razón", "&7Escribe en el chat la razón del ban", 20, 300, 20);
                        otherReasons.put(player, playerName);
                    }));
    }

    @Override
    protected void onClose(Player player) {

    }

}
