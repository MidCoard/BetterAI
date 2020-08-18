package com.focess.betterai.goal;

import com.focess.betterai.utils.BetterAIConfiguration;
import com.focess.pathfinder.core.entity.NMSFocessEntity;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.goal.Goal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ZombieBlockPlaceGoal extends Goal {


    private final NMSFocessEntity zombie;

    public ZombieBlockPlaceGoal(Zombie zombie) {
        this.zombie = EntityManager.getFocessEntity(zombie);
    }

    @Override
    public boolean canStart() {
        return ((LivingEntity) zombie.getBukkitEntity()).getEquipment() != null &&
                BetterAIConfiguration.getZombiePlaceBlockGoalMaterials().contains(((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getType()) &&
                ((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getType().isBlock() &&
                ((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getAmount() > 0;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        Material
              material   = ((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getType();
        Location zombieLocation = zombie.getBukkitEntity().getLocation().clone();
        LivingEntity livingEntity = ((Creature)this.zombie.getBukkitEntity()).getTarget();
        if (livingEntity == null)
            return;
        Location entityLocation = livingEntity.getLocation();
            if (entityLocation.getBlockY() > zombieLocation.getBlockY()) {
                Location l = zombieLocation.clone().add(0,2,0);
                if (l.getBlock().getType().equals(Material.AIR)) {
                    Location teleportLocation = zombieLocation.clone().add(0,1,0);
                    zombieLocation.getBlock().setType(material);
                    zombie.getBukkitEntity().teleport(teleportLocation);
                    havePlaced();
                }
            } else if (entityLocation.getBlockY() == zombieLocation.getBlockY()) {
                if (entityLocation.getBlockX() > zombieLocation.getBlockX()) {
                    Location loc = zombieLocation.clone();
                    loc.setY((loc.getBlockY() - 1));
                    loc.setX((loc.getBlockX() + 1));
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        loc.getBlock().setType(material);
                        havePlaced();
                    }
                } else if (entityLocation.getBlockX() < zombieLocation.getBlockX()) {
                    Location loc = zombieLocation.clone();
                    loc.setY((loc.getBlockY() - 1));
                    loc.setX((loc.getBlockX() - 1));
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        loc.getBlock().setType(material);
                        havePlaced();
                    }
                } else if (entityLocation.getBlockZ() > zombieLocation.getBlockZ()) {
                    Location loc = zombieLocation.clone();
                    loc.setY((loc.getBlockY() - 1));
                    loc.setZ((loc.getBlockZ() + 1));
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        loc.getBlock().setType(material);
                        havePlaced();
                    }
                } else if (entityLocation.getBlockZ() < zombieLocation.getBlockZ()) {
                    Location loc = zombieLocation.clone();
                    loc.setY((loc.getBlockY() - 1));
                    loc.setZ((loc.getBlockZ() - 1));
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        loc.getBlock().setType(material);
                        havePlaced();
                    }
                }
            }

    }

    private void havePlaced() {
        EntityEquipment equipment = ((LivingEntity)zombie.getBukkitEntity()).getEquipment();
        if (equipment.getItemInHand().getAmount() == 1) {
            equipment.setItemInHand(new ItemStack(Material.AIR));
        } else {
            ItemStack itemStack = equipment.getItemInHand().clone();
            itemStack.setAmount(equipment.getItemInHand().getAmount() - 1);
            equipment.setItemInHand(itemStack);
        }
    }


}
