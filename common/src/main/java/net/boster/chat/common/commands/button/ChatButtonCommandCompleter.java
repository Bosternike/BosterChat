package net.boster.chat.common.commands.button;

import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.button.ButtonBoundActions;
import net.boster.chat.common.commands.ChatCommandWrapper;
import net.boster.chat.common.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChatButtonCommandCompleter extends ChatCommandWrapper {

    public ChatButtonCommandCompleter(@NotNull BosterChatPlugin plugin) {
        super(plugin, "button-cmd-completer");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(args.length == 0) return;

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            return;
        }

        ButtonBoundActions.CompletableAction c = sender.getButtonBoundActions().get(id);
        if(c != null) {
            c.complete();
        }
    }
}
