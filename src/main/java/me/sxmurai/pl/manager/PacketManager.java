package me.sxmurai.pl.manager;

import me.sxmurai.pl.PacketLogger;
import net.minecraft.network.Packet;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class PacketManager {
    public void onSent(Packet<?> packet) {
        if (!PacketLogger.instance.logClientPackets) {
            return;
        }

        if (PacketLogger.instance.logInChat) {
            this.logToChat(packet);
        }

        if (PacketLogger.instance.logToFile) {
            this.logToFile(packet);
        }
    }

    public void onReceived(Packet<?> packet) {
        if (!PacketLogger.instance.logServerPackets) {
            return;
        }

        if (PacketLogger.instance.logInChat) {
            this.logToChat(packet);
        }

        if (PacketLogger.instance.logToFile) {
            this.logToFile(packet);
        }
    }

    private void logToChat(Packet<?> packet) {
        PacketLogger.mc.player.sendMessage(new TextComponentString(this.getData(packet)));
    }

    private void logToFile(Packet<?> packet) {
        String text = "";

        try {
            text = String.join("\n", Files.readAllLines(PacketLogger.instance.logFile));
        } catch (IOException ignored) { }

        text += this.getData(packet);

        try {
            Files.write(PacketLogger.instance.logFile, Collections.singletonList(text), StandardCharsets.UTF_8, Files.exists(PacketLogger.instance.logFile) ? StandardOpenOption.WRITE : StandardOpenOption.CREATE);
        } catch (IOException ignored) { }
    }

    private String getData(Packet<?> packet) {
        StringBuilder builder = new StringBuilder();

        builder.append("----------------");
        builder.append("\n\n");
        builder.append("Packet logged:");
        builder.append(" ");
        builder.append(packet.getClass().getTypeName().replace("$", "."));
        builder.append("\n\n");

        for (Field field : packet.getClass().getDeclaredFields()) {
            builder.append("-");
            builder.append(" ");
            builder.append(field.getName());
            builder.append(" ");
            builder.append("(");

            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            try {
                builder.append(field.get(packet));
            } catch (IllegalAccessException e) {
                builder.append("Could not get field");
            }

            field.setAccessible(accessible);

            builder.append(")");
            builder.append("\n");
        }

        builder.append("\n\n");
        builder.append("----------------");

        return builder.toString();
    }
}
