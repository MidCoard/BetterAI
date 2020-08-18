package com.focess.betterai.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class WorldUtil {
	
	public static double dis(Location loc1,Location loc2) {
		if(loc1==null||loc2==null)return -1;
		return loc1.distance(loc2);
	}
	
	public static Block getBlockUnderEntity(Entity entity) {
		if(entity==null)return null;
		Location eLoc=entity.getLocation().toBlockLocation();
		Location tmp=new Location(eLoc.getWorld(),eLoc.getX(),eLoc.getY()-1,eLoc.getZ());
		return tmp.getBlock();
	}
	
}
