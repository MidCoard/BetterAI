package com.focess.betterai.goal;

import com.focess.betterai.navigation.FocessZombieNavigation;
import com.focess.betterai.utils.BetterAIConfiguration;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.Goal;
import com.focess.pathfinder.goals.Goals;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ZombieBlockPlaceGoal extends Goal {

    private final List<Location> locations = Lists.newCopyOnWriteArrayList();
    private final FocessEntity zombie;

    public ZombieBlockPlaceGoal(Zombie zombie) {
        this.zombie = EntityManager.getFocessEntity(zombie);
    }

    public void addRequest(Location location) {
        locations.add(location);
    }

    @Override
    public boolean canStart() {
        return locations.size() != 0 && checkMaterial();
    }

    private boolean checkMaterial() {
        EntityEquipment equipment = ((Zombie)zombie.getBukkitEntity()).getEquipment();
        if (equipment == null)
            return false;
        if (equipment.getItemInHand() == null || !equipment.getItemInHand().getType().isBlock())
            return false;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        Material
                material = ((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getType();
        Location location = locations.get(0);
        locations.remove(0);
        location.getBlock().setType(material);
        havePlaced();
        this.zombie.getBukkitEntity().teleport(location.clone().add(0,1,0));
        for (FocessEntity entity:EntityManager.getAllFocessEntities())
            if (entity.getBukkitEntity() instanceof Zombie && entity.getLocation().distance(location) < 20)
                ((FocessZombieNavigation)entity.getNavigationManager().getNavigation("FocessZombie")).markShouldRecalculatePath();
    }

    private void havePlaced() {
        EntityEquipment equipment = ((LivingEntity) zombie.getBukkitEntity()).getEquipment();
        if (equipment.getItemInHand().getAmount() == 1) {
            equipment.setItemInHand(new ItemStack(Material.AIR));
        } else {
            ItemStack itemStack = equipment.getItemInHand().clone();
            itemStack.setAmount(equipment.getItemInHand().getAmount() - 1);
            equipment.setItemInHand(itemStack);
        }
    }


}
