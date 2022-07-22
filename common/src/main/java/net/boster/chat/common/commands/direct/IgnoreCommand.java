package net.boster.chat.common.commands.direct;

import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.commands.ChatCommandWrapper;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class IgnoreCommand extends ChatCommandWrapper {

    public IgnoreCommand(@NotNull BosterChatPlugin plugin, @NotNull ConfigurationSection section) {
        super(plugin, section);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(!(sender instanceof PlayerSender)) return;
        if(!checkPermission(sender)) return;

        ConfigurationSection c = plugin.getDirectSettingsFile().getConfig();
        PlayerSender p = (PlayerSender) sender;

        if(args.length == 0) {
            p.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.ignore.usage")));
            return;
        }

        if(p.getName().equals(args[0])) {
            p.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.ignore.self")));
            return;
        }

        if(p.getDirectSettings().isIgnored(args[0])) {
            p.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.ignore.already").replace("%name%", args[0])));
            return;
        }

        if(plugin.getPlayer(args[0]) == null) {
            p.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.ignore.noPlayer").replace("%name%", args[0])));
            return;
        }

        p.getDirectSettings().ignore(args[0]);
        p.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.ignore.success").replace("%name%", args[0])));
    }
}
