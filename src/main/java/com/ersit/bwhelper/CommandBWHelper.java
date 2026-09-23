package com.ersit.bwhelper;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandBWHelper extends CommandBase {
    @Override
    public String getCommandName() {
        return "bwhelper";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bwhelper";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        BWHelper.messageSender.send(Messages.playerList());
    }
}
