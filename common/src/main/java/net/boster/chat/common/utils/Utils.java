package net.boster.chat.common.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Base64;

public class Utils {

    public static String encode(Object s) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(s);
            outputStream.close();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T decode(@NotNull String s, @NotNull Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(s));
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        Object r = inputStream.readObject();
        inputStream.close();
        if(clazz.isInstance(r)) {
            return (T) r;
        } else {
            throw new ClassCastException("Class " + r.getClass().getName() + " can't be cast to " + clazz.getName());
        }
    }
}
