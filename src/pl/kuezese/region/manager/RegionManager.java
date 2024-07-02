package pl.kuezese.region.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.World;
import pl.kuezese.region.config.Config;
import pl.kuezese.region.object.Region;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RegionManager {

    private final Set<Region> regions;

    public RegionManager() {
        this.regions = ConcurrentHashMap.newKeySet();
    }

    public Region find(String name, World world) {
        return this.regions.stream().filter(region -> region.getName().equalsIgnoreCase(name) && region.getWorld() == world).findFirst().orElse(null);
    }

    public Region find(Location loc) {
        return this.regions.stream().filter(region -> region.isIn(loc)).max(Comparator.comparingInt(Region::getPriority)).orElse(null);
    }

    public void delete(Region region) {
        this.regions.remove(region);
    }

    public void load(Config config) {
        try {
            JsonObject object = new JsonParser().parse(config.data).getAsJsonObject();
            if (!object.has("regions")) {
                return;
            }
            JsonElement regions = object.get("regions");
            for (JsonElement element : regions.getAsJsonArray()) {
                Region region = new Region(element.getAsJsonObject());
                if (region.getWorld() != null) {
                    this.regions.add(region);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Config config) {
        JsonObject jsonObject = new JsonObject();
        JsonArray array = new JsonArray();
        this.regions.stream().map(region -> new JsonParser().parse(region.toJson()).getAsJsonObject()).forEach(array::add);
        jsonObject.add("regions", array);
        config.data = jsonObject.toString();
        config.save();
    }

    public Set<Region> getRegions() {
        return this.regions;
    }
}
