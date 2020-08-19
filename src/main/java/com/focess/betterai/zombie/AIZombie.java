package com.focess.betterai.zombie;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

import com.focess.betterai.navigation.ZombieNavigation;
import com.focess.betterai.utils.NMSUtil;
import com.focess.pathfinder.entity.IBasicFocessEntity;
import com.focess.pathfinder.entity.IFocessEntity;

/**
 * zombie包装类
 */
public class AIZombie implements IBasicFocessEntity{
	private Zombie zombie;
	private int entityID;
	
	/**
	 * 寻找玩家的范围
	 */
	private double findPlayerRange=10.0;
	
	private ZombieNavigation zombieNavigation;
	
	public AIZombie(Zombie zombie) {
		setBukkitEntity(zombie);
		this.zombieNavigation=new ZombieNavigation(this);
		ZombieManager.INSTANCE.saveAIZombie(this);
	}

	public void setBukkitEntity(Zombie zombie) {
		this.zombie = zombie;
		this.entityID=NMSUtil.getEntityID(zombie);
	}

	@Override
	public Zombie getBukkitEntity() {
		return this.zombie;
	}

	@Override
	public boolean isAlive() {
		return (zombie==null)?false:(zombie.isDead())?false:true;
	}

	public int getEntityID() {
		return entityID;
	}

	public ZombieNavigation getZombieNavigation() {
		return zombieNavigation;
	}

	public void setZombieNavigation(ZombieNavigation zombieNavigation) {
		this.zombieNavigation = zombieNavigation;
	}

	public double getFindPlayerRange() {
		return findPlayerRange;
	}

	public void setFindPlayerRange(double findPlayerRange) {
		this.findPlayerRange = findPlayerRange;
	}
	
	public boolean equals(Object ano) {
		if(ano==null||!(ano instanceof AIZombie))return false;
		AIZombie ai=(AIZombie)ano;
		if(ai.getBukkitEntity()==null) {
			if(this.getBukkitEntity()==null)return true;
			else return false;
		}
		if(ai.getBukkitEntity().equals(this.getBukkitEntity()))return true;
		return false;
	}
	
}
