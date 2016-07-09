package com.algorithmicsanonymous.forgewhitelist.common.thread;

import com.algorithmicsanonymous.forgewhitelist.ForgeWhitelist;
import com.algorithmicsanonymous.forgewhitelist.common.config.Config;
import com.algorithmicsanonymous.forgewhitelist.common.util.LogHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ThreadCheckWhitelist implements Runnable {

    private final GameProfile profile;

    public ThreadCheckWhitelist(GameProfile profile) {
        this.profile = profile;
    }


    @Override
    public void run() {
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        if(playerList.canSendCommands(profile) || playerList.getWhitelistedPlayers().isWhitelisted(profile)) {
            LogHelper.info(String.format("Letting %s (%s) join. Player is OP or manually whitelisted.", profile.getName(), profile.getId()));
            return;
        }

        if(Config.serverLockStatus) {
            ForgeWhitelist.KICK_LIST.add(profile.getId());
        }

        if(ForgeWhitelist.WHITELIST_CACHE.contains(profile.getId())) return;

        ForgeWhitelist.KICK_LIST.add(profile.getId());
    }
}
