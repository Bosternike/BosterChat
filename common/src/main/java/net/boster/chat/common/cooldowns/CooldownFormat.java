package net.boster.chat.common.cooldowns;

import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class CooldownFormat {

    @NotNull public final String second;
    @NotNull public final String some_seconds;
    @NotNull public final String seconds;
    @NotNull public final String minute;
    @NotNull public final String some_minutes;
    @NotNull public final String minutes;
    @NotNull public final String hour;
    @NotNull public final String some_hours;
    @NotNull public final String hours;
    @NotNull public final String day;
    @NotNull public final String some_days;
    @NotNull public final String days;

    private CooldownFormat(@NotNull String second, @NotNull String some_seconds, @NotNull String seconds, @NotNull String minute, @NotNull String some_minutes,
                           @NotNull String minutes, @NotNull String hour, @NotNull String some_hours, @NotNull String hours, @NotNull String day,
                           @NotNull String some_days, @NotNull String days) {
        this.second = second;
        this.some_seconds = some_seconds;
        this.seconds = seconds;
        this.minute = minute;
        this.some_minutes = some_minutes;
        this.minutes = minutes;
        this.hour = hour;
        this.some_hours = some_hours;
        this.hours = hours;
        this.day = day;
        this.some_days = some_days;
        this.days = days;
    }

    public static CooldownFormat fromSection(ConfigurationSection section) {
        if(section == null) return CooldownUtils.defaultFormat;

        String second = section.getString("second", CooldownUtils.defaultFormat.second);
        String some_seconds = section.getString("some_seconds", CooldownUtils.defaultFormat.some_seconds);
        String seconds = section.getString("seconds", CooldownUtils.defaultFormat.seconds);
        String minute = section.getString("minute", CooldownUtils.defaultFormat.minute);
        String some_minutes = section.getString("some_minutes", CooldownUtils.defaultFormat.some_minutes);
        String minutes = section.getString("minutes", CooldownUtils.defaultFormat.minutes);
        String hour = section.getString("hour", CooldownUtils.defaultFormat.hour);
        String some_hours = section.getString("some_hours", CooldownUtils.defaultFormat.some_hours);
        String hours = section.getString("hours", CooldownUtils.defaultFormat.hours);
        String day = section.getString("day", CooldownUtils.defaultFormat.day);
        String some_days = section.getString("some_days", CooldownUtils.defaultFormat.some_days);
        String days = section.getString("days", CooldownUtils.defaultFormat.days);

        return new CooldownFormat(second, some_seconds, seconds, minute, some_minutes, minutes, hour, some_hours, hours, day, some_days, days);
    }

    public static CooldownFormat empty() {
        return new CooldownFormat("second", "seconds", "seconds", "minute", "minutes", "minutes",
                "hour", "hours", "hours", "day", "days", "days");
    }
}
