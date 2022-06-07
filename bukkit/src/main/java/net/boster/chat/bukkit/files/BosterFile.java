package net.boster.chat.bukkit.files;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.bukkit.BosterChatBukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class BosterFile {

    private static final HashMap<String, BosterFile> hash = new HashMap<>();

    @Getter @Setter @NotNull private String expansion = ".yml";
    @Getter @Setter @NotNull private String directory = "";
    @Getter private final String name;
    @Getter private File file;
    @Getter private FileConfiguration configuration;

    public BosterFile(@NotNull String file) {
        hash.put(file, this);
        this.name = file;
    }

    public static BosterFile get(String s) {
        return hash.get(s);
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void loadFile(boolean loadFrom, boolean loadYaml) {
        file = new File(BosterChatBukkit.getInstance().getDataFolder() + directory, name + expansion);
        if (!file.exists()) {
            if (loadFrom) {
                BosterChatBukkit.getInstance().saveResource(directory + name + expansion, false);
            } else {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (loadYaml) {
            configuration = YamlConfiguration.loadConfiguration(file);
        }
    }

    public static Collection<BosterFile> getFiles() {
        return hash.values();
    }
}
