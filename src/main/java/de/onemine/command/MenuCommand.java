package de.onemine.command;

import de.onemine.OneMinePlugin;
import de.onemine.gui.MenuRouter;
import de.onemine.model.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {
    private final OneMinePlugin plugin;

    public MenuCommand(OneMinePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Command nutzen!");
            return true;
        }

        Player player = (Player) sender;
        
        // PlayerData erstellen falls neu
        PlayerData data = plugin.getDataStore().getPlayerData(player.getUniqueId());
        if (data.getLastName() == null || data.getLastName().isEmpty()) {
            data.setLastName(player.getName());
            plugin.getDataStore().savePlayerData(data);
        }
        
        // Menü öffnen
        MenuRouter.openMainMenu(player, plugin);
        return true;
    }
}
