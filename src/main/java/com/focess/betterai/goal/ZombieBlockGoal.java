package com.focess.betterai.goal;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.focess.betterai.utils.PacketUtil;
import com.focess.pathfinder.core.util.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class ZombieBlockGoal extends ZombieInteractBlockGoal {
    private int breakTime;
    private int breakSit;

    public ZombieBlockGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public boolean canStart() {
        return super.canStart() && Boolean.parseBoolean(this.zombie.getBukkitEntity().getWorld().getGameRuleValue("mobGriefing"));
    }

    @Override
    public boolean shouldContinue() {
        return this.breakTime<=30;
    }

    @Override
    public void start() {
        super.start();
        this.breakTime = 0;
    }

    @Override
    public void stop() {
        super.stop();
        PacketUtil.sendBreakAnimationPacket(this.zombie.getID(),this.block,-1);
    }
    @Override
    public void tick() {
        super.tick();
        this.breakTime++;
        int breakSit = (int)(this.breakTime / 3.0F);
        if (breakSit != this.breakSit) {
        	PacketUtil.sendBreakAnimationPacket(this.zombie.getID(),this.block,breakSit);
            this.breakSit = breakSit;
        }
        if (this.breakTime == 30) {
            EntityChangeBlockEvent event;
            if (NMSManager.getVersionInt() < 13) {
                try {
                    event = EntityChangeBlockEvent.class.getConstructor(LivingEntity.class, Block.class,Material.class).newInstance(this.zombie.getBukkitEntity(),this.block, Material.AIR);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else event = new EntityChangeBlockEvent(this.zombie.getBukkitEntity(),this.block,Bukkit.createBlockData(Material.AIR));
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                start();
                return;
            }
            Zombie z = (Zombie)this.zombie.getBukkitEntity();
            for (ItemStack itemStack : this.block.getDrops())
                z.getWorld().dropItem(new Location(z.getWorld(), this.block.getX(), this.block.getY(),
                        this.block.getZ()), itemStack);
            this.block.setType(Material.AIR);
        }
    }
}
