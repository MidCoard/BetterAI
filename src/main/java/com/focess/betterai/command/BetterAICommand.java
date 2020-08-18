package com.focess.betterai.command;


import com.focess.betterai.util.command.Command;
import com.focess.betterai.util.command.PlayerCommandExecutor;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.WrappedGoal;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
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
        this.addExecutor(1,(PlayerCommandExecutor)(sender, args)->{
            FocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(UUID.fromString(args[0])));
            for (WrappedGoal goal:focessEntity.getGoalSelector().getGoals())
                sender.sendMessage(goal.getNmsGoal().getClass().getName() + ":" + goal.getPriority() + ":" + goal.getControls().toString());
        },"show");
        this.addExecutor(2,(PlayerCommandExecutor)(sender,args)->{
            Integer pos = Integer.parseInt(args[1]);
            FocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(UUID.fromString(args[0])));
            focessEntity.getGoalSelector().removeExactGoal(focessEntity.getGoalSelector().getGoals().get(pos));
            sender.sendMessage("Successfully");
        },"remove");
    }

    @Override
    public void usage(CommandSender commandSender) {

    }
}
