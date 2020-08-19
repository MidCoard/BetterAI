package com.focess.betterai.command;


import com.focess.betterai.api.command.Command;
import com.focess.betterai.api.command.PlayerCommandExecutor;
import com.focess.betterai.utils.NMSUtil;
import com.focess.betterai.utils.PacketUtil;
import com.focess.betterai.utils.ZombieUtil;
import com.focess.betterai.zombie.AIZombie;
import com.focess.betterai.zombie.ZombieManager;
import com.focess.pathfinder.core.entity.NMSFocessEntity;
import com.focess.pathfinder.core.navigation.focess.FocessPathPoint;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.goal.WrappedGoal;
import com.google.common.collect.Lists;
import com.toolapi.utils.EntityUtil;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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
            NMSFocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(UUID.fromString(args[0])));
            for (WrappedGoal goal:focessEntity.getGoalSelector().getGoals())
                sender.sendMessage(goal.getNmsGoal().getClass().getName() + ":" + goal.getPriority() + ":" + goal.getControls().toString());
        },"show");
        this.addExecutor(2,(PlayerCommandExecutor)(sender,args)->{
            Integer pos = Integer.parseInt(args[1]);
            NMSFocessEntity focessEntity = EntityManager.getFocessEntity(Bukkit.getEntity(UUID.fromString(args[0])));
            focessEntity.getGoalSelector().removeExactGoal(focessEntity.getGoalSelector().getGoals().get(pos));
            sender.sendMessage("Successfully");
        },"remove");
        
        this.addExecutor(0,(PlayerCommandExecutor)(sender,args)->{
        	AIZombie aiZombie=ZombieUtil.spawnAIZombie(sender.getLocation());
        	sender.sendMessage("id:"+aiZombie.getEntityID());
        },"create");//生成僵尸
        
        this.addExecutor(1,(PlayerCommandExecutor)(sender,args)->{
        	int id=Integer.parseInt(args[0]);
        	AIZombie aiZombie=ZombieManager.INSTANCE.getZombieByEntityID(id);
        	aiZombie.getBukkitEntity().setVelocity(new Vector(0.1,0,0.1));
        	aiZombie.getZombieNavigation().gotoPathPoint(new FocessPathPoint(0,sender.getLocation()));
        	sender.sendMessage("id:"+aiZombie.getEntityID());
        },"move");//移动僵尸 bai move ID
        
        this.addExecutor(1,(PlayerCommandExecutor)(sender,args)->{
        	int id=Integer.parseInt(args[0]);
        	AIZombie aiZombie=ZombieManager.INSTANCE.getZombieByEntityID(id);
        	aiZombie.getBukkitEntity().setVelocity(new Vector(0,0.5,0));
        	sender.sendMessage("id:"+aiZombie.getEntityID());
        },"jump");//跳跃僵尸 bai jump ID
        
        this.addExecutor(2,(PlayerCommandExecutor)(sender,args)->{
        	int id=Integer.parseInt(args[0]);
        	int range=Integer.parseInt(args[1]);
        	AIZombie aiZombie=ZombieManager.INSTANCE.getZombieByEntityID(id);
        	Player player=EntityUtil.getNearestPlayerInRange(aiZombie.getBukkitEntity(), range);
        	if(player==null) {
        		sender.sendMessage("null");
        	}else
        	sender.sendMessage("name:"+player.getName());
        },"find");//寻找最近的玩家  bai find ID 范围
        
        this.addExecutor(2,(PlayerCommandExecutor)(sender,args)->{
        	int id=Integer.parseInt(args[0]);
        	int breaksit=Integer.parseInt(args[1]);
        	Block block=sender.getTargetBlock(10);
        	System.out.println("block:"+block.getY());
        	if(block!=null) {
        		PacketUtil.sendBreakAnimationPacket(id, block, breaksit);
        	}
        },"break");
    }

    @Override
    public void usage(CommandSender commandSender) {

    }
}
