package com.github.tokao750.minecraft.chatbotconnector;

import java.net.URI;
import java.net.URISyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ChatBotWebSocketClient extends WebSocketClient {
	
	private final ChatBotConnectorPlugin plugin;
	
	// Configuration
	private String chatStyle;
	private String prefix;
	private int reconnectDelay;
	private int reconnectAttempts;
	
	// Internal
	private int failedAttempts = 0;

	public ChatBotWebSocketClient(ChatBotConnectorPlugin plugin) throws URISyntaxException {
		super(new URI(plugin.getConfig().getString("websocket-uri")));
		
		// Set directly from constructor parameter
		this.plugin = plugin;

		// Get config
		FileConfiguration config = plugin.getConfig();
		
		// Set directly from config
		this.reconnectDelay = config.getInt("reconnect.delay");
		this.reconnectAttempts = config.getInt("reconnect.attempts");
		
		// Format from config
		String rawPrefix = config.getString("prefix");
		this.prefix = Utils.isNullOrEmpty(rawPrefix) ? "" : rawPrefix + " ";
		
		// Parse from config
		ChatStyle style;
		if (config.isList("chat-style")) {
			style = new ChatStyle(config.getStringList("chat-style"));
		} else {
			style = new ChatStyle(config.getString("chat-style"));
		}
		this.chatStyle = style.toString();
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		Utils.info("New websocket connection opened");
		
		// Reset failed attempts
		failedAttempts = 0;
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		if (!remote && code == 1000) {
			// The websocket was closed normally from client-side. Don't reconnect.
			Utils.info("Websocket closed successfully");
			return;
		}
		
		// Don't log close events for reconnection attempts
		if (this.failedAttempts == 0) {
			// Build reason clause if present
			String reasonString = Utils.isNullOrEmpty(reason) ? "" : " Reason: " + reason;
			
			Utils.info(String.format("Websocket closed with exit code %s.%s", code, reasonString));
		}
		
		if (this.failedAttempts < this.reconnectAttempts) {
			Utils.info(String.format("Attempting to reconnect to websocket %s/%s", failedAttempts+1, reconnectAttempts));
			
			// Start reconnection timer
			Utils.setTimeout(() -> this.reconnect(), reconnectDelay);
		} else if (this.reconnectAttempts > 0) {
			// Maximum reconnection attempts has been reached
			Utils.warn("Failed to reconnect to websocket");
		}
		
		// Increment failed reconnection attempts
		this.failedAttempts++;
	}

	@Override
	public void onMessage(final String message) {
		// Schedule task to broadcast messages from websocket to server chat on next server tick (to prevent response appearing in chat before request)
		final String combinedMessage = this.chatStyle + this.prefix + message;
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getServer().broadcastMessage(combinedMessage);
			}
		}.runTask(plugin);
	}

	@Override
	public void onError(Exception ex) {
		Utils.warn("A websocket error occurred: " + ex.getMessage());
	}
}