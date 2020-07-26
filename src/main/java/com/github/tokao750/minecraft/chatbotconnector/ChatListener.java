package com.github.tokao750.minecraft.chatbotconnector;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.java_websocket.client.WebSocketClient;

public class ChatListener implements Listener {

	private final WebSocketClient client;
	
    public ChatListener(WebSocketClient client) {
        this.client = client;
    }
    
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		// Prepare variables for JSON
		String username = Utils.sanitizeStringForJson(event.getPlayer().getName());
		String message = Utils.sanitizeStringForJson(event.getMessage());
		
		// Constructing JSON (done manually to keep lightweight)
		String json = String.format("{\"user\":\"%s\",\"msg\":\"%s\"}", username, message);

		// Send messages from chat to websocket
		client.send(json);
	}
}
