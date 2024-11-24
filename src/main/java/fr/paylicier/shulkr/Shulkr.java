package fr.paylicier.shulkr;

import fr.paylicier.shulkr.commands.LogsCommands;
import fr.paylicier.shulkr.gui.MenuListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import static fr.paylicier.shulkr.Utils.getLogs;

public final class Shulkr extends JavaPlugin {

    protected BukkitAudiences adventure;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.adventure = BukkitAudiences.create(this);
        BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.getAutoCompleter().registerSuggestion("loglist", SuggestionProvider.of(getLogs()));
        handler.register(new LogsCommands());
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        //config
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }


}
