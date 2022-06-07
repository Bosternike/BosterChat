package net.boster.chat.common.placeholders;

import net.boster.chat.common.provider.PlaceholderProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PlaceholdersManager {

    private static final List<PlaceholderProvider<?>> placeholders = new ArrayList<>();

    public static <T> void addProvider(@NotNull Class<T> clazz, @NotNull Function<T, @NotNull String> function) {
        addProvider(new PlaceholderProvider<>(clazz, function));
    }

    public static void addProvider(@NotNull PlaceholderProvider<?> provider) {
        placeholders.add(provider);
    }

    public static <T> List<PlaceholderProvider<T>> requestPlaceholders(@NotNull Class<T> clazz) {
        List<PlaceholderProvider<T>> list = new ArrayList<>();

        for(PlaceholderProvider<?> p : placeholders) {
            if(p.getClazz() == clazz) {
                list.add((PlaceholderProvider<T>) p);
            }
        }

        return list;
    }
}
