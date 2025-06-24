package me.damian.bans.menu;

import me.damian.core.menu.AbstractMenu;
import me.damian.core.menu.ActionMenuItem;
import me.damian.core.menu.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BanHackingMenu extends AbstractMenu {

    private final String playerName;

    public BanHackingMenu(String playerName) {
        super("&0&nBaneo por Hacking", 36);
        this.playerName = playerName;
    }

    @Override
    protected void setupMenuItems() {
        // Fly - #00e1ff
        setItem(10, new ActionMenuItem(ItemBuilder.of(Material.FEATHER)
                .withName("#00e1ffFly")
                .withLore(
                        "#00e1ff▎ &fUsar el modo de vuelo",
                        "#00e1ff▎ &fEjemplo: Volar sin permiso",
                        "",
                        "&f» #00e1ffClick para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 7d Hacking - Fly");
            player.closeInventory();
        }));

        // KillAura - #ff0000
        setItem(11, new ActionMenuItem(ItemBuilder.of(Material.DIAMOND_SWORD)
                .withName("#ff0000KillAura")
                .withLore(
                        "#ff0000▎ &fUsar KillAura o similares",
                        "#ff0000▎ &fEjemplo: Atacar a jugadores sin apuntar",
                        "",
                        "&f» #ff0000Click para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 14d Hacking - KillAura");
            player.closeInventory();
        }));

        // X-Ray - #ffe600
        setItem(12, new ActionMenuItem(ItemBuilder.of(Material.DIAMOND_ORE)
                .withName("#ffe600X-Ray")
                .withLore(
                        "#ffe600▎ &fUsar X-Ray para ver a través de bloques",
                        "#ffe600▎ &fEjemplo: Encontrar diamantes fácilmente",
                        "",
                        "&f» #ffe600Click para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 7d Hacking - X-Ray");
            player.closeInventory();
        }));

        // Chest ESP - #0099ff
        setItem(13, new ActionMenuItem(ItemBuilder.of(Material.CHEST)
                .withName("#0099ffChest ESP")
                .withLore(
                        "#0099ff▎ &fDetectar cofres a través de las paredes",
                        "#0099ff▎ &fEjemplo: Encontrar una base de un jugador con",
                        "#0099ff▎ &fdicha ventajas.",
                        "",
                        "&f» #0099ffClick para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 14d Hacking - Chest ESP");
            player.closeInventory();
        }));

        // Aimbot - #bdbdbd
        setItem(14, new ActionMenuItem(ItemBuilder.of(Material.IRON_BLOCK)
                .withName("#bdbdbdAimbot")
                .withLore(
                        "#bdbdbd▎ &fApuntar automáticamente a jugadores.",
                        "#bdbdbd▎ &fEjemplo: Matar con criticos sin necesidad de apuntar.",
                        "",
                        "&f» #bdbdbdClick para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 14d Hacking - Aimbot");
            player.closeInventory();
        }));

        // Reach - #00fff7
        setItem(15, new ActionMenuItem(ItemBuilder.of(Material.IRON_SWORD)
                .withName("#00fff7Reach")
                .withLore(
                        "#00fff7▎ &fAlcanzar un mayor rango de ataque y AIM.",
                        "#00fff7▎ &fEjemplo: Atacar a un jugador desde más de 7 bloques.",
                        "",
                        "&f» #00fff7Click para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 7d Hacking - Reach");
            player.closeInventory();
        }));

        // Anti Knockback - #ff9900
        setItem(16, new ActionMenuItem(ItemBuilder.of(Material.STICK)
                .withName("#ff9900Anti Knockback")
                .withLore(
                        "#ff9900▎ &fEvitar ser empujado a través de los golpes.",
                        "#ff9900▎ &fEjemplo: Empujar con Empuje II y no se haya movido",
                        "#ff9900▎ &fdespues de muchos golpes.",
                        "",
                        "&f» #ff9900Click para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 14d Hacking - Anti-KB");
            player.closeInventory();
        }));

        // Speed - #00ff2a
        setItem(21, new ActionMenuItem(ItemBuilder.of(Material.FEATHER)
                .withName("#00ff2aSpeed")
                .withLore(
                        "#00ff2a▎ &fRecorrer velocidades más rápidas sin",
                        "#00ff2a▎ &fnecesidad de pociones de velocidad.",
                        "",
                        "&f» #00ff2aClick para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 14d Hacking - Speed");
            player.closeInventory();
        }));

        // Crystal Aura - #F767FF
        setItem(22, new ActionMenuItem(ItemBuilder.of(Material.END_CRYSTAL)
                .withName("#F767FFCrystal Aura")
                .withLore(
                        "#F767FF▎ &fUsar Crystal Aura para atacar a jugadores",
                        "#F767FF▎ &fcon cristales de End sin implementarse",
                        "#F767FF▎ &fmanualmente.",
                        "",
                        "&f» #F767FFClick para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 14d Hacking - Crystal Aura");
            player.closeInventory();
        }));
        // Otro - #ff00e6
        setItem(23, new ActionMenuItem(ItemBuilder.of(Material.BARRIER)
                .withName("#ff00e6Otro")
                .withLore(
                        "#ff00e6▎ &fSi la razón no está en la lista.",
                        "#ff00e6▎ &fEjemplo: Usar hacks o cheats no listados.",
                        "",
                        "&f» #ff00e6Click para seleccionar esta razón.").toItemStack(), player -> {
            player.chat("/ban " + playerName + " 7d Hacking - Sin especificar");
            player.closeInventory();
        }));
    }

    @Override
    protected void onClose(Player player) {

    }
}
