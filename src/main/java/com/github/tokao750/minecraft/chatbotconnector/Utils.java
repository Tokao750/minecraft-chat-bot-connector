package com.github.tokao750.minecraft.chatbotconnector;

public class Utils {
	
	// Create private singleton for non-static usage
	private static Utils utils = new Utils();
	
	// Make constructor private to prevent external use
	private Utils() { }
	
	
	
	// Non-static field to allow dynamic retrieval of plugin name
	private String pluginName;
	
	// Set plugin name (only meant for use once on startup)
	public static void setPluginName(String pluginName) {
		utils.pluginName = pluginName;
	}
	
	
	
	// Log INFO messages to console
	public static void info(String message) {
		System.out.println(String.format("[%s] %s", utils.pluginName, message));
	}
	
	// Log WARN messages to console
	public static void warn(String message) {
		System.err.println(String.format("[%s] %s", utils.pluginName, message));
	}
	
	// Create a new thread to run a task after a delay
	public static void setTimeout(Runnable runnable, int delay){
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e){
				Utils.warn(e.getMessage());
			}
		}).start();
	}
	
	// Returns true if a string is either null or empty
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
	// Sanitize a string for JSON
	public static String sanitizeStringForJson(String string) {
		return string
				.replace("\\", "\\\\") // Escape backslash (needs to be first as each replacement adds additional backslashes)
				.replace("\"", "\\\""); // Escape quotation mark
	}
}
