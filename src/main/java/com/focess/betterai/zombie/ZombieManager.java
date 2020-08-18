package com.focess.betterai.zombie;

import java.util.HashMap;

public class ZombieManager {
	public static final ZombieManager INSTANCE=new ZombieManager();
	private HashMap<String,AIZombie> zombieMap=new HashMap<>();
	
	public AIZombie getZombieByEntityID(int id) {
		String strID=""+id;
		return zombieMap.get(strID);
	}
	
	public void saveAIZombie(AIZombie aiZombie) {
		zombieMap.put(aiZombie.getEntityID()+"", aiZombie);
	}
	
}
