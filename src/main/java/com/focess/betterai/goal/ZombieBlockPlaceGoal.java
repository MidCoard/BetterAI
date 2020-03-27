package com.focess.betterai.goal;

import com.focess.betterai.util.BetterAIConfiguration;
import com.focess.pathfinder.core.util.NMSManager;
import com.focess.pathfinder.entity.EntityManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.Goal;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ZombieBlockPlaceGoal extends Goal {

    private final FocessEntity zombie;

    public ZombieBlockPlaceGoal(Zombie zombie) {
        this.zombie = EntityManager.getFocessEntity(zombie);
        this.addControl(Control.LOOK);
    }

    @Override
    public boolean canStart() {
        return !zombie.getNavigationManager().isIdle() && ((LivingEntity) zombie.getBukkitEntity()).getEquipment() != null && BetterAIConfiguration.getZombiePlaceBlockGoalMaterials().contains(((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getType()) && ((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getType().isBlock() && ((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getAmount() > 0;
    }

    @Override
    public void start() {
        Material material = ((LivingEntity) zombie.getBukkitEntity()).getEquipment().getItemInHand().getType();
        Block block = ((LivingEntity) zombie.getBukkitEntity()).getTargetBlock(null, 3);
        BlockCanBuildEvent blockCanBuildEvent;
        if (NMSManager.getVersionInt() < 13) {
            try {
                blockCanBuildEvent =
                        BlockCanBuildEvent.class.getConstructor(Block.class, int.class, boolean.class).newInstance(block, material.getId(), true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else
            blockCanBuildEvent =
                    new BlockCanBuildEvent(block,
                            Bukkit.createBlockData(material), true);
        Bukkit.getServer().getPluginManager().callEvent(blockCanBuildEvent);
        if (blockCanBuildEvent.isBuildable()) {
            block.setType(material);
            havePlaced();
        }
        else {
            Location location = this.zombie.getBukkitEntity().getLocation().clone();
            if (location.clone().add(0, 2, 0).getBlock().getType().equals(Material.AIR)) {
                location.clone().getBlock().setType(material);
                this.zombie.getBukkitEntity().teleport(location.clone().add(0, 1, 0));
                havePlaced();
            }
        }
    }

    private void havePlaced() {
        EntityEquipment entityEquipment = ((LivingEntity) zombie.getBukkitEntity()).getEquipment();
        if ( entityEquipment != null && ! entityEquipment.getItemInHand().getType().equals(Material.AIR)) {
            ItemStack itemStack =  entityEquipment.getItemInMainHand().clone();
            if (itemStack.getAmount() <= 1) {
                itemStack.setAmount(0);
                itemStack.setType(Material.AIR);
            }
            else itemStack.setAmount(itemStack.getAmount() - 1);
            entityEquipment.setItemInMainHand(itemStack);
        }
    }
}
