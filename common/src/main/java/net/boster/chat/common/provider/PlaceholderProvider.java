package net.boster.chat.common.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.placeholders.PlaceholdersRequest;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@AllArgsConstructor
@NoArgsConstructor
public class PlaceholderProvider<T> {

    @Getter @Setter @NotNull protected Class<T> clazz;
    @Getter @Setter @NotNull protected Function<PlaceholdersRequest<T>, @NotNull String> function;
}
