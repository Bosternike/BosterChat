package net.boster.chat.common.cooldowns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
public class Cooldown {

    @Getter @Setter protected int days;
    @Getter @Setter protected int hours;
    @Getter @Setter protected int minutes;
    @Getter @Setter protected int seconds;
    @Getter @Setter protected int time;

    @Getter @Setter @NotNull protected CooldownFormat format;
    @Getter @Setter @NotNull protected CooldownMessages messages;

    @Getter @Setter @Nullable protected String skipPermission;

    public Cooldown(@NotNull ConfigurationSection section) {
        this.days = section.getInt("Time.days", 0);
        this.hours = section.getInt("Time.hours", 0);
        this.minutes = section.getInt("Time.minutes", 0);
        this.seconds = section.getInt("Time.seconds", 0);

        this.time = applyTime();

        this.format = CooldownFormat.fromSection(section.getSection("Format"));
        this.messages = CooldownMessages.load(section.getSection("Messages"));

        this.skipPermission = section.getString("SkipPermission");
    }

    public int applyTime() {
        int lg = 0;
        if (days > 0) {
            lg += 86400 * days;
        }
        if (hours > 0) {
            lg += 3600 * hours;
        }
        if (minutes > 0) {
            lg += 60 * minutes;
        }
        if (seconds > 0) {
            lg += seconds;
        }
        return lg;
    }
}
