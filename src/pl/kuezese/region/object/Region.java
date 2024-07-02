package pl.kuezese.region.object;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import pl.kuezese.region.type.Flag;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Region {

    private final String name;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;
    private int priority;
    private final Set<Flag> flags;
    private final String worldName;
    private final transient World world;

    public Region(String name, int minX, int maxX, int minY, int maxY, int minZ, int maxZ, World world) {
        this.name = name;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.priority = 0;
        this.flags = ConcurrentHashMap.newKeySet();
        this.worldName = world.getName();
        this.world = world;
    }

    public Region(JsonObject object) {
        this.name = object.get("name").getAsString();
        this.minX = object.get("minX").getAsInt();
        this.maxX = object.get("maxX").getAsInt();
        this.minY = object.get("minY").getAsInt();
        this.maxY = object.get("maxY").getAsInt();
        this.minZ = object.get("minZ").getAsInt();
        this.maxZ = object.get("maxZ").getAsInt();
        this.priority = object.get("priority").getAsInt();
        this.flags = ConcurrentHashMap.newKeySet();
        JsonArray flags = object.get("flags").getAsJsonArray();
        for (JsonElement flag : flags) {
            this.flags.add(Flag.valueOf(flag.getAsString()));
        }
        this.worldName = object.get("worldName").getAsString();
        this.world = Bukkit.getWorld(this.worldName);
    }

    public void setCoords(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public boolean isIn(Location loc) {
        if (loc.getWorld() != this.world) {
            return false;
        }
        int minx = Math.min(this.minX, this.maxX);
        int maxx = Math.max(this.minX, this.maxX);
        int miny = Math.min(this.minY, this.maxY);
        int maxy = Math.max(this.minY, this.maxY);
        int minz = Math.min(this.minZ, this.maxZ);
        int maxz = Math.max(this.minZ, this.maxZ);
        return loc.getBlockY() >= miny && loc.getBlockY() <= maxy && loc.getBlockZ() >= minz && loc.getBlockZ() <= maxz && loc.getBlockX() >= minx && loc.getBlockX() <= maxx;
    }

    public String getName() {
        return this.name;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMinZ() {
        return this.minZ;
    }

    public int getMaxZ() {
        return this.maxZ;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    public Set<Flag> getFlags() {
        return this.flags;
    }

    public World getWorld() {
        return this.world;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
