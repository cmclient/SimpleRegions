package pl.kuezese.region;

import org.bukkit.plugin.java.*;
import pl.kuezese.region.config.*;
import pl.kuezese.region.manager.*;
import pl.kuezese.region.command.*;
import pl.kuezese.region.listener.*;

public class SimpleRegions {

    private final JavaPlugin plugin;
    private final Config config;
    private final RegionManager regionManager;
    
    public SimpleRegions(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = new Config();
        this.regionManager = new RegionManager();
    }

    public void onEnable() {
        this.config.load(this.plugin);
        this.regionManager.load(this.config);
        this.plugin.getCommand("region").setExecutor(new RegionCommand(this.config, this));
        this.plugin.getServer().getPluginManager().registerEvents(new RegionListener(this), this.plugin);
    }

    public RegionManager getRegionManager() {
        return this.regionManager;
    }
}
