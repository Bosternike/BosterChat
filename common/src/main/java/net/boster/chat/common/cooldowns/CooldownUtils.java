package net.boster.chat.common.cooldowns;

import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class CooldownUtils {

	public static CooldownFormat defaultFormat;
	public static CooldownMessages cooldownMessages;

	public static void load(@NotNull ConfigurationSection format, @NotNull ConfigurationSection messages) {
		defaultFormat = CooldownFormat.fromSection(format);
		cooldownMessages = CooldownMessages.load(messages);
	}

	public static void sendCooldown(@NotNull PlayerSender p, long lg, int time, @Nullable String msg, @Nullable String title, @Nullable String subtitle, @Nullable String actionbar, @Nullable CooldownFormat format) {
		String left = getLeft(lg, time, format != null ? format : defaultFormat);

		if (msg != null) {
			p.sendMessage(ChatUtils.toColor(msg).replace("%time%", left));
		}

		p.sendTitle(title != null ? ChatUtils.toColor(title).replace("%time%", left) : "", subtitle != null ? ChatUtils.toColor(subtitle).replace("%time%", left) : "", 20, 60, 20);

		if (actionbar != null) {
			p.sendActionbar(ChatUtils.toColor(actionbar).replace("%time%", left));
		}
	}

	public static void sendCooldown(@NotNull PlayerSender p, long lg, int time, @NotNull CooldownMessages messages, @Nullable CooldownFormat format) {
		sendCooldown(p, lg, time, messages.getMessage(), messages.getTitle(), messages.getSubTitle(), messages.getActionbar(), format);
	}

	public static String getLeft(long lg, int time) {
		return getLeft(lg, time, defaultFormat);
	}

	public static String getLeft(long lg, int time, @NotNull CooldownFormat cdFormat) {
		String s = "";
		long secondsleft = ((lg / 1000) + time) - (System.currentTimeMillis() / 1000);
		if (secondsleft > 0) {
			boolean ds = false;
			if (TimeUnit.SECONDS.toDays(secondsleft) > 0) {
				s = getDays((int) TimeUnit.SECONDS.toDays(secondsleft), cdFormat);
				secondsleft -= 86400 * TimeUnit.SECONDS.toDays(secondsleft);
				ds = true;
			}
			if (TimeUnit.SECONDS.toHours(secondsleft) > 0) {
				s += (ds ? " " : "") + getHours((int) TimeUnit.SECONDS.toHours(secondsleft), cdFormat);
				secondsleft -= 3600 * TimeUnit.SECONDS.toHours(secondsleft);
				ds = true;
			}
			if (TimeUnit.SECONDS.toMinutes(secondsleft) > 0) {
				s += (ds ? " " : "") + getMinutes((int) TimeUnit.SECONDS.toMinutes(secondsleft), cdFormat);
				secondsleft -= 60 * TimeUnit.SECONDS.toMinutes(secondsleft);
				ds = true;
			}
			if (secondsleft > 0) {
				s += (ds ? " " : "") + getSeconds((int) secondsleft, cdFormat);
			}
		}
		return s;
	}

	public static String getSeconds(int i) {
		return getSeconds(i, defaultFormat);
	}

	public static String getMinutes(int i) {
		return getMinutes(i, defaultFormat);
	}

	public static String getHours(int i) {
		return getHours(i, defaultFormat);
	}

	public static String getDays(int i) {
		return getDays(i, defaultFormat);
	}

	public static String getSeconds(int i, @NotNull CooldownFormat cdFormat) {
		String s;
		if (i > 5 && i < 21) {
			s = cdFormat.seconds;
		} else {
			String si = Integer.toString(i);
			if (si.endsWith("1")) {
				s = cdFormat.second;
			} else if (si.endsWith("2") || si.endsWith("3") || si.endsWith("4")) {
				s = cdFormat.some_seconds;
			} else {
				s = cdFormat.seconds;
			}
		}
		return i + " " + s;
	}

	public static String getMinutes(int i, @NotNull CooldownFormat cdFormat) {
		String s;
		if (i > 5 && i < 21) {
			s = cdFormat.minutes;
		} else {
			String si = Integer.toString(i);
			if (si.endsWith("1")) {
				s = cdFormat.minute;
			} else if (si.endsWith("2") || si.endsWith("3") || si.endsWith("4")) {
				s = cdFormat.some_minutes;
			} else {
				s = cdFormat.minutes;
			}
		}
		return i + " " + s;
	}

	public static String getHours(int i, @NotNull CooldownFormat cdFormat) {
		String s;
		if (i > 5 && i < 21) {
			s = cdFormat.hours;
		} else {
			String si = Integer.toString(i);
			if (si.endsWith("1")) {
				s = cdFormat.hour;
			} else if (si.endsWith("2") || si.endsWith("3") || si.endsWith("4")) {
				s = cdFormat.some_hours;
			} else {
				s = cdFormat.hours;
			}
		}
		return i + " " + s;
	}

	public static String getDays(int i, @NotNull CooldownFormat cdFormat) {
		String s;
		String si = Integer.toString(i);
		if (i > 5 && i < 21 || si.length() >= 3 &&
				(si.endsWith("11") || si.endsWith("12") || si.endsWith("13") || si.endsWith("14") || si.endsWith("15") ||
						si.endsWith("16") || si.endsWith("17") || si.endsWith("18") || si.endsWith("19") || si.endsWith("20"))) {
			s = cdFormat.days;
		} else {
			if (si.endsWith("1")) {
				s = cdFormat.day;
			} else if (si.endsWith("2") || si.endsWith("3") || si.endsWith("4")) {
				s = cdFormat.some_days;
			} else {
				s = cdFormat.days;
			}
		}
		return i + " " + s;
	}

	public static int getCooldown(@NotNull ConfigurationSection section) {
		int lg = 0;
		if (section.getInt("days") > 0) {
			lg += 86400 * section.getInt("days");
		}
		if (section.getInt("hours") > 0) {
			lg += 3600 * section.getInt("hours");
		}
		if (section.getInt("minutes") > 0) {
			lg += 60 * section.getInt("minutes");
		}
		if (section.getInt("seconds") > 0) {
			lg += section.getInt("seconds");
		}
		return lg;
	}
}
