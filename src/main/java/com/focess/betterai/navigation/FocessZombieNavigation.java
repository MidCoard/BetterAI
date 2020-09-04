package com.focess.betterai.navigation;

import com.focess.betterai.utils.ZombieUtil;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.navigation.FocessNavigation;
import com.focess.pathfinder.navigation.path.FocessPath;
import com.focess.pathfinder.navigation.path.Path;
import com.focess.pathfinder.navigation.path.mode.PathMode;
import com.focess.pathfinder.util.Requests;
import com.google.common.collect.Maps;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import static com.focess.betterai.navigation.FocessZombiePathPoint.MAX_DISTANCE;

public class FocessZombieNavigation extends FocessNavigation {

    private static final int[][] direction = {{0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}, {1, 0, 0}, {-1, 0, 0}};
    private final Map<Location, Location> backs = Maps.newHashMap();

    private final Map<Location, Boolean> visited = Maps.newHashMap();

    private final Map<Location, Double> distances = Maps.newHashMap();
    private boolean inRecalculate = false;
    private FocessZombiePath path;
    private boolean shouldRecalculatePath = false;

    public FocessZombieNavigation(FocessEntity focessEntity) {
        super(focessEntity);
    }

    @Override
    public boolean isIdle() {
        return this.path == null || path.isIdle();
    }

    @Override
    public void stop() {
        this.path = null;
        this.stopPathMove();
    }

    @Override
    public boolean startMovingAlong(Path p, double speed) {
        this.stopPathMove();
        if (p instanceof FocessZombiePath) {
            PathMode.createPathMode("FocessZombie", this, (FocessPath) p, speed);
            return true;
        }
        return false;
    }

    @Override
    public FocessZombiePath findPathTo(double x, double y, double z, double maxDistance) {
        if(inRecalculate)
            return null;
        this.inRecalculate = true;
        Location targetLocation = new Location(this.getWorld(), x, y, z).toBlockLocation();
        Location defaultLocation = this.getFocessEntity().getBukkitEntity().getLocation().toBlockLocation();
        backs.clear();
        distances.clear();
        visited.clear();
        distances.put(defaultLocation, 0d);
        FocessZombiePathPoint base = new FocessZombiePathPoint(defaultLocation, this.getBlocks());
        PriorityQueue<FocessZombiePathPoint> points = new PriorityQueue<>();
        points.add(base);
        boolean flag = false;
        Location lastLocation = null;
        while (!points.isEmpty()) {
            FocessZombiePathPoint pathPoint = points.poll();
            if (pathPoint.getLocation().toVector().equals(targetLocation.toVector())) {
                flag = true;
                lastLocation = pathPoint.getLocation();
                break;
            }
            if (pathPoint.getDistance() > maxDistance || visited.getOrDefault(pathPoint.getLocation(), false) || distances.getOrDefault(pathPoint.getLocation(), (double) MAX_DISTANCE) < pathPoint.getDistance())
                continue;
            visited.put(pathPoint.getLocation(), true);
            for (int i = 0; i < direction.length; i++) {
                FocessZombiePathPoint tempPathPoint = pathPoint.move(direction[i]);
                double distance = tempPathPoint.value(pathPoint);
                if (distances.getOrDefault(tempPathPoint.getLocation(), (double) MAX_DISTANCE) > distance && !visited.getOrDefault(tempPathPoint.getLocation(), false)) {
                    distances.put(tempPathPoint.getLocation(), distance);
                    backs.put(tempPathPoint.getLocation(), pathPoint.getLocation());
                    points.add(tempPathPoint);
                }
            }
        }
        Stack<Location> locations = new Stack<>();
        FocessZombiePath path = null;
        if (flag == true) {
            path = new FocessZombiePath();
            for (Location loc = lastLocation; loc != null; loc = backs.get(loc)) {
                locations.push(loc);
            }
            int size = locations.size();
            for (int i = 0; i < size; i++) {
                Location now = locations.pop();
                path.addPathPoint(i, now);
            }
        }
        this.inRecalculate = false;
        return path;
    }

    private int getBlocks() {
        EntityEquipment equipment = ((LivingEntity) this.getFocessEntity().getBukkitEntity()).getEquipment();
        if (equipment.getItemInHand() != null && ZombieUtil.checkPlaceMaterial(equipment.getItemInHand().getType()))
            return equipment.getItemInHand().getAmount();
        return 0;
    }

    public void markShouldRecalculatePath() {
        this.shouldRecalculatePath = true;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return (!this.inRecalculate) && (this.path == null || path.isIdle() || this.shouldRecalculatePath);
    }

    @Override
    public void recalculatePath() {
        this.getFocessEntity().getNavigationManager().stop();
        this.shouldRecalculatePath = false;
        Zombie zombie = (Zombie) this.getFocessEntity().getBukkitEntity();
        if (zombie.getTarget() != null) {
            LivingEntity entity = zombie.getTarget();
            Requests.addRequest(new Requests.Request() {
                @Override
                public void run() {
                    path = (FocessZombiePath) findPathTo(EntityManager.getFocessEntity(entity), 20);
                    if (path != null && !path.isIdle())
                        startMovingAlong(path, 1);
                }
            });

        }
    }

    @Override
    public FocessZombiePath getCurrentPath() {
        return this.path;
    }
}
