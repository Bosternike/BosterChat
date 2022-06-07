package net.boster.chat.bukkit.utils;

import net.boster.chat.bukkit.utils.actionbar.ActionBarSender;
import net.boster.chat.bukkit.utils.actionbar.NewActionBarSender;
import net.boster.chat.bukkit.utils.actionbar.OldActionBarSender;
import net.boster.chat.bukkit.utils.hover.HoverBuilder;
import net.boster.chat.bukkit.utils.hover.NewHoverBuilder;
import net.boster.chat.bukkit.utils.hover.OldHoverBuilder;
import net.boster.chat.bukkit.utils.title.NewTitleSender;
import net.boster.chat.bukkit.utils.title.OldTitleSender;
import net.boster.chat.bukkit.utils.title.TitleSender;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VersionManager {

    private static TitleSender titleSender;
    private static ActionBarSender actionBarSender;
    private static HoverBuilder hoverBuilder;

    public static void load() {
        Version v = Version.getCurrentVersion();
        if (v.getVersionInteger() < 3) {
            titleSender = new OldTitleSender();
        } else {
            titleSender = new NewTitleSender();
        }

        if (v.getVersionInteger() < 4) {
            actionBarSender = new OldActionBarSender();
        } else {
            actionBarSender = new NewActionBarSender();
        }

        if(v.getVersionInteger() < 13) {
            hoverBuilder = new OldHoverBuilder();
        } else {
            hoverBuilder = new NewHoverBuilder();
        }
    }

    public static void sendTitle(@NotNull Player p, @NotNull String title, @NotNull String subTitle, int fadeIn, int stay, int fadeOut) {
        titleSender.send(p, title, subTitle, fadeIn, stay, fadeOut);
    }

    public static void sendActionBar(@NotNull Player p, @NotNull String message) {
        actionBarSender.send(p, message);
    }

    public static @NotNull HoverEvent buildHover(@NotNull String s) {
        return hoverBuilder.build(s);
    }
}
