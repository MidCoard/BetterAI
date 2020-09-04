package com.focess.betterai.goal;

import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.Goal;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Zombie;

import java.util.List;

public abstract class ZombieInteractBlockGoal extends Goal {
    protected final FocessEntity zombie;
    private final List<Location> locations = Lists.newCopyOnWriteArrayList();
    protected Block block;
    private boolean isStop;
    private float x;
    private float z;

    public ZombieInteractBlockGoal(Zombie zombie) {
        this.zombie = EntityManager.getFocessEntity(zombie);
    }

    public void addRequest(Location location) {
        locations.add(location);
    }

    @Override
    public boolean canStart() {
        boolean flag = locations.size() != 0;
        if (flag) {
            this.block = locations.get(0).getBlock();
            locations.remove(0);
        }
        return flag;
    }

    @Override
    public boolean shouldContinue() {
        return !this.isStop;
    }

    @Override
    public void start() {
        this.isStop = false;
        this.x = (float) ((this.block.getX() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getX());
        this.z = (float) ((this.block.getZ() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getZ());
    }

    @Override
    public void tick() {
        float f1 = (float) ((this.block.getX() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getX());
        float f2 = (float) ((this.block.getZ() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getZ());
        float f3 = this.x * f1 + this.z * f2;
        if (f3 < 0.0F)
            this.isStop = true;
    }


}
