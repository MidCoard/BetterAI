package com.focess.betterai;

import com.focess.betterai.command.BetterAICommand;
import com.focess.betterai.goal.ZombieAttackPlayerGoal;
import com.focess.betterai.goal.ZombieBlockGoal;
import com.focess.betterai.goal.ZombieBlockPlaceGoal;
import com.focess.betterai.goal.ZombieInteractBlockGoal;
import com.focess.betterai.listener.EntityListener;
import com.focess.betterai.navigation.FocessZombieNavigation;
import com.focess.betterai.navigation.FocessZombiePathMode;
import com.focess.betterai.utils.BetterAIConfiguration;
import com.focess.betterai.utils.command.Command;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.FocessGoalItem;
import com.focess.pathfinder.goal.GoalItem;
import com.focess.pathfinder.goal.GoalSelector;
import com.focess.pathfinder.goals.Goals;
import com.focess.pathfinder.navigation.path.mode.PathModes;
import com.focess.pathfinder.wrapped.WrappedEntityCreature;
import com.focess.pathfinder.wrapped.WrappedEntityInsentient;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BetterAI extends JavaPlugin {
    private static BetterAI instance;

    public static BetterAI getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        loadConfig();//加载配置文件
        BetterAIConfiguration.loadDefault(this);

        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        //注册事件

        Command.register(new BetterAICommand());

        PathModes.registerPathMode("FocessZombie", FocessZombiePathMode.class);
        FocessGoalItem.registerGoalItem("ZombieBlockPlace", ZombieBlockPlaceGoal.class);
        FocessGoalItem.registerGoalItem("ZombieBlock", ZombieBlockGoal.class);
        for (World world:Bukkit.getWorlds())
            for (Zombie zombie:world.getEntitiesByClass(Zombie.class)) {
                FocessEntity entity = EntityManager.getFocessEntity(zombie);
                GoalSelector goalSelector = entity.getGoalSelector();
//            for (WrappedGoal wrappedGoal:goalSelector.getGoals())
//                if (wrappedGoal.getPriority() == 2 && wrappedGoal.getGoalItems().contains(Goals.TARGET.NEAREST_ATTACKABLE_TARGET))
//                    goalSelector.removeExactGoal(wrappedGoal);
                if (!goalSelector.containsGoal(Goals.JUMP.FLOAT))
                    goalSelector.addGoal(Goals.JUMP.FLOAT
                            .clear()
                            .writeEntityInsentient(WrappedEntityInsentient.getWrappedEntityInsentient(zombie))
                            .build(0, false));


                //躲避太阳
                goalSelector.addGoal(Goals.MOVE.FLEE_SUN.clear().writeEntityCreature(WrappedEntityCreature.getWrappedEntityCreature(zombie)).writeDouble(1.0).build(3, false));
                goalSelector.addGoal(Goals.RESTRICT_SUN.clear().writeEntityCreature(WrappedEntityCreature.getWrappedEntityCreature(zombie)).build(2, false));
                //破坏方块
                goalSelector.addGoal(new FocessGoalItem(new ZombieBlockGoal(zombie)).build(1, false));
                //放置方块
                goalSelector.addGoal(new FocessGoalItem(new ZombieBlockPlaceGoal(zombie)).build(1, false));
                entity.getNavigationManager().addNavigation("FocessZombie", new FocessZombieNavigation(entity));
                //攻击玩家
                goalSelector.addGoal(new FocessGoalItem(new ZombieAttackPlayerGoal(zombie)).build(0, false));

            }
        //注册指令
    }

    private void loadConfig() {
        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdir();
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists())
            this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Command.unregisterAllCommand();
        Bukkit.getScheduler().cancelTasks(this);
    }
}
