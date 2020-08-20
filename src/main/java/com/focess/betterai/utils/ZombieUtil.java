package com.focess.betterai.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.focess.betterai.zombie.AIZombie;
import com.toolapi.utils.EntityUtil;

import pers.blockdurability.data.DurabilityManager;

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
		int dur=DurabilityManager.INSTANCE.getBlockDurability(block);
		dur++;
		if(dur==10){
			block.setType(Material.AIR);
			
			DurabilityManager.INSTANCE.setBlockDurability(block, -1);
		}else
		DurabilityManager.INSTANCE.setBlockDurability(block, dur);
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
	
	private static double range=50;//寻找范围
	
	/**
	 * 给僵尸寻找目标
	 * @param zombie
	 * @return
	 */
	public static Player findPlayer(AIZombie zombie) {
		Entity entity=zombie.getBukkitEntity();
		if(entity==null)return null;
		Location eLoc=entity.getLocation();
		List<Entity> entities=entity.getNearbyEntities(range, range, range);
		double pDis=Double.MAX_VALUE;
		Player player=null;
		for(Entity en:entities) {
			if(en instanceof Player) {
				Player p=(Player)en;
				if(p.getGameMode()!=GameMode.SURVIVAL)continue;
				double tmpDis=en.getLocation().distance(eLoc);
				if(tmpDis<pDis) {
					player=p;
					pDis=tmpDis;
				}
			}
		}
		return player;
	}
}
