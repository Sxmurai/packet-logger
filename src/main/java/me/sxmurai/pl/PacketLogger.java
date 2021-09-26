package me.sxmurai.pl;

import me.sxmurai.pl.manager.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

@Mod(modid = PacketLogger.MOD_ID, name = PacketLogger.MOD_NAME, version = PacketLogger.MOD_VER)
public class PacketLogger {
    public static final String MOD_ID = "packet_logger";
    public static final String MOD_NAME = "Packet Logger";
    public static final String MOD_VER = "1.0.0";

    @Mod.Instance
    public static PacketLogger instance;

    public static Minecraft mc;

    public static Logger LOGGER = LogManager.getLogger(PacketLogger.class);
    public static PacketManager packetManager;

    private Configuration config;
    public Path logFile;

    public boolean logClientPackets;
    public boolean logServerPackets;
    public boolean logToFile;
    public boolean logInChat;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER.info("Loading configurations for {} v{}...", MOD_NAME, MOD_VER);

        this.config = new Configuration(new File(event.getModConfigurationDirectory(), "packet_logger.cfg"));

        if (!Files.exists(Paths.get(Paths.get("").toString(), "packet_logger"))) {
            try {
                Files.createDirectory(Paths.get(Paths.get("").toString(), "packet_logger"));
            } catch (IOException e) {
                LOGGER.info("Couldn't create packet_logger directory... Wtf");
            }
        }

        this.logFile = Paths.get(Paths.get("").toString(), "packet_logger", "log_" + System.currentTimeMillis() + ".txt");
        this.load();

        if (this.logToFile) {
            try {
                LOGGER.info("Creating file at {}", this.logFile.toString());
                Files.write(this.logFile, Collections.singletonList(""), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                LOGGER.warn("Could not create log file at {}", this.logFile.toString());
            }
        }
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        mc = Minecraft.getMinecraft();

        LOGGER.info("Initializing packet manager...");

        packetManager = new PacketManager();

        LOGGER.info("Welcome to {} v{}", MOD_NAME, MOD_VER);
    }

    private void load() {
        this.logClientPackets = this.config.getBoolean("Log Client Packets", "Default", true, "If to log client sided packets");
        this.logServerPackets = this.config.getBoolean("Log Server Packets", "Default", true, "If to log server sided packets");
        this.logToFile = this.config.getBoolean("Log To File", "Default", true, "If to log the packets to a file");
        this.logInChat = this.config.getBoolean("Log In Chat", "Default", true, "If to log the packets in chat");

        this.config.save();
    }
}
