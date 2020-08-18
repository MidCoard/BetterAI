package com.focess.betterai.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.focess.betterai.zombie.AIZombie;
import com.toolapi.utils.EntityUtil;

/**
 * 实体工具类
 */
public class ZombieUtil {
	
	
	/**
	 * 攻击一下方块
	 * @param block
	 */
	public static void attackBlock(Block block) {
		World world=block.getWorld();
		world.playSound(block.getLocation(), Sound.BLOCK_STONE_HIT , 1, 1);
	}
	
	/**
	 * 生成一只AI僵尸
	 * @param loc
	 */
	public static AIZombie spawnAIZombie(Location loc) {
		Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);//在玩家附近生成僵尸
    	
		//TODO NEED REMOVE
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5000, 1));
    	
    	
    	AIZombie aiZombie=new AIZombie(zombie);
		return aiZombie;
	}
	
	private static double range=10;//寻找范围
	
	/**
	 * 给僵尸寻找目标
	 * @param zombie
	 * @return
	 */
	public static Player findPlayer(AIZombie zombie) {
		Player player=EntityUtil.getNearestPlayerInRange(zombie.getBukkitEntity(), range);
		return player;
	}
}
