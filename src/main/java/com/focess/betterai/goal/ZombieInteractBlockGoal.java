package com.focess.betterai.goal;

import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.Goal;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public abstract class ZombieInteractBlockGoal extends Goal {
    protected final FocessEntity zombie;
    protected Block block;
    private boolean isStop;
    private float x;
    private float z;

    public ZombieInteractBlockGoal(Zombie zombie){
        this.zombie = EntityManager.getFocessEntity(zombie);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = ((Creature)this.zombie.getBukkitEntity()).getTarget();
        if (target == null)
            return false;
        Vector vector =
                target.getLocation().clone().subtract(((Creature)this.zombie.getBukkitEntity()).getEyeLocation().clone()).getDirection();
        BlockIterator iterator = new BlockIterator(this.zombie.getBukkitEntity().getWorld(),
                ((Creature)this.zombie.getBukkitEntity()).getEyeLocation().toVector(),vector,4,4);
        Block now = null;
        while (iterator.hasNext()){
            now = iterator.next();
            if (!now.getType().equals(Material.AIR))
                break;
        }
        if (now != null && !now.getType().equals(Material.AIR)) {
            this.zombie.getBukkitEntity().setVelocity(vector);
            this.block = now;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return !this.isStop;
    }

    @Override
    public void start() {
        this.isStop = false;
        this.x = (float)((this.block.getX() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getX());
        this.z = (float)((this.block.getZ() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getZ());
    }

    @Override
    public void tick() {
        float f1 = (float)((this.block.getX() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getX());
        float f2 = (float)((this.block.getZ() + 0.5F) - this.zombie.getBukkitEntity().getLocation().getZ());
        float f3 = this.x * f1 + this.z * f2;
        if (f3 < 0.0F)
            this.isStop = true;
    }


}
