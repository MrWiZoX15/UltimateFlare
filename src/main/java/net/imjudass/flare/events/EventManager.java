package net.imjudass.flare.events;


import net.imjudass.flare.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    public void registerEvents(final Main pl) {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(pl), pl);
    }

}
