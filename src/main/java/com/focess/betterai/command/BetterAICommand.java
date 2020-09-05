package com.focess.betterai.command;


import com.focess.betterai.utils.command.Command;
import com.focess.betterai.utils.command.CommandResult;
import com.focess.betterai.utils.command.PlayerCommandExecutor;
import com.focess.betterai.utils.command.TabCompleter;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.WrappedGoal;
import com.focess.pathfinder.navigation.FocessNavigation;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Zombie;
import org.bukkit.permissions.Permission;

import java.util.Objects;
import java.util.UUID;

public class BetterAICommand extends Command {

    public static final Permission DEFAULT = new Permission("betterai.command");

    public BetterAICommand() {
        super("betterai", Lists.newArrayList("bai"));
    }

    @Override
    public void init() {
        this.addExecutor(1, (PlayerCommandExecutor) (sender, args) -> {
            FocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(args.getUUID()));
            for (WrappedGoal goal : focessEntity.getGoalSelector().getGoals())
                sender.sendMessage(goal.getGoal().getClass().getName() + ":" + goal.getPriority() + ":" + goal.getControls().toString());
            return CommandResult.ALLOW;
        },  "show").addTabComplete(TabCompleter.EntityExceptPlayerUUID);
        this.addExecutor(2, (PlayerCommandExecutor) (sender, args) -> {
            int pos = args.getInt();
            FocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(args.getUUID()));
            focessEntity.getGoalSelector().removeExactGoal(focessEntity.getGoalSelector().getGoals().get(pos));
            sender.sendMessage("Successfully");
            return CommandResult.ALLOW;
        }, "remove").addTabComplete(TabCompleter.EntityExceptPlayerUUID);
        this.addExecutor(3, (PlayerCommandExecutor) (sender, args) -> {
            FocessEntity focessEntity = null;
            for (Zombie zombie : sender.getWorld().getEntitiesByClass(Zombie.class)) {
                focessEntity = EntityManager.getFocessEntity(zombie);
                break;
            }
            sender.sendMessage(Objects.toString(focessEntity));
            if (focessEntity != null)
                focessEntity.getNavigationManager().getNavigation("FocessZombie").findPathTo(args.getInt(), args.getInt(), args.getInt(), 20);
            return CommandResult.ALLOW;
        }).addTabComplete(TabCompleter.CoordinateIntX,TabCompleter.CoordinateIntY,TabCompleter.CoordinateIntZ);
        this.addExecutor(3, (PlayerCommandExecutor) (sender, args) -> {
            FocessEntity focessEntity = null;
            for (Zombie zombie : sender.getWorld().getEntitiesByClass(Zombie.class)) {
                focessEntity = EntityManager.getFocessEntity(zombie);
                break;
            }
            sender.sendMessage(Objects.toString(focessEntity));
            if (focessEntity != null)
                ((FocessNavigation) focessEntity.getNavigationManager().getNavigation("FocessZombie")).moveTo(args.getInt(), args.getInt(), args.getInt(), 10);
            return CommandResult.ALLOW;
        },  "move").addTabComplete(TabCompleter.CoordinateIntX,TabCompleter.CoordinateIntY,TabCompleter.CoordinateIntZ).addCommandResult(CommandResult.POSITIVE, () -> {
            System.out.println("Hello World");
        });;
    }

    @Override
    public void usage(CommandSender commandSender) {

    }
}
