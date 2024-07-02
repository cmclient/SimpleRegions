package pl.kuezese.region.command;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.region.SimpleRegions;
import pl.kuezese.region.config.Config;
import pl.kuezese.region.helper.ChatHelper;
import pl.kuezese.region.helper.StringHelper;
import pl.kuezese.region.manager.RegionManager;
import pl.kuezese.region.object.Region;
import pl.kuezese.region.type.Flag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegionCommand implements CommandExecutor {

    private final Config config;
    private final RegionManager regionManager;
    private final WorldEdit worldEdit;

    public RegionCommand(Config config, SimpleRegions simpleRegions) {
        this.config = config;
        this.regionManager = simpleRegions.getRegionManager();
        this.worldEdit = WorldEdit.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("sr.region")) {
            sender.sendMessage(ChatHelper.color("&8>> &7You do not have permission to use this command! &8(&csr.region&8)"));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatHelper.color("&8>> &7Correct usage: &6/region <define/redefine/delete/list/info/flag/setpriority>"));
            return true;
        }
        Player player = (Player) sender;
        String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "define": {
                if (args.length < 2) {
                    player.sendMessage(ChatHelper.color("&8>> &7Correct usage: &6/region define <name>"));
                    return true;
                }
                Region reg = this.regionManager.find(args[1], player.getWorld());
                if (reg != null) {
                    player.sendMessage(ChatHelper.color("&8>> &7Region &e" + args[1] + " &7in world &e" + player.getWorld().getName() + " &7already exists."));
                    return true;
                }
                com.sk89q.worldedit.regions.Region selection = this.selection(player);
                if (selection == null) {
                    player.sendMessage(ChatHelper.color("&8>> &7Please select an area first."));
                    return true;
                }
                Vector min = selection.getMinimumPoint();
                Vector max = selection.getMaximumPoint();
                if (min.getBlockY() == max.getBlockY()) {
                    player.sendMessage(ChatHelper.color("&8>> &cWarning: &7The minimum and maximum &eY &7level are the same."));
                }
                Region region = new Region(args[1], min.getBlockX(), max.getBlockX(), min.getBlockY(), max.getBlockY(), min.getBlockZ(), max.getBlockZ(), player.getWorld());
                this.regionManager.getRegions().add(region);
                this.regionManager.save(this.config);
                player.sendMessage(ChatHelper.color("&8>> &7Created region &e" + region.getName() + " &7in world &e" + player.getWorld().getName() + " &7minimum location: &8(" + region.getMinX() + ", " + region.getMinY() + ", " + region.getMinZ() + "&8) &7maximum location: &8(" + region.getMaxX() + ", " + region.getMaxY() + ", " + region.getMaxZ() + "&8)"));
                break;
            }
            case "redefine": {
                if (args.length < 2) {
                    player.sendMessage(ChatHelper.color("&8>> &7Correct usage: &6/region redefine <name>"));
                    return true;
                }
                Region region = this.regionManager.find(args[1], player.getWorld());
                if (region == null) {
                    player.sendMessage(ChatHelper.color("&8>> &7Region &e" + args[1] + " &7in world &e" + player.getWorld().getName() + " &7does not exist."));
                    return true;
                }
                com.sk89q.worldedit.regions.Region selection = this.selection(player);
                if (selection == null) {
                    player.sendMessage(ChatHelper.color("&8>> &7Please select an area."));
                    return true;
                }
                Vector min = selection.getMinimumPoint();
                Vector max = selection.getMaximumPoint();
                if (min.getBlockY() == max.getBlockY()) {
                    player.sendMessage(ChatHelper.color("&8>> &cWarning: &7The minimum and maximum &eY &7level are the same."));
                }
                region.setCoords(min.getBlockX(), max.getBlockX(), min.getBlockY(), max.getBlockY(), min.getBlockZ(), max.getBlockZ());
                this.regionManager.save(this.config);
                player.sendMessage(ChatHelper.color("&8>> &7Redefined region &e" + region.getName() + " &7in world &e" + player.getWorld().getName() + " &7minimum location: &8(" + region.getMinX() + ", " + region.getMinY() + ", " + region.getMinZ() + "&8) &7maximum location: &8(" + region.getMaxX() + ", " + region.getMaxY() + ", " + region.getMaxZ() + "&8)"));
                break;
            }
            case "delete": {
                if (args.length < 2) {
                    player.sendMessage(ChatHelper.color("&8>> &7Correct usage: &6/region delete <name>"));
                    return true;
                }
                Region region = this.regionManager.find(args[1], player.getWorld());
                if (region == null) {
                    player.sendMessage(ChatHelper.color("&8>> &7Region &e" + args[1] + " &7in world &e" + player.getWorld().getName() + " &7does not exist."));
                    return true;
                }
                this.regionManager.delete(region);
                this.regionManager.save(this.config);
                player.sendMessage(ChatHelper.color("&8>> &7Deleted region &e" + region.getName() + " &7in world &e" + player.getWorld().getName() + " &7minimum location: &8(" + region.getMinX() + ", " + region.getMinY() + ", " + region.getMinZ() + "&8) &7maximum location: &8(" + region.getMaxX() + ", " + region.getMaxY() + ", " + region.getMaxZ() + "&8)"));
                break;
            }
            case "list": {
                List<String> regions = new ArrayList<>();
                this.regionManager.getRegions().forEach(region -> regions.add(regions.contains(region.getName()) ? region.getName() + " &8(" + region.getWorld().getName() + "&8)&e" : region.getName()));
                player.sendMessage(ChatHelper.color("&8>> &7List of regions &8(" + regions.size() + "&8): &e" + StringHelper.join(regions, ", ")));
                break;
            }
            case "info": {
                Region region = (args.length >= 2) ? this.regionManager.find(args[1], player.getWorld()) : this.regionManager.find(player.getLocation());
                if (region == null) {
                    player.sendMessage((args.length >= 2) ? ChatHelper.color("&8>> &7Region &e" + args[1] + " &7in world &e" + player.getWorld().getName() + " &7does not exist.") : ChatHelper.color("&8>> &7There is no region at your location."));
                    return true;
                }
                player.sendMessage(ChatHelper.color("&8>> &7Information about region &e" + region.getName() + "&7:"));
                player.sendMessage(ChatHelper.color("&8>> &7World: &e" + player.getWorld().getName()));
                player.sendMessage(ChatHelper.color("&8>> &7Minimum location: &8(" + region.getMinX() + ", " + region.getMinY() + ", " + region.getMinZ() + "&8)"));
                player.sendMessage(ChatHelper.color("&8>> &7Maximum location: &8(" + region.getMaxX() + ", " + region.getMaxY() + ", " + region.getMaxZ() + "&8)"));
                player.sendMessage(ChatHelper.color("&8>> &7Priority: &e" + region.getPriority()));
                List<String> flags = region.getFlags().stream().map(Enum::name).collect(Collectors.toList());
                player.sendMessage(ChatHelper.color("&8>> &7Flags &8(" + flags.size() + ")&7: &e" + StringHelper.join(flags, ", ")));
                break;
            }
            case "flag": {
                if (args.length < 4) {
                    player.sendMessage(ChatHelper.color("&8>> &7Correct usage: &6/region flag <region> <add/remove> <flag>"));
                    return true;
                }
                Region region = this.regionManager.find(args[1], player.getWorld());
                if (region == null) {
                    player.sendMessage(ChatHelper.color("&8>> &7Region &e" + args[1] + " &7in world &e" + player.getWorld().getName() + "&7does not exist."));
                    return true;
                }
                String action = args[2];
                String flag = args[3];
                if (action.equalsIgnoreCase("add")) {
                    if (flag.equalsIgnoreCase("all")) {
                        region.getFlags().clear();
                        region.getFlags().addAll(Arrays.asList(Flag.values()));
                        player.sendMessage(ChatHelper.color("&8>> &7Added all flags to region &e" + region.getName()));
                    } else {
                        Flag flag2 = Flag.get(flag);
                        if (flag2 == null) {
                            player.sendMessage(ChatHelper.color("&8>> &7Flag named &e" + flag + " &cdoes not exist&7."));
                            player.sendMessage(ChatHelper.color("&8>> &7Available flags: &e" + Flag.all()));
                            return true;
                        }
                        if (region.getFlags().contains(flag2)) {
                            player.sendMessage(ChatHelper.color("&8>> &7Region &e" + region.getName() + " &7already contains flag &e" + flag));
                            return true;
                        }
                        region.getFlags().add(flag2);
                        player.sendMessage(ChatHelper.color("&8>> &7Added flag &e" + flag + " &7to region &e" + region.getName()));
                    }
                } else if (action.equalsIgnoreCase("remove")) {
                    if (flag.equalsIgnoreCase("all")) {
                        region.getFlags().clear();
                        player.sendMessage(ChatHelper.color("&8>> &7Removed all flags from region &e" + region.getName()));
                    } else {
                        Flag flag2 = Flag.get(flag);
                        if (flag2 == null) {
                            player.sendMessage(ChatHelper.color("&8>> &7Flag named &e" + flag + " &cdoes not exist&7."));
                            player.sendMessage(ChatHelper.color("&8>> &7Available flags: &e" + Flag.all()));
                            return true;
                        }
                        if (!region.getFlags().contains(flag2)) {
                            player.sendMessage(ChatHelper.color("&8>> &7Region &e" + region.getName() + " &7does not contain flag &e" + flag));
                            return true;
                        }
                        region.getFlags().remove(flag2);
                        player.sendMessage(ChatHelper.color("&8>> &7Removed flag &e" + flag + " &7from region &e" + region.getName()));
                    }
                }
                this.regionManager.save(this.config);
                break;
            }
            case "setpriority": {
                if (args.length < 3) {
                    player.sendMessage(ChatHelper.color("&8>> &7Correct usage: &6/region setpriority <region> <priority>"));
                    return true;
                }
                Region region = this.regionManager.find(args[1], player.getWorld());
                if (region == null) {
                    player.sendMessage(ChatHelper.color("&8>> &7Region &e" + args[1] + " &7in world &e" + player.getWorld().getName() + " &7does not exist."));
                    return true;
                }
                if (!StringHelper.isInteger(args[2])) {
                    player.sendMessage(ChatHelper.color("&8>> &e" + args[2] + " &7is not a &enumber&7."));
                    return true;
                }
                int priority = Integer.parseInt(args[2]);
                region.setPriority(priority);
                this.regionManager.save(this.config);
                player.sendMessage(ChatHelper.color("&8>> &7Set priority &e" + priority + " &7for region &e" + region.getName()));
                break;
            }
            default: {
                player.sendMessage(ChatHelper.color("&8>> &7Correct usage: &6/region <define/redefine/delete/list/info/flag/setpriority>"));
                break;
            }
        }
        return true;
    }

    private com.sk89q.worldedit.regions.Region selection(Player player) {
        LocalSession session = this.worldEdit.getSessionManager().findByName(player.getName());
        if (session == null || session.getSelectionWorld() == null) {
            return null;
        }
        try {
            return session.getSelection(session.getSelectionWorld());
        } catch (IncompleteRegionException e) {
            return null;
        }
    }
}