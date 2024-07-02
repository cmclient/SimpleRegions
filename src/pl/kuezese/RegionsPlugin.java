package pl.kuezese;

import org.bukkit.plugin.java.*;
import pl.kuezese.region.*;

public class RegionsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new SimpleRegions(this).onEnable();
    }
}
