package net.imjudass.flare;

import net.imjudass.flare.cmds.FlareCommand;
import net.imjudass.flare.events.EventManager;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {

    public static Main instance;
    public ArrayList<Material> itemsList = new ArrayList<>();

    /*----------------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------------*/

    public void saveCmds() {
        getCommand("flaregive").setExecutor(new FlareCommand(this));
    }

    @Override
    public void onEnable() {
        instance = this;

        super.onEnable();

        getServer().getPluginManager().registerEvents(this, this);

        new EventManager().registerEvents(this);

        for (String itemKey : getConfig().getConfigurationSection("Items").getKeys(false)) {
            Material mat = Material.getMaterial(itemKey);
            itemsList.add(mat);
        }

        saveCmds();
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {

        super.onDisable();
    }

    /*----------------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------------*/

    public String getMesssage(String path) {
        return getConfig().getString(path).replaceAll("&", "§");
    }

}

