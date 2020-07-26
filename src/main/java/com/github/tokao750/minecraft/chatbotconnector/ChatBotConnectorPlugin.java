package com.github.tokao750.minecraft.chatbotconnector;

import java.net.URISyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.client.WebSocketClient;

public final class ChatBotConnectorPlugin extends JavaPlugin {
	
	private WebSocketClient client;
	
	public ChatBotConnectorPlugin() {
		Utils.setPluginName(this.getName());
		
		try {
			// Create websocket to bot server
			this.client = new ChatBotWebSocketClient(this);
		} catch (URISyntaxException ex) {
			Utils.warn("websocket-uri from config.yml could not be parsed as a URI reference.");
		}
		
	}
	
	@Override
	public void onEnable() {
		// Save default configuration if missing
		this.saveDefaultConfig();

		// Connect websocket
		this.client.connect();
		
		// Register chat listener
		Bukkit.getPluginManager().registerEvents(new ChatListener(this.client), this);
	}
	
	@Override
	public void onDisable() {
		// Close websocket when plugin is disabled (code 1000 = normal closure)
		this.client.close(1000);
	}
}
