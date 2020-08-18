package com.focess.betterai.utils;

import com.focess.betterai.BetterAI;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class BetterAIConfiguration {

    public static Set<Material> getZombiePlaceBlockGoalMaterials() {
        return ZombiePlaceBlockGoalMaterials;
    }

    private static Set<Material> ZombiePlaceBlockGoalMaterials = Sets.newHashSet();

    public static Set<Material> getZombieInteractBlockGoalMaterials() {
        return ZombieInteractBlockGoalMaterials;
    }

    private static Set<Material> ZombieInteractBlockGoalMaterials = Sets.newHashSet();

    public static boolean isEnableZombie() {
        return enableZombie;
    }

    private static boolean enableZombie;

    public static void loadDefault(BetterAI betterAI) {
        FileConfiguration configuration = betterAI.getConfig();
        if (configuration.isConfigurationSection("Zombie"))
            buildZombie(configuration.getConfigurationSection("Zombie"));
    }

    private static void buildZombie(ConfigurationSection zombie) {
        enableZombie = zombie.getBoolean("Flag",true);
        for (String material:zombie.getStringList("ZombieInteractBlockGoal"))
            ZombieInteractBlockGoalMaterials.add(Material.getMaterial(material));
        for (String material:zombie.getStringList("ZombiePlaceBlockGoal"))
            ZombiePlaceBlockGoalMaterials.add(Material.getMaterial(material));
    }
}
