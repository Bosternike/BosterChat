package net.boster.chat.common.commands;

import net.boster.chat.common.BosterChat;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.chat.pattern.patterns.ColorPattern;
import net.boster.chat.common.provider.ChatColorProvider;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class ChatColorCommand extends ChatCommandWrapper {

    public ChatColorCommand(@NotNull BosterChatPlugin plugin, @NotNull String name, @NotNull String... aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(!checkPermission(sender, "boster.chat.chatcolor")) return;

        boolean u = sender.hasPermission("boster.chat.chatcolor.unlimited");

        if(args.length == 0) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatColor.usage")));
            return;
        }

        if(args.length == 1) {
            if(!(sender instanceof PlayerSender)) {
                sender.sendMessage(ChatUtils.toColorAndPrefix("%prefix% &cYou must be a player to do this."));
                return;
            }

            process(sender, args[0], (PlayerSender) sender, u);
            return;
        }

        PlayerSender p = BosterChat.get().getPlayer(args[1]);
        if(p == null) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.nullPlayer").replace("%arg%", args[1])));
            return;
        }

        process(sender, args[1], p, u);
    }

    private void process(CommandSender sender, String arg, PlayerSender p, boolean u) {
        if(p != sender) {
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.chatColor.noPermissionOthers")));
            return;
        }

        Object o = read(sender, arg, u);

        if(o instanceof Boolean) {
            if((boolean) o) {
                setPattern(p, null, sender, arg);
            } else {
                sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.chatColor.limit")));
            }
        } else {
            setPattern(p, (MessagePattern) o, sender, arg);
        }
    }

    private void setPattern(PlayerSender p, MessagePattern o, CommandSender sender, String arg) {
        String t;
        if(o != null) {
            p.setMessagePattern(o);
            t = "set";
        } else {
            p.setMessagePattern(null);
            t = "off";
        }

        p.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatColor." + t + "-self").replace("%color%", arg)));
        if(p != sender) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatColor." + t + "-others").replace("%player%", p.getName()).replace("%color%", arg)));
        }
    }

    private Object read(CommandSender sender, String s, boolean unlimited) {
        if(ChatColorProvider.getDisableArgument().equalsIgnoreCase(s)) {
            return true;
        }

        return unlimited ? readU(s) : readL(sender, s);
    }

    private MessagePattern readU(String s) {
        MessagePattern mp = ChatColorProvider.getMessagePatterns().get(s);
        if(mp != null) {
            return mp;
        }

        try {
            mp = ChatUtils.getPatternReader().read(s);
            if(mp != null) {
                return mp;
            }
        } catch (Exception ignored) {}

        return new ColorPattern(s);
    }

    private Object readL(CommandSender sender, String s) {
        String c = s.replace("§", "").replace("&", "");

        for(ChatColorProvider.LimitedPermission perm : ChatColorProvider.getLimitedPermissions().values()) {
            if(sender.hasPermission("boster.chat.chatcolor." + perm.getName())) {
                if(perm.getColorCodes().contains(c)) {
                    return new ColorPattern(c);
                }

                MessagePattern mp = perm.getPatterns().get(s);
                if(mp != null) {
                    return mp;
                }

                MessagePattern mo = perm.getOthers().get(s);
                if(mo != null) {
                    return mo;
                }
            }
        }

        return false;
    }
}
