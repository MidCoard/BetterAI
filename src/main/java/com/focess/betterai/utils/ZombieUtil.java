package com.focess.betterai.utils;

import org.bukkit.Material;

/**
 * 实体工具类
 */
public class ZombieUtil {


    public static boolean checkPlaceMaterial(Material type) {
        return BetterAIConfiguration.getZombiePlaceBlockGoalMaterials().contains(type) &&
                type.isBlock();
    }
}
