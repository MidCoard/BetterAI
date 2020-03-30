package com.focess.betterai.goal;

import com.focess.betterai.util.BetterAIConfiguration;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.Goal;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.util.BlockIterator;

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
        BlockIterator
            i = new BlockIterator((LivingEntity) this.zombie.getBukkitEntity(), 4);
        Block last = null;
        while (i.hasNext()) {
            Block temp = i.next();
            if (!temp.getType().equals(Material.AIR)) {
                last = temp;
                break;
            }
        }
        if (last != null) {
            this.block = last;
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
