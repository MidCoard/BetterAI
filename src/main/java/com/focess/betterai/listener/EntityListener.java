package com.focess.betterai.listener;

import com.focess.betterai.goal.ZombieAttackPlayerGoal;
import com.focess.betterai.goal.ZombieBlockGoal;
import com.focess.betterai.goal.ZombieBlockPlaceGoal;
import com.focess.betterai.navigation.FocessZombieNavigation;
import com.focess.betterai.utils.BetterAIConfiguration;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.FocessGoalItem;
import com.focess.pathfinder.goal.GoalSelector;
import com.focess.pathfinder.goals.Goals;
import com.focess.pathfinder.wrapped.WrappedEntityCreature;
import com.focess.pathfinder.wrapped.WrappedEntityInsentient;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Zombie && BetterAIConfiguration.isEnableZombie()) {
            event.getEntity().getEquipment();
            event.getEntity().setCanPickupItems(true);
            FocessEntity entity = EntityManager.getFocessEntity(event.getEntity());
            GoalSelector goalSelector = entity.getGoalSelector();
//            for (WrappedGoal wrappedGoal:goalSelector.getGoals())
//                if (wrappedGoal.getPriority() == 2 && wrappedGoal.getGoalItems().contains(Goals.TARGET.NEAREST_ATTACKABLE_TARGET))
//                    goalSelector.removeExactGoal(wrappedGoal);
            if (!goalSelector.containsGoal(Goals.JUMP.FLOAT))
                goalSelector.addGoal(Goals.JUMP.FLOAT
                        .clear()
                        .writeEntityInsentient(WrappedEntityInsentient.getWrappedEntityInsentient(event.getEntity()))
                        .build(0, false));


            //躲避太阳
            goalSelector.addGoal(Goals.MOVE.FLEE_SUN.clear().writeEntityCreature(WrappedEntityCreature.getWrappedEntityCreature((Creature) event.getEntity())).writeDouble(1.0).build(3, false));
            goalSelector.addGoal(Goals.RESTRICT_SUN.clear().writeEntityCreature(WrappedEntityCreature.getWrappedEntityCreature((Creature) event.getEntity())).build(2, false));
            //破坏方块
            goalSelector.addGoal(new FocessGoalItem(new ZombieBlockGoal((Zombie) event.getEntity())).build(1, false));
            //放置方块
            goalSelector.addGoal(new FocessGoalItem(new ZombieBlockPlaceGoal((Zombie) event.getEntity())).build(1, false));
            entity.getNavigationManager().addNavigation("FocessZombie", new FocessZombieNavigation(entity));
            //攻击玩家
            goalSelector.addGoal(new FocessGoalItem(new ZombieAttackPlayerGoal((Zombie) event.getEntity())).build(0, false));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (Zombie zombie : event.getBlock().getLocation().getNearbyEntitiesByType(Zombie.class, 12, 12, 12)) {
            FocessZombieNavigation navigation = (FocessZombieNavigation) EntityManager.getFocessEntity(zombie).getNavigationManager().getNavigation("FocessZombie");
            navigation.markShouldRecalculatePath();
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        for (Zombie zombie : event.getBlock().getLocation().getNearbyEntitiesByType(Zombie.class, 12, 12, 12)) {
            FocessZombieNavigation navigation = (FocessZombieNavigation) EntityManager.getFocessEntity(zombie).getNavigationManager().getNavigation("FocessZombie");
            navigation.markShouldRecalculatePath();
        }
    }
}
