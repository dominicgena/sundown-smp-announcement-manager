package org.chonkleblorp.sundownAnnouncementManager;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SundownAnnouncementManager extends JavaPlugin {
    private static final Map<UUID, String> playerTags = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        final String bukkitVersion = Bukkit.getServer().getBukkitVersion();
        final String versionString = bukkitVersion.split("-")[0];
        final String[] versions = versionString.split("\\.");

        final int version = Integer.parseInt(versions[1]);
        // 1. Access the Lifecycle Manager
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {

            // 2. Register the command from the AnnouncementCommand class
            event.registrar().register(
                    AnnouncementCommand.create().build(), // Call .build() to convert the Builder to a Node
                    "Manage sundown announcements"        // Provide a description for the command
            );

        });

        getLogger().info("SundownAnnouncementManager has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Map<UUID, String> getPlayerTags() {
        return playerTags;
    }

    public static SundownAnnouncementManager getInstance() {
        return getPlugin(SundownAnnouncementManager.class);
    }
}
