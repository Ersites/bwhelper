package com.ersit.bwhelper;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatListener {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        String message_formatted = event.message.getFormattedText();
        BWHelper.messageSender.send(Messages.chatMessage(message_formatted));

        if (message.contains("Goodluck with your BedWars Game")) {
            schedulePlayerList();
        }
    }

    private void schedulePlayerList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        BWHelper.messageSender.send(
                                Messages.playerList()
                        );
                    }
                });
            }
        }, "BWHelper-PlayerListDelay").start();
    }
}
