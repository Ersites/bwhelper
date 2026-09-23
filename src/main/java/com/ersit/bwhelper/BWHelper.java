package com.ersit.bwhelper;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BWHelper.MODID, version = BWHelper.VERSION)
public class BWHelper
{
    public static final String MODID = "bwhelper";
    public static final String VERSION = "0.1.0";
    public static MessageSender messageSender;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("Initializing BWHelper");
        messageSender = new MessageSender("127.0.0.1", 52138);
        messageSender.start();

        ClientCommandHandler.instance.registerCommand(new CommandBWHelper());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
    }
}
