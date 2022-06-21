package net.boster.chat.common.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.chat.implementation.SimplePatternReader;
import net.boster.chat.common.chat.pattern.PatternReader;
import net.boster.chat.common.utils.colorutils.ColorUtils;
import net.boster.chat.common.utils.colorutils.NewColorUtils;
import net.boster.chat.common.utils.colorutils.OldColorUtils;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatUtils {

    @Getter private static ColorUtils colorUtils;
    @Getter @Setter private static PatternReader patternReader = new SimplePatternReader();

    public static final Character[] ALL_CODES = new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o'};

    private static final Pattern gradient = Pattern.compile("<GRADIENT:([0-9A-Fa-f]{6})>(.*?)</GRADIENT:([0-9A-Fa-f]{6})>");

    private static final List<String> SPECIAL_COLORS = ImmutableList.of("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");
    private static final Map<Color, ChatColor> colors = ImmutableMap.<Color, ChatColor>builder()
            .put(new Color(0), ChatColor.getByChar('0'))
            .put(new Color(170), ChatColor.getByChar('1'))
            .put(new Color(43520), ChatColor.getByChar('2'))
            .put(new Color(43690), ChatColor.getByChar('3'))
            .put(new Color(11141120), ChatColor.getByChar('4'))
            .put(new Color(11141290), ChatColor.getByChar('5'))
            .put(new Color(16755200), ChatColor.getByChar('6'))
            .put(new Color(11184810), ChatColor.getByChar('7'))
            .put(new Color(5592405), ChatColor.getByChar('8'))
            .put(new Color(5592575), ChatColor.getByChar('9'))
            .put(new Color(5635925), ChatColor.getByChar('a'))
            .put(new Color(5636095), ChatColor.getByChar('b'))
            .put(new Color(16733525), ChatColor.getByChar('c'))
            .put(new Color(16733695), ChatColor.getByChar('d'))
            .put(new Color(16777045), ChatColor.getByChar('e'))
            .put(new Color(16777215), ChatColor.getByChar('f')).build();

    static {
        try {
            ChatColor.class.getMethod("of", String.class);
            colorUtils = new NewColorUtils();
        } catch (NoSuchMethodException e) {
            colorUtils = new OldColorUtils();
        }
    }

    public static @NotNull String color(@NotNull String s, @NotNull Color start, @NotNull Color end) {
        ChatColor[] colors = createGradient(start, end, withoutSpecialChars(s).length());
        return applyColors(s, colors);
    }

    public static @NotNull String applyColors(@NotNull String s, @NotNull ChatColor... colors) {
        String sc = "";
        String sb = "";
        String[] chars = s.split("");
        int oi = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i].equals("&") || chars[i].equals("§")) {
                if (i + 1 < chars.length) {
                    if (chars[i + 1].equals("r")) {
                        sc = "";
                    } else {
                        sc += chars[i];
                        sc += chars[i + 1];
                    }
                    i++;
                } else {
                    sb += colors[oi++] + sc + chars[i];
                    sc = "";
                }
            } else {
                sb += colors[oi++] + sc + chars[i];
                sc = "";
            }
        }

        return sb;
    }

    public static @NotNull String withoutSpecialChars(@NotNull String s) {
        String r = s;

        for (String color : SPECIAL_COLORS) {
            if (r.contains(color)) {
                r = r.replace(color, "");
            }
        }

        return r;
    }

    public static ChatColor[] createGradient(@NotNull Color start, @NotNull Color end, int step) {
        ChatColor[] colors = new ChatColor[step];
        int stepR = Math.abs(start.getRed() - end.getRed()) / (step < 2 ? 1 : step - 1);
        int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step < 2 ? 1 : step - 1);
        int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step < 2 ? 1 : step - 1);
        int[] direction = new int[] {
                start.getRed() < end.getRed() ? + 1 : -1,
                start.getGreen() < end.getGreen() ? + 1 : -1,
                start.getBlue() < end.getBlue() ? + 1 : -1
        };

        for (int i = 0; i < step; i++) {
            Color color = new Color(start.getRed() + ((stepR * i) * direction[0]), start.getGreen() + ((stepG * i) * direction[1]), start.getBlue() + ((stepB * i) * direction[2]));
            colors[i] = colorUtils.of(color);
        }

        return colors;
    }

    public static @NotNull ChatColor getClosestColor(@NotNull Color color) {
        Color c = null;
        double d = Integer.MAX_VALUE;

        for(Color cc : colors.keySet()) {
            double distance = Math.pow(color.getRed() - cc.getRed(), 2) + Math.pow(color.getGreen() - cc.getGreen(), 2) + Math.pow(color.getBlue() - cc.getBlue(), 2);
            if (distance < d) {
                c = cc;
                d = distance;
            }
        }

        return colors.get(c);
    }

    public static String toColor(String s) {
        if(s == null) return null;

        return colorUtils.toColor(s);
    }

    public static @NotNull String toColorAndPrefix(@NotNull String s) {
        return toColor(s.replace("%prefix%", BosterChat.get().config().getString("Prefix")).replace("%n%", "\n"));
    }

    public static @NotNull String stripColors(@NotNull String s) {
        return ChatColor.stripColor(s);
    }
}
