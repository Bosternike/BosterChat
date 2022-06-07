package net.boster.chat.common.utils;

import net.boster.chat.common.utils.colorutils.ColorUtils;
import net.boster.chat.common.utils.colorutils.NewColorUtils;
import net.boster.chat.common.utils.colorutils.OldColorUtils;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ChatUtils {

    private static ColorUtils colorUtils;

    static {
        try {
            ChatColor.class.getMethod("of", String.class);
            colorUtils = new NewColorUtils();
        } catch (NoSuchMethodException e) {
            colorUtils = new OldColorUtils();
        }
    }


    public static String toColor(String s) {
        if(s == null) return null;

        return colorUtils.toColor(s);
    }

    public static @NotNull String stripColors(@NotNull String s) {
        return ChatColor.stripColor(s);
    }
}
