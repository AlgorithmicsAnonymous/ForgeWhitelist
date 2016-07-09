package com.algorithmicsanonymous.forgewhitelist;

import com.algorithmicsanonymous.forgewhitelist.common.command.CommandLockServer;
import com.algorithmicsanonymous.forgewhitelist.common.config.Config;
import com.algorithmicsanonymous.forgewhitelist.common.thread.ThreadCheckWhitelist;
import com.algorithmicsanonymous.forgewhitelist.common.thread.ThreadKickPlayers;
import com.algorithmicsanonymous.forgewhitelist.common.util.CachedSet;
import com.algorithmicsanonymous.forgewhitelist.common.util.LogHelper;
import com.google.common.base.Stopwatch;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, certificateFingerprint = ModInfo.FINGERPRINT, dependencies = ModInfo.DEPENDENCIES, version = ModInfo.VERSION_BUILD, serverSideOnly = true, acceptableRemoteVersions = "*")
public class ForgeWhitelist {
    @Mod.Instance(ModInfo.MOD_ID)
    public static ForgeWhitelist instance;

    public static CachedSet<UUID> WHITELIST_CACHE = new CachedSet<>(43200000);
    public static Queue<UUID> KICK_LIST = new ConcurrentLinkedQueue<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        LogHelper.info("Pre Initialization (Started)");

        Config.initConfig(event.getSuggestedConfigurationFile());

        MinecraftForge.EVENT_BUS.register(this);

        LogHelper.info("Pre Initialization (Ended after " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms)");
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLockServer());
    }

    @SubscribeEvent
    public void onConnectionFromClient(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        if(event.isLocal()) return;

        LogHelper.info(">>>>> Spawning new Checker thread");

        Thread t = new Thread(new ThreadCheckWhitelist(((NetHandlerPlayServer) event.getHandler()).playerEntity.getGameProfile()));
        t.setName("FWCheckerThread");
        t.start();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        Thread t = new Thread(new ThreadKickPlayers());
        t.setName("FWKickThread");
        t.start();
    }

    @NetworkCheckHandler
    public boolean networkCheckHandler(Map<String, String> map, Side side)
    {
        return true;
    }
}
