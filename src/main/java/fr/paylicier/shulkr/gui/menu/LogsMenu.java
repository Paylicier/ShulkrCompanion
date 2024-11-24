package fr.paylicier.shulkr.gui.menu;

import fr.paylicier.shulkr.Shulkr;
import fr.paylicier.shulkr.gui.PaginatedMenu;
import fr.paylicier.shulkr.gui.PlayerMenuUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static fr.paylicier.shulkr.Utils.getLogs;

/**
 * A paginated menu that displays available log files and allows players to upload them.
 * Extends PaginatedMenu to handle multiple pages of log entries.
 */
public class LogsMenu extends PaginatedMenu {

    // Constants for menu configuration
    private static final int MENU_SIZE = 45;
    private static final String MENU_TITLE = "Server Logs";
    private static final NamespacedKey LOG_KEY = new NamespacedKey(
            Shulkr.getPlugin(Shulkr.class),
            "log"
    );

    private static final NamedTextColor ERROR_COLOR = NamedTextColor.RED;

    private final List<String> logs;

    /**
     * Constructs a new LogsMenu instance.
     *
     * @param playerMenuUtility The utility class managing player-specific menu data
     */
    public LogsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.logs = getLogs();
    }

    @Override
    public String getMenuName() {
        return MENU_TITLE;
    }

    @Override
    public int getSlots() {
        return MENU_SIZE;
    }

    /**
     * Handles click events within the menu.
     * Processes navigation arrows, log selection, and menu closing.
     *
     * @param event The inventory click event
     */
    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        switch (clickedItem.getType()) {
            case PAPER -> handleLogSelection(player, clickedItem);
            case BARRIER -> player.closeInventory();
            case ARROW -> handleNavigation(player, clickedItem);
        }
    }

    /**
     * Handles the selection and upload of a log file.
     *
     * @param player The player selecting the log
     * @param item The selected log item
     */
    private void handleLogSelection(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String logFile = meta.getPersistentDataContainer()
                .get(LOG_KEY, PersistentDataType.STRING);

        if (logFile != null) {
            player.performCommand("shulkr upload " + logFile);
            player.closeInventory();
        }
    }

    /**
     * Handles navigation between pages of the menu.
     *
     * @param player The player navigating
     * @param arrow The arrow item clicked
     */
    private void handleNavigation(Player player, ItemStack arrow) {
        if (arrow.getItemMeta() == null) return;

        String direction = arrow.getItemMeta().getDisplayName();

        if (direction.contains("Left")) {
            if (page == 0) {
                Shulkr.getPlugin(Shulkr.class).adventure().sender(player).sendMessage(Component.text("You are already on the first page.")
                        .color(ERROR_COLOR));
                return;
            }
            page--;
        } else if (direction.contains("Right")) {
            if ((index + 1) >= logs.size()) {
                Shulkr.getPlugin(Shulkr.class).adventure().sender(player).sendMessage(Component.text("You are on the last page.")
                        .color(ERROR_COLOR));
                return;
            }
            page++;
        }

        super.open();
    }

    /**
     * Populates the menu with log items and navigation controls.
     * Creates a paginated display of available log files.
     */
    @Override
    public void setMenuItems() {
        addMenuBorder();

        for (int i = 0; i < getMaxItemsPerPage(); i++) {
            index = getMaxItemsPerPage() * page + i;
            if (index >= logs.size()) break;

            String logFile = logs.get(index);
            if (logFile == null) continue;

            ItemStack logItem = createLogItem(logFile);
            inventory.addItem(logItem);
        }
    }

    /**
     * Creates an ItemStack representing a log file.
     *
     * @param logFile The name of the log file
     * @return An ItemStack configured to represent the log file
     */
    private @NotNull ItemStack createLogItem(String logFile) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setDisplayName(ChatColor.AQUA + "Upload " + ChatColor.DARK_AQUA + logFile);

        // Store log filename in item metadata
        meta.getPersistentDataContainer().set(
                LOG_KEY,
                PersistentDataType.STRING,
                logFile
        );

        item.setItemMeta(meta);
        return item;
    }
}