package net.boster.chat.common.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;

public class TextUtils {

    private static final String DOT = "([.,&-=+*#@!%/ ]{1,10})";
    public static final String IP_REGEX = "((\\d{1,3})" + DOT + "(\\d{1,3})" + DOT + "(\\d{1,3})" + DOT + "(\\d{1,3}){2,10})";
    public static final String DOMAIN_REGEX = "(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})";

    public static String replace(String s, String l, String replacement) {
        StringBuilder sbSource = new StringBuilder(s);
        StringBuilder sbSourceLower = new StringBuilder(s.toLowerCase());
        String searchString = l.toLowerCase();

        int idx = 0;
        while((idx = sbSourceLower.indexOf(searchString, idx)) != -1) {
            sbSource.replace(idx, idx + searchString.length(), replacement);
            sbSourceLower.replace(idx, idx + searchString.length(), replacement);
            idx += replacement.length();
        }
        sbSourceLower.setLength(0);
        sbSourceLower.trimToSize();
        sbSourceLower = null;

        return sbSource.toString();
    }

    public static String stripMessage(@NotNull String o) {
        String s = ChatUtils.stripColors(o.toLowerCase());

        s = s.replaceAll("[^a-zA-Z0-9\\s]", "");

        s = s.replaceAll("(.)(?=\\1\\1+)", "");
        s = s.replaceAll("(..)(?=\\1\\1+)", "");
        s = s.replaceAll("(...)(?=\\1\\1+)", "");

        return s;
    }

    public static int similarity(@NotNull String s1, @NotNull String s2) {
        String longer = s1, shorter = s2;

        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        final int longerLength = longer.length();

        if (longerLength == 0) {
            return 100;
        }

        final double result = (longerLength - editDistance(longer, shorter)) / (double) longerLength;

        return (int) (result * 100);
    }

    private static int editDistance(@NotNull String s1, @NotNull String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        final int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++)
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }

        return costs[s2.length()];
    }

    public static @NotNull String capitalize(@NotNull String text) {
        String t = "";

        for(String s : text.split("(?<=[!?\\.])\\s")) {
            String word = text.split("\\s")[0];

            if (!isDomain(word)) {
                s = s.substring(0, 1).toUpperCase() + s.substring(1);
            }

            t += s + " ";
        }

        return t.trim();
    }

    public static @NotNull String insertDot(@NotNull String s) {
        String r = s;
        String lastChar = s.substring(s.length() - 1);
        String[] words = s.split("\\s");
        String lastWord = words[words.length - 1];

        if(!isDomain(lastWord) && lastChar.matches("(?i)[a-z]")) {
            r += ".";
        }

        return r;
    }

    public static boolean isDomain(@NotNull String s) {
        return s.matches(DOMAIN_REGEX);
    }

    public static boolean isIP(@NotNull String s) {
        return s.matches(IP_REGEX);
    }

    @Contract("_, _, !null -> !null")
    @Nullable
    public static String buildOneLineList(@NotNull Collection<? extends String> c, @NotNull String separator, @Nullable String def) {
        return buildOneLineList(c, separator, null, def);
    }

    @Contract("_, _, _, !null -> !null")
    @Nullable
    public static String buildOneLineList(@NotNull Collection<? extends String> c, @NotNull String separator, @Nullable Function<@NotNull String, @NotNull String> formatter, @Nullable String def) {
        if(c.isEmpty()) return def;

        String s = "";
        String a = "";

        for(String o : c) {
            s += a + (formatter != null ? formatter.apply(o) : o);
            a = separator;
        }

        return s;
    }
}
