package net.boster.chat.common.commands.subcommands;

import net.boster.chat.common.chat.component.ChatComponent;
import net.boster.chat.common.chat.message.MessageRowBuilder;
import net.boster.chat.common.commands.SubCommand;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import net.boster.chat.common.utils.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerInfoCommand extends SubCommand {

    private final ChatComponent showButton;
    private final ChatComponent resetButton;
    private final ChatComponent enableButton;
    private final ChatComponent disableButton;

    public PlayerInfoCommand() {
        super("profile", "player-info");

        this.showButton = new ChatComponent(color("&a&l[SHOW]")).setHover(color("&aClick here to display"));
        this.resetButton = new ChatComponent(color("&c&l[RESET]")).setHover(color("&cClick here to reset"));
        this.enableButton = new ChatComponent(color("&a&l[ENABLE]")).setHover(color("&aClick here to enable"));
        this.disableButton = new ChatComponent(color("&c&l[DISABLE]")).setHover(color("&cClick here to disable"));
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(args.length == 0) {
            sendUsage(sender);
            return;
        }

        PlayerSender p = plugin.getPlayer(args[0]);
        if(p == null) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.nullPlayer").replace("%arg%", args[0])));
            return;
        }

        sender.sendMessage(color("&7-------- &d&l%arg%'s info &7--------").replace("%arg%", args[0]));

        MessageRowBuilder b1 = new MessageRowBuilder().appendComponent(color("&7Disabled chats: &d" + p.getDisabledChats().size()) + " ")
                .append(showButton.clone().attachButton(sender.getButtonBoundActions().addAction(() -> {
            String s = TextUtils.buildOneLineList(p.getDisabledChats(), "&7,&b ", "&cempty");
            sender.sendMessage(color("&7Disabled chats of &d" + args[0] + "&7:&b " + s));
        }))).appendComponent(" ").append(resetButton.clone().attachButton(sender.getButtonBoundActions().addAction(() -> {
            p.getDisabledChats().clear();
            sender.sendMessage(color("&7Disabled chats list of &d" + args[0] + "&f has been reset."));
        })));

        MessageRowBuilder b2 = new MessageRowBuilder().appendComponent(color("&7Message pattern (ChatColor): &d") +
                (p.getMessagePattern() != null ? p.getMessagePattern().getInitialPattern() : "\u00a7cnone"))
                .appendComponent(" ")
                .append(resetButton.clone().attachButton(sender.getButtonBoundActions().addAction(() -> {
                    p.setMessagePattern(null);
                    sender.sendMessage(color("&7Message pattern of &d" + args[0] + "&f has been reset."));
                })));

        MessageRowBuilder b3 = new MessageRowBuilder().appendComponent(color("&7Direct messages enabled: " +
                (p.getDirectSettings().isEnabled() ? "&ayes" : "&cno")))
                .appendComponent(" ")
                .append(p.getDirectSettings().isEnabled() ?
                        disableButton.clone().attachButton(p.getButtonBoundActions().addAction(() -> {
                            p.getDirectSettings().setEnabled(false);
                            sender.sendMessage(color("&7Direct messages of &d" + args[0] + "&f have been &cdisabled&7."));
                        }))
                        :
                        enableButton.clone().attachButton(p.getButtonBoundActions().addAction(() -> {
                            p.getDirectSettings().setEnabled(true);
                            sender.sendMessage(color("&7Direct messages of &d" + args[0] + "&f have been &aenabled&7."));
                        })));

        MessageRowBuilder b4 = new MessageRowBuilder().appendComponent(color("&7Ignored players: &d" + p.getDirectSettings().getIgnoring().size()))
                .appendComponent(" ")
                .append(showButton.clone().attachButton(p.getButtonBoundActions().addAction(() -> {
                    String s = TextUtils.buildOneLineList(p.getDirectSettings().getIgnoring(), "&7,&b ", "&cempty");
                    sender.sendMessage(color("&7Ignore list of &d" + args[0] + "&7:&b " + s));
                })))
                .appendComponent(" ")
                .append(resetButton.clone().attachButton(p.getButtonBoundActions().addAction(() -> {
                    p.getDirectSettings().getIgnoring().clear();
                    sender.sendMessage(color("&7Ignore list of &d" + args[0] + "&f has been reset."));
                })));

        p.sendMessage(b1.createTextComponentArray());
        p.sendMessage(b2.createTextComponentArray());
        p.sendMessage(b3.createTextComponentArray());
        p.sendMessage(b4.createTextComponentArray());

        sender.sendMessage(color("&7-------- &d&l%arg%'s info &7--------").replace("%arg%", args[0]));
    }

    private String color(String s) {
        return ChatUtils.toColor(s);
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return plugin.getPlayers().stream().map(CommandSender::getName).collect(Collectors.toList());
    }
}
