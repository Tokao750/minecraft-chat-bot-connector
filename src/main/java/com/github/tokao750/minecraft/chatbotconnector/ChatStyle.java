package com.github.tokao750.minecraft.chatbotconnector;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;

public class ChatStyle {
	
	// Wether the "reset" style has been set and previous styles should be cleared
	private boolean reset = false;
	// Color of chat message
	private ChatColor color;
	// Format(s) of chat message
	private LinkedList<ChatColor> formats = new LinkedList<ChatColor>();

	// Create ChatStyle object from a single style string
	public ChatStyle(String style) {
		addChatStyle(style);
	}

	// Create ChatStyle object from a list style strings
	public ChatStyle(List<String> styles) {
		for (String style : styles) {
			addChatStyle(style);
		}
	}
	
	// Compact contained styles to a single string with correct order
	@Override
	public String toString() {
		String ret = "";
		
		// Set reset at beginning to only reset previously set styles
		if (reset) {
			ret += ChatColor.RESET;
		}
		
		if (color != null) {
			ret += color;
		}

		// Set formats at end as subsequent colors disable previous formats
		if (formats != null && !formats.isEmpty()) {
			for (ChatColor format : formats) {
				ret += format;
			}
		}
		
		// Return the correctly ordered and filtered format string
		return ret;
	}
	
	// Add a style to the ChatStyle object by string
	private void addChatStyle(String style) {
		// Parse the string as a ChatColor object
		ChatColor parsed = parseChatColor(style);
		if (parsed == null) {
			// Parse failed
			Utils.warn(String.format("Chat style: %s not recognized.", style));
		} else if (parsed.isFormat()) {
			// ChatColor object is a format style
			formats.add(parsed);
		} else if (parsed == ChatColor.RESET) {
			// ChatColor object is the "reset" style
			reset = true;
		} else {
			// ChatColor object is a color style
			if (color != null) {
				// A color has already been set
				Utils.info(String.format("Multiple colors set. As only 1 color can be displayed %s has been ignored", color.name()));
			}
			
			// Use last color specified
			color = parsed;
		}
	}
	
	// Convert string to ChatColor object
	private ChatColor parseChatColor(String string) {
		switch (string.toUpperCase(Locale.ROOT)) {
			case "AQUA":
				return ChatColor.AQUA;
			case "BLACK":
				return ChatColor.BLACK;
			case "BLUE":
				return ChatColor.BLUE;
			case "BOLD":
				return ChatColor.BOLD;
			case "DARKAQUA":
				return ChatColor.DARK_AQUA;
			case "DARKBLUE":
				return ChatColor.DARK_BLUE;
			case "DARKGRAY":
				return ChatColor.DARK_GRAY;
			case "DARKGREEN":
				return ChatColor.DARK_GREEN;
			case "DARKPURPLE":
				return ChatColor.DARK_PURPLE;
			case "DARKRED":
				return ChatColor.DARK_RED;
			case "GOLD":
				return ChatColor.GOLD;
			case "GRAY":
				return ChatColor.GRAY;
			case "GREEN":
				return ChatColor.GREEN;
			case "ITALIC":
				return ChatColor.ITALIC;
			case "LIGHTPURPLE":
				return ChatColor.LIGHT_PURPLE;
			case "MAGIC":
				return ChatColor.MAGIC;
			case "RED":
				return ChatColor.RED;
			case "RESET":
				return ChatColor.RESET;
			case "STRIKE":
				return ChatColor.STRIKETHROUGH;
			case "UNDERLINE":
				return ChatColor.UNDERLINE;
			case "WHITE":
				return ChatColor.WHITE;
			case "YELLOW":
				return ChatColor.YELLOW;
			default:
				return null;
		}
	}
}
