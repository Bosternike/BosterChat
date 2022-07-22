package net.boster.chat.common.utils;

import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class ConfigUtils {

    public static String toSimpleName(File f) {
        String name;
        try {
            String[] ss = f.getName().replace(".", ",").split(",");
            name = ss[ss.length - 2];
        } catch (Exception e) {
            name = f.getName();
        }
        return name;
    }

    public static boolean hasAllStrings(@NotNull ConfigurationSection instance, @NotNull ConfigurationSection toCheck, List<String> skip) {
        String parent = instance.getCurrentPath() != null && !instance.getCurrentPath().isEmpty() ? instance.getCurrentPath() + "." : "";

        for(String k : instance.getKeys()) {
            if(skip.contains(parent + k)) continue;
            if(toCheck.get(k) == null) return false;

            ConfigurationSection ns = instance.getSection(k);
            if(ns != null) {
                ConfigurationSection nc = toCheck.getSection(k);
                if(nc == null) {
                    return false;
                } else {
                    if(!hasAllStrings(ns, nc, skip)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void replaceOldConfig(@NotNull File oldFile, @NotNull File outFile, @NotNull InputStream config) {
        replaceOldConfig(oldFile, toSimpleName(oldFile), outFile, config);
    }

    public static void replaceOldConfig(@NotNull File oldFile, @NotNull String oldFileName, @NotNull File outFile, @NotNull InputStream config) {
        try {
            Files.move(oldFile.toPath(), new File(oldFile.getParentFile(), oldFileName + "-old_" + System.currentTimeMillis() + ".yml").toPath());
            saveResource(config, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveResource(@NotNull InputStream in, File outFile) throws IOException {
        if(!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            outFile.createNewFile();
        }

        OutputStream out = new FileOutputStream(outFile);
        byte[] buf = new byte[1024];

        int len;
        while((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        out.close();
        in.close();
    }

    public static String configToString(@NotNull ConfigurationSection section) {
        DumperOptions options = new DumperOptions();
        Representer representer = new Representer();
        Yaml yaml = new Yaml(representer, options);
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        String dump = yaml.dump(section.entries());

        if (dump.equals("{}\n")) {
            dump = "";
        }

        return dump;
    }
}
