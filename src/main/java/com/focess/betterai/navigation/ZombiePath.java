package com.focess.betterai.navigation;

import org.bukkit.Location;
import org.bukkit.World;

import com.focess.pathfinder.core.navigation.focess.FocessPath;
import com.focess.pathfinder.core.navigation.focess.FocessPathPoint;
import com.toolapi.utils.WorldUtil;

public class ZombiePath extends FocessPath{
	private ZombieNavigation zombieNavigation;
	
	public ZombiePath(ZombieNavigation zombieNavigation) {
		this.zombieNavigation=zombieNavigation;
	}

	@Override
	public PlayerPathPoint getNowPathPoint() {
		return (PlayerPathPoint) this.getPathPoint(1);
	}

	public ZombieNavigation getZombieNavigation() {
		return zombieNavigation;
	}

	public void setZombieNavigation(ZombieNavigation zombieNavigation) {
		this.zombieNavigation = zombieNavigation;
	}

	@Override
	public boolean isPathAlive() {
		PlayerPathPoint playerPoint=getNowPathPoint();
		if(playerPoint==null)return false;
		Location loc=playerPoint.getLocation();
		if(WorldUtil.hasPlayerInDistance(loc, 1, playerPoint.getPlayerName()))return true;
		return false;
	}
	

}
