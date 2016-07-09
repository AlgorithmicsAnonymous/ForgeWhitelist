package com.algorithmicsanonymous.forgewhitelist.common.command;

import com.algorithmicsanonymous.forgewhitelist.common.config.Config;
import com.algorithmicsanonymous.forgewhitelist.common.util.ConfigHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Configuration;

public class CommandLockServer extends CommandBase {
    @Override
    public String getCommandName() {
        return "fwlock";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/fwlock [true|false] | status";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 1) {
            if(args[0] == "status") {
                sender.addChatMessage(new TextComponentString("The server is currently " + (Config.serverLockStatus ? "locked" : "open") + "."));
            } else {
                try {
                    Config.serverLockStatus = parseBoolean(args[0]);

                    if(Config.rememberLastLockStatus) {
                        ConfigHelper.setBoolean(Config.configuration, "Locked", Configuration.CATEGORY_GENERAL, Config.serverLockStatus);
                    }
                    sender.addChatMessage(new TextComponentString("The server is now " + (Config.serverLockStatus ? "locked" : "open") + "."));
                } catch(CommandException ex) {
                    sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
                }
            }
        } else {
            sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
        }
    }
}
