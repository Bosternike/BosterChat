package net.boster.chat.common.provider;

import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.placeholders.ChatMessagePlaceholder;
import net.boster.chat.common.log.ChatLog;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatLogProvider implements ChatLog {

    private final File file;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public ChatLogProvider(@NotNull File folder) {
        this.file = new File(folder, "chat-log.txt");
        check();
    }

    @Override
    public void log(@NotNull PlayerSender sender, @NotNull Chat chat, @NotNull String message, @Nullable List<ChatMessagePlaceholder> placeholders) {
        if(chat.getChatSettings().getLogFormat() == null) return;

        check();

        String date = dateFormat.format(new Date());
        try {
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);

            String s = chat.getChatSettings().getLogFormat().replace("%date%", date).replace("%chat%", chat.getName())
                    .replace("%player%", sender.getName()).replace("%message%", message);

            if(placeholders != null) {
                for(ChatMessagePlaceholder mp : placeholders) {
                    s = s.replace(mp.getPlaceholder(), mp.getReplacement());
                }
            }

            pw.println(s);
            pw.flush();
            pw.close();
        } catch(IOException ignored) {}
    }

    private void check() {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
