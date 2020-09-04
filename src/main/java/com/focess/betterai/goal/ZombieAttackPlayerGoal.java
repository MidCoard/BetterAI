package com.focess.betterai.goal;

import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.Goal;
import org.bukkit.GameMode;
import org.bukkit.entity.*;

public class ZombieAttackPlayerGoal extends Goal {

    private static final int TIMEOUT = 30;
    private final FocessEntity zombie;
    private Entity target = null;
    private int time = 0;

    public ZombieAttackPlayerGoal(Zombie zombie) {
        this.zombie = EntityManager.getFocessEntity(zombie);
    }

    private static boolean isPlayer(Entity entity) {
        if (!entity.getType().equals(EntityType.PLAYER))
            return false;
        Player player = (Player) entity;
        return player.getGameMode().equals(GameMode.ADVENTURE) || player.getGameMode().equals(GameMode.SURVIVAL);
    }

    @Override
    public boolean canStart() {
        searchTarget();
        return target != null;
    }

    @Override
    public boolean shouldContinue() {
        return this.target != null && this.zombie.getBukkitEntity().getLocation().distance(this.target.getLocation()) < 20;
    }

    @Override
    public void stop() {
        this.target = null;
        ((Creature) this.zombie.getBukkitEntity()).setTarget((LivingEntity) this.target);
        this.time = 0;
    }

    @Override
    public void tick() {
        this.time++;
        if (this.time % TIMEOUT == 0) {
            this.time = 0;
            searchTarget();
        }
        ((Creature) this.zombie.getBukkitEntity()).setTarget((LivingEntity) this.target);
    }

    private void searchTarget() {
        this.zombie.getBukkitEntity().getNearbyEntities(12, 12, 12).stream().filter(ZombieAttackPlayerGoal::isPlayer).sorted((i, j) -> {
            double distance1 = this.zombie.getBukkitEntity().getLocation().distance(i.getLocation());
            double distance2 = this.zombie.getBukkitEntity().getLocation().distance(j.getLocation());
            return Double.compare(distance1, distance2);
        }).findFirst().ifPresent((entity) -> target = entity);
    }
}
