package net.boster.chat.common.log;

import lombok.Getter;

public enum LogType {

    NONE("§d[§bBosterChat§d] ", "§7"),
    UPDATER("§d[§bBosterChat§d] §7[§6UPDATER§7] ", "§7"),
    FINE("§d[§bBosterChat§d] §7[§aFINE§7] ", "§a"),
    INFO("§d[§bBosterChat§d] §7[§9INFO§7] ", "§9"),
    WARNING("§d[§bBosterChat§d] §7[§cWARNING§7] ", "§c"),
    ERROR("§d[§bBosterChat§d] §7[§4ERROR§7] ", "§4");

    @Getter private final String format;
    @Getter private final String color;

    LogType(String s, String color) {
        this.format = s;
        this.color = color;
    }
}
