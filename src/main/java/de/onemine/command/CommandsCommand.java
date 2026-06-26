package de.onemine.command;

import de.onemine.OneMinePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsCommand implements CommandExecutor {
    private final OneMinePlugin plugin;

    public CommandsCommand(OneMinePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Command nutzen!");
            return true;
        }

        Player player = (Player) sender;

        if (player.isOp()) {
            showOperatorCommands(player);
        } else {
            showPlayerCommands(player);
        }

        return true;
    }

    private void showPlayerCommands(Player player) {
        player.sendMessage("§6═══════════════════════════════════════");
        player.sendMessage("§b🏴‍☠️ OneMine - Spieler Commands");
        player.sendMessage("§6═══════════════════════════════════════");
        player.sendMessage("§e/menü§7 - Öffnet das Hauptmenü");
        player.sendMessage("§e/commands§7 - Zeigt alle verfügbaren Commands");
        player.sendMessage("§e/bounty§7 - Zeigt die Top-10 Kopfgelder");
        player.sendMessage("§e/stats§7 - Deine Spieler-Statistiken");
        player.sendMessage("§e/clan info§7 - Informationen über deine Bande");
        player.sendMessage("§e/clan members§7 - Zeigt Clan-Mitglieder");
        player.sendMessage("§e/insel info§7 - Info über deine Insel");
        player.sendMessage("§6═══════════════════════════════════════");
    }

    private void showOperatorCommands(Player player) {
        player.sendMessage("§6═══════════════════════════════════════");
        player.sendMessage("§c🔧 OneMine - Operator Commands");
        player.sendMessage("§6═══════════════════════════════════════");
        player.sendMessage("§c[SPIELER COMMANDS]");
        player.sendMessage("§e/menü§7 - Öffnet das Hauptmenü");
        player.sendMessage("§e/commands§7 - Zeigt alle verfügbaren Commands");
        player.sendMessage("§e/bounty§7 - Zeigt die Top-10 Kopfgelder");
        player.sendMessage("§e/stats§7 - Deine Spieler-Statistiken");
        player.sendMessage("§e/clan info§7 - Informationen über deine Bande");
        player.sendMessage("§e/clan members§7 - Zeigt Clan-Mitglieder");
        player.sendMessage("§e/insel info§7 - Info über deine Insel");
        player.sendMessage("");
        player.sendMessage("§c[ADMIN COMMANDS]");
        player.sendMessage("§a/berrys <spieler> <amount>§7 - Berrys an Spieler senden");
        player.sendMessage("§a/admin spawn-marine <anzahl>§7 - Marines spawnen");
        player.sendMessage("§a/admin reset-bounty <spieler>§7 - Kopfgeld zurücksetzen");
        player.sendMessage("§a/admin reload-config§7 - Konfiguration neu laden");
        player.sendMessage("§a/admin island-list§7 - Alle Inseln auflisten");
        player.sendMessage("§a/admin clan-list§7 - Alle Clans anzeigen");
        player.sendMessage("§a/admin setop <spieler>§7 - Spieler zum OP machen");
        player.sendMessage("§6═══════════════════════════════════════");
    }
}
