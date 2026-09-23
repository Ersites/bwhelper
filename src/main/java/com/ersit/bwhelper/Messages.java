package com.ersit.bwhelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

public class Messages {
    public static String gameBegin() {
        JsonObject json = new JsonObject();

        json.addProperty("type", "game_begin");

        return json.toString();
    }

    public static String playerList() {
        GuiPlayerTabOverlay tab = Minecraft.getMinecraft().ingameGUI.getTabList();

        JsonObject json = new JsonObject();
        json.addProperty("type", "player_list");
        JsonArray players = new JsonArray();

        for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap()) {
            String tabName = tab.getPlayerName(playerInfo);
            String username = playerInfo.getGameProfile().getName();

            JsonObject player = new JsonObject();

            player.addProperty("name", username);
            player.addProperty("formatted", tabName);

            players.add(player);
        }

        json.add("players", players);

        return json.toString();
    }

    public static String chatMessage(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "chat_message_formatted");
        json.addProperty("message", message);

        return json.toString();
    }

    public static String connected() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "connected");

        return json.toString();
    }
}
