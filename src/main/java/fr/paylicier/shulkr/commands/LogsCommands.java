package fr.paylicier.shulkr.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.paylicier.shulkr.Shulkr;
import fr.paylicier.shulkr.gui.PlayerMenuUtility;
import fr.paylicier.shulkr.gui.menu.LogsMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Objects;

import static fr.paylicier.shulkr.Utils.*;

/**
 * Handles all log-related commands for the Shulkr plugin.
 * Provides functionality to upload, list, and manage server logs through various commands.
 */
@Command({"logs", "log", "shulkr", "shulkrlogs"})
@Description("Upload and manage server logs via Shulkr")
public class LogsCommands {

    // Color scheme constants for consistent messaging
    private static final NamedTextColor PRIMARY_COLOR = NamedTextColor.AQUA;
    private static final NamedTextColor SECONDARY_COLOR = NamedTextColor.DARK_AQUA;
    private static final NamedTextColor ERROR_COLOR = NamedTextColor.RED;

    /**
     * Default command handler that uploads the latest log file.
     * Executes asynchronously to prevent server lag during upload.
     *
     * @param sender The command sender (player or console)
     */
    @DefaultFor("~")
    @CommandPermission("shulkr.upload")
    public void defaultCommand(CommandSender sender) {
        uploadLogFile(sender, "latest.log");
    }

    /**
     * Opens a GUI menu displaying all available log files.
     *
     * @param player The player viewing the logs list
     */
    @Subcommand("list")
    @Description("Display available server logs")
    @CommandPermission("shulkr.upload")
    public void list(Player player) {
        LogsMenu logsMenu = new LogsMenu(new PlayerMenuUtility(player));
        logsMenu.open();
    }

    /**
     * Uploads a specific log file to Shulkr.
     *
     * @param sender The command sender
     * @param log The name of the log file to upload (defaults to latest.log)
     */
    @Subcommand("upload")
    @Description("Upload a specific log file")
    @CommandPermission("shulkr.upload")
    @AutoComplete("@loglist")
    public void upload(CommandSender sender, @Default("latest.log") String log) {
        String path = getLogs().contains(log) ? log : "latest.log";
        Shulkr.getPlugin(Shulkr.class).adventure().sender(sender).sendMessage(Component.text("Uploading logs: ").color(PRIMARY_COLOR).append(Component.text(path).color(SECONDARY_COLOR)));
        uploadLogFile(sender, path);
    }

    /**
     * Helper method to handle the asynchronous upload of log files.
     *
     * @param sender The command sender
     * @param logPath The path of the log file to upload
     */
    private void uploadLogFile(CommandSender sender, String logPath) {
        Bukkit.getScheduler().runTaskAsynchronously(Shulkr.getPlugin(Shulkr.class), () -> {
            try {
                String logs = uploadLogs(Objects.requireNonNull(getLogContent(logPath)));
                JsonObject response = JsonParser.parseString(logs).getAsJsonObject();

                if (!response.get("success").getAsBoolean()) {
                    handleUploadError(sender, response.getAsString());
                    return;
                }

                String link = response.get("url") != null ?
                        response.get("url").getAsString() :
                        response.get("id").getAsString();

                sendUploadSuccessMessage(sender, link);

            } catch (Exception e) {
                handleUploadError(sender, e.toString());
            }
        });
    }

    /**
     * Handles errors during log upload and sends appropriate messages.
     *
     * @param sender The command sender
     * @param errorDetails Details of the error that occurred
     */
    private void handleUploadError(CommandSender sender, String errorDetails) {
        Shulkr plugin = Shulkr.getPlugin(Shulkr.class);
        plugin.getLogger().severe("Log upload failed: " + errorDetails);
        plugin.adventure().sender(sender).sendMessage(
                Component.text("An error occurred while uploading logs")
                        .color(ERROR_COLOR)
                        .decorate(TextDecoration.BOLD)
        );
    }

    /**
     * Sends a success message with a clickable link to the uploaded logs.
     *
     * @param sender The command sender
     * @param link URL to the uploaded logs
     */
    private void sendUploadSuccessMessage(CommandSender sender, String link) {
        TextComponent message = Component.text("Logs uploaded to Shulkr: ")
                .color(PRIMARY_COLOR)
                .append(Component.text(link)
                        .color(SECONDARY_COLOR)
                        .decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.openUrl(link)));

        Shulkr.getPlugin(Shulkr.class).adventure().sender(sender).sendMessage(message);
        Shulkr.getPlugin(Shulkr.class).getLogger().info("Logs uploaded successfully: " + link);
    }
}