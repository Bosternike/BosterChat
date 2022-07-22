package net.boster.chat.common.chat.implementation;

import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.chat.pattern.PatternReader;
import net.boster.chat.common.chat.pattern.patterns.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimplePatternReader implements PatternReader {

    @Override
    public @Nullable MessagePattern read(@NotNull String s) {
        return read(s, null);
    }

    private MessagePattern read(String s, String o) {
        String initial = o;
        if(initial == null) {
            initial = s;
        }
        try {
            if(s.contains("|")) {
                List<MessagePattern> list = new ArrayList<>();
                for(String p : s.split("\\|")) {
                    list.add(read(p, initial));
                }
                return new SplitPattern(s, list);
            } else if(s.contains("smart:")) {
                List<MessagePattern> list = new ArrayList<>();
                for(String p : s.split("smart:")[1].split(",")) {
                    list.add(read(p, initial));
                }
                return new SmartPattern(s, list);
            } else if(s.contains("random:")) {
                List<MessagePattern> list = new ArrayList<>();
                for(String p : s.split("random:")[1].split(",")) {
                    list.add(read(p, initial));
                }
                return new RandomPattern(s, list);
            } else if(s.contains("randomChar:")) {
                List<MessagePattern> list = new ArrayList<>();
                for(String p : s.split("randomChar:")[1].split(",")) {
                    list.add(read(p, initial));
                }
                return new RandomCharPattern(s, list);
            } else if(s.contains("rgb")) {
                String[] ss = s.split("rgb:");
                if(ss.length < 2) {
                    return new RGBPattern();
                }

                String[] c = ss[1].split(";");
                return new RGBPattern(getColor(c[0].split(",")), getColor(c[1].split(",")));
            } else {
                return new ColorPattern(s);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(initial != null ? "Could not read: \"" + initial + "\" section:" + s : "Could not read: " + s, e);
        }
    }

    private Color getColor(String[] s) {
        try {
            return new Color(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
        } catch (Exception e) {
            return null;
        }
    }
}
