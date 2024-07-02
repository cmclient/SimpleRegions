package pl.kuezese.region.helper;

import org.bukkit.*;

public final class ChatHelper {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s).replace(">>", "»");
    }
}
