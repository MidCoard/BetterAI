package com.focess.betterai.utils.command.data;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldBuffer extends DataBuffer<World> {
    private final StringBuffer stringBuffer;

    public WorldBuffer(int size) {
        this.stringBuffer = StringBuffer.allocate(size);
    }

    public static WorldBuffer allocate(int size) {
        return new WorldBuffer(size);
    }

    public void flip() {
        this.stringBuffer.flip();
    }

    public void put(World world) {
        this.stringBuffer.put(world.getName());
    }

    @Override
    public World get() {
        return Bukkit.getWorld(stringBuffer.get());
    }
}
