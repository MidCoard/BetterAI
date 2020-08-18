package com.focess.betterai.navigation;

import com.focess.pathfinder.core.navigation.focess.FocessPath;
import com.focess.pathfinder.core.navigation.focess.FocessPathPoint;

public class ZombiePath extends FocessPath{
	private ZombieNavigation zombieNavigation;
	
	public ZombiePath(ZombieNavigation zombieNavigation) {
		this.zombieNavigation=zombieNavigation;
	}

	@Override
	public FocessPathPoint getNowPathPoint() {
		return this.getPathPoint(1);
	}

	public ZombieNavigation getZombieNavigation() {
		return zombieNavigation;
	}

	public void setZombieNavigation(ZombieNavigation zombieNavigation) {
		this.zombieNavigation = zombieNavigation;
	}
	

}
