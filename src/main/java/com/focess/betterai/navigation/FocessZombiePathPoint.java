package com.focess.betterai.navigation;

import com.focess.betterai.utils.BetterAIConfiguration;
import com.focess.pathfinder.navigation.path.PathPoint;
import org.apache.logging.log4j.core.pattern.ThreadIdPatternConverter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class FocessZombiePathPoint extends PathPoint implements Comparable<FocessZombiePathPoint> {

    public static final int MAX_DISTANCE = 60000000;

    private double distance;

    private int blocks;

    public FocessZombiePathPoint(int x, int y, int z, World world, int blocks) {
        this(new Location(world, x, y, z), blocks);

    }

    public FocessZombiePathPoint(Location loc, int blocks) {
        super(loc);
        this.distance = 0d;
        this.blocks = blocks;
    }

    public FocessZombiePathPoint move(int[] direction) {
        if (direction.length == 3)
            return new FocessZombiePathPoint(this.getLocation().getBlockX() + direction[0], this.getLocation().getBlockY() + direction[1], this.getLocation().getBlockZ() + direction[2], this.getLocation().getWorld(), this.blocks);
        else throw new IllegalArgumentException();
    }

    public double value(FocessZombiePathPoint pathPoint) {
        double base = 0;
        if (this.getLocation().clone().subtract(0, 1, 0).getBlock().getType().isAir())
            if (this.blocks > 0) {
                base += 2;
                this.blocks--;
            } else return MAX_DISTANCE;
        base += MaterialValue.getValue(this.getLocation().getBlock().getType()) + MaterialValue.getValue(this.getLocation().clone().add(0, 1, 0).getBlock().getType());
        return this.distance = (pathPoint.distance + base);
    }


    @Override
    public int compareTo(FocessZombiePathPoint o) {
        return this.distance < o.distance ? -1 : o.distance == this.distance ? 0 : 1;
    }

    public double getDistance() {
        return this.distance;
    }

    public int getBlocks() {
        return this.blocks;
    }

    private static class MaterialValue {


        public static double getValue(Material material) {
            if (material.isAir())
                return 0.5;
            if (BetterAIConfiguration.getZombieInteractBlockGoalMaterials().contains(material))
                return 1;
            return MAX_DISTANCE;
        }
    }
}
