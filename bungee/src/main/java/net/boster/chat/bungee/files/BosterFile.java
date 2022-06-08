package net.boster.chat.bungee.files;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.bungee.BosterChatBungee;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;

public class BosterFile {

    private static final HashMap<String, BosterFile> hash = new HashMap<>();

    @Getter @Setter @NotNull private String expansion = ".yml";
    @Getter @Setter @NotNull private String directory = "";
    @Getter private final String name;
    @Getter private File file;
    @Getter private Configuration configuration;

    private boolean loadFrom, loadYaml;

    public BosterFile(@NotNull String file) {
        hash.put(file, this);
        this.name = file;
    }

    public static BosterFile get(String s) {
        return hash.get(s);
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        loadFile(loadFrom, loadYaml);
    }

    public void loadFile(boolean loadFrom, boolean loadYaml) {
        this.loadFrom = loadFrom;
        this.loadYaml = loadYaml;

        file = new File(BosterChatBungee.getInstance().getDataFolder() + directory, name + expansion);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                if(loadFrom) {
                    Files.copy(BosterChatBungee.getInstance().getResourceAsStream(directory + name + expansion), file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(loadYaml) {
            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Collection<BosterFile> getFiles() {
        return hash.values();
    }
}
