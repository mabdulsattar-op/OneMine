package de.onemine.command;

import de.onemine.OneMinePlugin;
import de.onemine.model.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BerrysCommand implements CommandExecutor {
    private final OneMinePlugin plugin;

    public BerrysCommand(OneMinePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Command nutzen!");
            return true;
        }

        Player player = (Player) sender;

        // Nur OPs dürfen Berrys senden
        if (!player.isOp()) {
            player.sendMessage("§c✗ Du darfst Berrys nicht senden!");
            return true;
        }

        // Syntax: /berrys <spieler> <amount>
        if (args.length < 2) {
            player.sendMessage("§e/berrys <spieler> <amount>");
            return true;
        }

        String targetName = args[0];
        long amount;

        try {
            amount = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("§c✗ Ungültige Anzahl!");
            return true;
        }

        if (amount <= 0) {
            player.sendMessage("§c✗ Betrag muss > 0 sein!");
            return true;
        }

        // Ziel-Spieler finden
        Player target = plugin.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§c✗ Spieler '" + targetName + "' nicht online!");
            return true;
        }

        // Berrys senden
        PlayerData data = plugin.getDataStore().getPlayerData(target.getUniqueId());
        data.addBerrys(amount);
        plugin.getDataStore().savePlayerData(data);

        player.sendMessage("§a✓ " + amount + " Berrys an " + target.getName() + " gesendet!");
        target.sendMessage("§a✓ Du erhältst " + amount + " Berrys von " + player.getName() + "!");

        plugin.getLogger().info("§a" + player.getName() + " sendete " + amount + " Berrys an " + target.getName());

        return true;
    }
}
