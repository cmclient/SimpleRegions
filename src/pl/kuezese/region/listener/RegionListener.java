package pl.kuezese.region.listener;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import pl.kuezese.region.SimpleRegions;
import pl.kuezese.region.helper.ChatHelper;
import pl.kuezese.region.manager.RegionManager;
import pl.kuezese.region.object.Region;
import pl.kuezese.region.type.Flag;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class RegionListener implements Listener {

    private final Set<Material> types;
    private final Set<Material> vehicles;
    private final RegionManager regionManager;

    public RegionListener(SimpleRegions simpleRegions) {
        this.types = Collections.unmodifiableSet(EnumSet.of(Material.CHEST, Material.TRAPPED_CHEST, Material.DISPENSER, Material.JUKEBOX, Material.BREWING_STAND, Material.CAULDRON, Material.DROPPER, Material.WOOD_DOOR, Material.IRON_DOOR, Material.TRAP_DOOR, Material.WOODEN_DOOR, Material.BEACON));
        this.vehicles = Collections.unmodifiableSet(EnumSet.of(Material.BOAT, Material.MINECART, Material.STORAGE_MINECART, Material.POWERED_MINECART, Material.EXPLOSIVE_MINECART, Material.HOPPER_MINECART, Material.COMMAND_MINECART));
        this.regionManager = simpleRegions.getRegionManager();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getBlockPlaced().getLocation());
            if (region != null && region.getFlags().contains(Flag.blockplace)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getBlock().getLocation());
            if (region != null && region.getFlags().contains(Flag.blockbreak)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player damager = this.damager(e);
            if (damager != null) {
                Player victim = (Player) e.getEntity();
                Region victimRegion = this.regionManager.find(victim.getLocation());
                Region damagerRegion = this.regionManager.find(damager.getLocation());
                if ((victimRegion != null && victimRegion.getFlags().contains(Flag.pvp)) || (damagerRegion != null && damagerRegion.getFlags().contains(Flag.pvp))) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBurn(BlockBurnEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.burn)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFade(BlockFadeEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.fade)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onForm(BlockFormEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.grow)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGrow(BlockGrowEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.grow)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPiston(BlockPistonExtendEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.fromto)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onIgnite(BlockIgniteEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.burn)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpread(BlockSpreadEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.grow)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDecay(LeavesDecayEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.leafdecay)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onForm(EntityBlockFormEvent e) {
        Region region = this.regionManager.find(e.getBlock().getLocation());
        if (region != null && region.getFlags().contains(Flag.blockbreak)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getBlock().getLocation());
            if (region != null && region.getFlags().contains(Flag.blockplace)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent e) {
        Region region = this.regionManager.find(e.getLocation());
        if (region != null && region.getFlags().contains(Flag.explode)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getBlockClicked().getLocation());
            if (region != null && region.getFlags().contains(Flag.bucket)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBucketFill(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getBlockClicked().getLocation());
            if (region != null && region.getFlags().contains(Flag.bucket)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVehicleDestroy(VehicleDestroyEvent e) {
        Entity p = e.getAttacker();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getVehicle().getLocation());
            if (region != null && region.getFlags().contains(Flag.blockbreak)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        Entity p = e.getRemover();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getEntity().getLocation());
            if (region != null && region.getFlags().contains(Flag.blockbreak)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHangingPlace(HangingPlaceEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            Region region = this.regionManager.find(e.getEntity().getLocation());
            if (region != null && region.getFlags().contains(Flag.blockplace)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() != EntityType.PLAYER) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                Region region = this.regionManager.find(e.getRightClicked().getLocation());
                if (region != null && region.getFlags().contains(Flag.blockplace)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobSpawn(CreatureSpawnEvent e) {
        Region region = this.regionManager.find(e.getLocation());
        if (region != null && region.getFlags().contains(Flag.mobspawning)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp() && e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Region region = this.regionManager.find(e.getTo());
            if (region != null && region.getFlags().contains(Flag.enderpearl)) {
                e.setCancelled(true);
                p.sendMessage(ChatHelper.color("&8>> &cYou can't teleport here."));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp() && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Region region = this.regionManager.find(p.getLocation());
            if (region != null && region.getFlags().contains(Flag.use) && this.types.contains(e.getClickedBlock().getType())) {
                e.setCancelled(true);
                return;
            }
            if (region != null && region.getFlags().contains(Flag.blockplace) && this.vehicles.contains(p.getItemInHand().getType())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                Player p = (Player) e.getEntity();
                Region region = this.regionManager.find(p.getLocation());
                if (region != null && region.getFlags().contains(Flag.fire)) {
                    e.setCancelled(true);
                }
            }
            else if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Player p = (Player) e.getEntity();
                Region region = this.regionManager.find(p.getLocation());
                if (region != null && region.getFlags().contains(Flag.falldamage)) {
                    e.setCancelled(true);
                }
            }
        } else if (e.getEntity() instanceof ItemFrame) {
            Region region = this.regionManager.find(e.getEntity().getLocation());
            if (region != null && region.getFlags().contains(Flag.blockbreak)) {
                e.setCancelled(true);
            }
        }
    }

    private Player damager(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            return (Player) damager;
        }
        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }
        return null;
    }
}
