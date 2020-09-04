package com.focess.betterai.command;


import com.comphenix.protocol.PacketType;
import com.focess.betterai.utils.command.Command;
import com.focess.betterai.utils.command.PlayerCommandExecutor;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.WrappedGoal;
import com.focess.pathfinder.navigation.FocessNavigation;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ShortArrayMap;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Zombie;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BetterAICommand extends Command {
    public BetterAICommand() {
        super("betterai", Lists.newArrayList("bai"), "betterai.command");
    }

    @Override
    protected List<String> getCompleteLists(CommandSender sender, String cmd, String[] args) {
        return Lists.newArrayList();
    }

    @Override
    public void init() {
        this.addExecutor(1, (PlayerCommandExecutor) (sender, args) -> {
            FocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(UUID.fromString(args[0])));
            for (WrappedGoal goal : focessEntity.getGoalSelector().getGoals())
                sender.sendMessage(goal.getGoal().getClass().getName() + ":" + goal.getPriority() + ":" + goal.getControls().toString());
        }, "show");
        this.addExecutor(2, (PlayerCommandExecutor) (sender, args) -> {
            Integer pos = Integer.parseInt(args[1]);
            FocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(UUID.fromString(args[0])));
            focessEntity.getGoalSelector().removeExactGoal(focessEntity.getGoalSelector().getGoals().get(pos));
            sender.sendMessage("Successfully");
        }, "remove");
        this.addExecutor( 3,(PlayerCommandExecutor)(sender,args) ->{
            FocessEntity focessEntity = null;
            for (Zombie zombie :sender.getWorld().getEntitiesByClass(Zombie.class)) {
                focessEntity = EntityManager.getFocessEntity(zombie);
                break;
            }
            sender.sendMessage(Objects.toString(focessEntity));
            if (focessEntity != null)
            focessEntity.getNavigationManager().getNavigation("FocessZombie").findPathTo(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]),20);
        });

        this.addExecutor(3,(PlayerCommandExecutor) (sender,args)->{
            FocessEntity focessEntity = null;
            for (Zombie zombie :sender.getWorld().getEntitiesByClass(Zombie.class)) {
                focessEntity = EntityManager.getFocessEntity(zombie);
                break;
            }
            sender.sendMessage(Objects.toString(focessEntity));
            if (focessEntity != null)
                ((FocessNavigation)focessEntity.getNavigationManager().getNavigation("FocessZombie")).moveTo(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]),10);
        },"move");
    }

    @Override
    public void usage(CommandSender commandSender) {

    }
}
