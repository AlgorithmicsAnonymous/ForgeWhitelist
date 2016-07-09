package com.algorithmicsanonymous.forgewhitelist.common.thread;

import com.algorithmicsanonymous.forgewhitelist.ForgeWhitelist;
import com.algorithmicsanonymous.forgewhitelist.common.config.Config;
import com.algorithmicsanonymous.forgewhitelist.common.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ThreadKickPlayers implements Runnable{
    private static Boolean isRunning = false;

    @Override
    public void run() {
        while(!ForgeWhitelist.KICK_LIST.isEmpty() && !isRunning) {
            isRunning = true;
            EntityPlayerMP playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(ForgeWhitelist.KICK_LIST.poll());

            if(playerMP == null) continue;

            playerMP.connection.kickPlayerFromServer(Config.serverLockStatus ? "The server is currently locked!" : "You are not whitelisted!");
        }

        isRunning = false;
    }
}
