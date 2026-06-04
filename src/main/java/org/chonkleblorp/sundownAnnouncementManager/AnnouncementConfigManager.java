package org.chonkleblorp.sundownAnnouncementManager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class AnnouncementConfigManager {
    private final static AnnouncementConfigManager instance = new AnnouncementConfigManager();

    private File file;
    private YamlConfiguration config;

    private String message;

    private AnnouncementConfigManager() {

    }

    public void load() {
        file = new File(SundownAnnouncementManager.getInstance().getDataFolder(), "settings.yml");
        if (!file.exists())
            SundownAnnouncementManager.getInstance().saveResource("settings.yml", false);

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        message = config.getString("announcement.message", "");
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        set("announcement.message", message);
    }

    public static AnnouncementConfigManager getInstance() {
        return instance;
    }
}
