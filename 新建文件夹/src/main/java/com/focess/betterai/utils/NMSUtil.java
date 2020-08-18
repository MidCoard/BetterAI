package com.focess.betterai.utils;

import org.bukkit.entity.Entity;

import com.focess.pathfinder.core.util.NMSManager;

public class NMSUtil {

	/**
	 * @param entity
	 * @return 若entity为null则返回0
	 */
	public static int getEntityID(Entity entity) {
		int id=0;
		if(entity==null)return id;
		try {
			id = NMSManager.getField(NMSManager.getNMSClass("Entity"),"id").getInt(NMSManager.getNMSEntity(entity));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return id;
	}
	
}
