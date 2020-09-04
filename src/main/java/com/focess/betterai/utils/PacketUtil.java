package com.focess.betterai.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PacketUtil {

    /**
     * 发送方块破坏包
     *
     * @param id       实体ID
     * @param block
     * @param breakSit 破坏程度1--9
     */
    public static void sendBreakAnimationPacket(int id, Block block, int breakSit) {
        for (World world : Bukkit.getWorlds())
            for (Player player : world.getEntitiesByClass(Player.class)) {
                PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
                packet.getIntegers().write(0, id).write(1, breakSit);
                packet.getBlockPositionModifier().write(0, new BlockPosition(block.getLocation().toVector()));
                if (block.getLocation().distanceSquared(player.getLocation()) < 1024D) {
                    try {
                        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

}
