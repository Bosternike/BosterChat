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

        String second;
        String some_seconds;
        String seconds;
        String minute;
        String some_minutes;
        String minutes;
        String hour;
        String some_hours;
        String hours;
        String day;
        String some_days;
        String days;
        //Seconds
        if(section.getString("second") != null) {
            second = section.getString("second");
        } else {
            second = CooldownUtils.defaultFormat.second;
        }
        if(section.getString("some_seconds") != null) {
            some_seconds = section.getString("some_seconds");
        } else {
            some_seconds = CooldownUtils.defaultFormat.some_seconds;
        }
        if(section.getString("seconds") != null) {
            seconds = section.getString("seconds");
        } else {
            seconds = CooldownUtils.defaultFormat.seconds;
        }
        //Minutes
        if(section.getString("minute") != null) {
            minute = section.getString("minute");
        } else {
            minute = CooldownUtils.defaultFormat.minute;
        }
        if(section.getString("some_minutes") != null) {
            some_minutes = section.getString("some_minutes");
        } else {
            some_minutes = CooldownUtils.defaultFormat.some_minutes;
        }
        if(section.getString("minutes") != null) {
            minutes = section.getString("minutes");
        } else {
            minutes = CooldownUtils.defaultFormat.minutes;
        }
        //Hours
        if(section.getString("hour") != null) {
            hour = section.getString("hour");
        } else {
            hour = CooldownUtils.defaultFormat.hour;
        }
        if(section.getString("some_hours") != null) {
            some_hours = section.getString("some_hours");
        } else {
            some_hours = CooldownUtils.defaultFormat.some_hours;
        }
        if(section.getString("hours") != null) {
            hours = section.getString("hours");
        } else {
            hours = CooldownUtils.defaultFormat.hours;
        }
        //Days
        if(section.getString("day") != null) {
            day = section.getString("day");
        } else {
            day = CooldownUtils.defaultFormat.day;
        }
        if(section.getString("some_days") != null) {
            some_days = section.getString("some_days");
        } else {
            some_days = CooldownUtils.defaultFormat.some_days;
        }
        if(section.getString("days") != null) {
            days = section.getString("days");
        } else {
            days = CooldownUtils.defaultFormat.days;
        }
        return new CooldownFormat(second, some_seconds, seconds, minute, some_minutes, minutes, hour, some_hours, hours, day, some_days, days);
    }
}
