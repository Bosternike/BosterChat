package net.boster.chat.bukkit.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReflectionUtils {

    @Getter @NotNull protected static final Map<String, Class<?>> classCache = new HashMap<>();
    @Getter @NotNull protected static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    @Getter protected static final int versionInt;

    private static Method playerHandle;
    private static Field playerConnection;
    private static Method sendPacket;

    private static SimpleCommandMap commandMap;

    static {
        versionInt = Integer.parseInt(version.split("_")[1]);

        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap) f.get(Bukkit.getServer());

            if(versionInt < 17) {
                classCache.put("Packet", Class.forName("net.minecraft.server." + version + ".Packet"));
                classCache.put("PacketPlayOutChat", Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat"));
                Class<?> icbcClass = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
                Class<?> chatSerializerClass = null;
                for(Class<?> clazz : icbcClass.getClasses()) {
                    if (clazz.getName().contains("ChatSerializer")) {
                        chatSerializerClass = clazz;
                    }
                }

                classCache.put("ChatSerializer", chatSerializerClass);
                if (versionInt >= 11) {
                    classCache.put("ChatMessageType", Class.forName("net.minecraft.server." + version + ".ChatMessageType"));
                }

                classCache.put("IChatBaseComponent", icbcClass);

                classCache.put("Packet", Class.forName("net.minecraft.server." + version + ".Packet"));
                classCache.put("EntityPlayer", Class.forName("net.minecraft.server." + version + ".EntityPlayer"));
                classCache.put("PlayerConnection", Class.forName("net.minecraft.server." + version + ".PlayerConnection"));

                classCache.put("CraftServer", Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer"));
                classCache.put("CraftPlayer", Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer"));

                playerHandle = classCache.get("CraftPlayer").getMethod("getHandle");
                playerConnection = classCache.get("EntityPlayer").getField("playerConnection");
                sendPacket = classCache.get("PlayerConnection").getMethod("sendPacket", classCache.get("Packet"));
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(@NotNull Player p, @NotNull Object packet) {
        try {
            Object np = playerHandle.invoke(p);
            Object c = playerConnection.get(np);
            sendPacket.invoke(c, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendActionbar(Player p, String message) {
        try {
            Object chat = classCache.get("ChatSerializer").getDeclaredMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\"}");
            if (versionInt < 11) {
                Object packet = classCache.get("PacketPlayOutChat").getConstructor(classCache.get("IChatBaseComponent"), byte.class).newInstance(chat, (byte) 2);
                sendPacket(p, packet);
            } else {
                Class<?> pc = classCache.get("PacketPlayOutChat");
                Class<?> chatMessageTypeClass = classCache.get("ChatMessageType");
                Method chatMessageTypeMethod = chatMessageTypeClass.getMethod("valueOf", String.class);
                Object chatMessageTypeValue = chatMessageTypeMethod.invoke(null, "GAME_INFO");
                Object packet;
                Constructor<?> con;
                if (versionInt < 16) {
                    con = pc.getConstructor(classCache.get("IChatBaseComponent"), chatMessageTypeClass);
                    packet = con.newInstance(chat, chatMessageTypeValue);
                } else {
                    con = pc.getConstructor(classCache.get("IChatBaseComponent"), chatMessageTypeClass, UUID.class);
                    packet = con.newInstance(chat, chatMessageTypeValue, p.getUniqueId());
                }

                sendPacket(p, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerCommand(@NotNull Command command) {
        try {
            commandMap.register("bosterchat", command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterCommand(@NotNull Command command) {
        try {
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
            Map<String, Command> map = (Map<String, Command>) knownCommands.get(commandMap);
            map.remove(command.getName());
            map.remove( "bosterchat:" + command.getName());
            for(String alias : command.getAliases()) {
                map.remove(alias);
                map.remove("bosterchat:" + alias);
            }
            command.unregister(commandMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncCommands() {
        try {
            if(Version.getCurrentVersion().getVersionInteger() >= 12) {
                Method m = Bukkit.getServer().getClass().getMethod("syncCommands");
                m.setAccessible(true);
                m.invoke(Bukkit.getServer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
