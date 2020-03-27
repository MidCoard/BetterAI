package com.focess.betterai.listener;

import com.focess.betterai.goal.ZombieBlockGoal;
import com.focess.betterai.goal.ZombieBlockPlaceGoal;
import com.focess.betterai.util.BetterAIConfiguration;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.FocessGoalItem;
import com.focess.pathfinder.goal.GoalSelector;
import com.focess.pathfinder.goals.Goals;
import com.focess.pathfinder.wrapped.WrappedEntityCreature;
import com.focess.pathfinder.wrapped.WrappedEntityInsentient;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Zombie && BetterAIConfiguration.isEnableZombie()) {
            FocessEntity entity = EntityManager.getFocessEntity(event.getEntity());
            GoalSelector goalSelector = entity.getGoalSelector();
            if (!goalSelector.containsGoal(Goals.JUMP.FLOAT))
                goalSelector.addGoal(Goals.JUMP.FLOAT
                        .clear()
                        .writeEntityInsentient(WrappedEntityInsentient.getWrappedEntityInsentient((Mob) event.getEntity()))
                        .build(0, false));
            goalSelector.addGoal(Goals.MOVE.FLEE_SUN.clear().writeEntityCreature(WrappedEntityCreature.getWrappedEntityCreature((Creature) event.getEntity())).writeDouble(1.0).build(3, false));
            goalSelector.addGoal(Goals.RESTRICT_SUN.clear().writeEntityCreature(WrappedEntityCreature.getWrappedEntityCreature((Creature) event.getEntity())).build(2, false));
            goalSelector.addGoal(new FocessGoalItem(new ZombieBlockGoal((Zombie) event.getEntity())).build(2,false));
            goalSelector.addGoal(new FocessGoalItem(new ZombieBlockPlaceGoal((Zombie) event.getEntity())).build(2,
                    false));
        }
    }
}
