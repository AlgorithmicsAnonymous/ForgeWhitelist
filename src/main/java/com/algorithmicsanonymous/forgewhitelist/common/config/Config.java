package com.algorithmicsanonymous.forgewhitelist.common.config;

import com.algorithmicsanonymous.forgewhitelist.ModInfo;
import com.algorithmicsanonymous.forgewhitelist.ForgeWhitelist;
import com.algorithmicsanonymous.forgewhitelist.common.util.ConfigHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.Arrays;

public class Config {
    public static Configuration configuration;

    public static Boolean serverLockStatus;
    public static Boolean rememberLastLockStatus;

    public Config() {
    }

    public static Configuration initConfig(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
        return configuration;
    }

    public static void loadConfiguration() {
        rememberLastLockStatus = ConfigHelper.getBoolean(configuration, "RememberLockStatus", Configuration.CATEGORY_GENERAL, false, "Should the server remember lock status between restarts?");
        serverLockStatus = ConfigHelper.getBoolean(configuration, "Locked", Configuration.CATEGORY_GENERAL, false, "Should the server allow people to join on start? (OPs & manually whitelisted people are exempt from this)");

        configuration.save();
    }

    @SubscribeEvent
    public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ModInfo.MOD_ID)) {
            Config.loadConfiguration();
        }
    }
}