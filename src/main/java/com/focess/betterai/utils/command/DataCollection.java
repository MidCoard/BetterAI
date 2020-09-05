package com.focess.betterai.utils.command;

import com.focess.betterai.utils.command.data.*;
import com.focess.betterai.utils.command.data.StringBuffer;
import org.bukkit.World;

import java.nio.*;
import java.util.UUID;

public class DataCollection {

    private final IntBuffer intBuffer;
    private final DoubleBuffer doubleBuffer;
    private final FloatBuffer floatBuffer;
    private final BooleanBuffer booleanBuffer;
    private final ByteBuffer byteBuffer;
    private final LongBuffer longBuffer;
    private final CharBuffer charBuffer;
    private final ShortBuffer shortBuffer;
    private final StringBuffer stringBuffer;
    private final UUIDBuffer _UUIDBuffer;
    private final WorldBuffer worldBuffer;
    private final ObjectBuffer objectBuffer;
    private final StringBuffer defaultBuffer;

    public DataCollection(int size) {
        this.defaultBuffer = StringBuffer.allocate(size);
        this.intBuffer = IntBuffer.allocate(size);
        this.doubleBuffer = DoubleBuffer.allocate(size);
        this.floatBuffer = FloatBuffer.allocate(size);
        this.booleanBuffer = BooleanBuffer.allocate(size);
        this.byteBuffer = ByteBuffer.allocate(size);
        this.longBuffer = LongBuffer.allocate(size);
        this.charBuffer = CharBuffer.allocate(size);
        this.shortBuffer = ShortBuffer.allocate(size);
        this.stringBuffer = StringBuffer.allocate(size);
        this._UUIDBuffer = UUIDBuffer.allocate(size);
        this.worldBuffer = WorldBuffer.allocate(size);
        this.objectBuffer = ObjectBuffer.allocate(size);
    }

    public void flip() {
        this.defaultBuffer.flip();
        this.intBuffer.flip();
        this.doubleBuffer.flip();
        this.floatBuffer.flip();
        this.booleanBuffer.flip();
        this.byteBuffer.flip();
        this.longBuffer.flip();
        this.charBuffer.flip();
        this.shortBuffer.flip();
        this.stringBuffer.flip();
        this._UUIDBuffer.flip();
        this.worldBuffer.flip();
        this.objectBuffer.flip();
    }

    void write(String s) {defaultBuffer.put(s);}

    void writeInt(int i) { intBuffer.put(i); }

    void writeDouble(double d) { doubleBuffer.put(d); }

    void writeFloat(float f) {floatBuffer.put(f);}

    void writeBoolean(boolean b){booleanBuffer.put(b);}

    void writeByte(byte b){byteBuffer.put(b);}

    void writeLong(long l){longBuffer.put(l);}

    void writeChar(char c){charBuffer.put(c);}

    void writeShort(short s){shortBuffer.put(s);}

    void writeString(String s){stringBuffer.put(s);}

    void writeUUID(UUID u){_UUIDBuffer.put(u);}

    void writeWorld(World w){worldBuffer.put(w);}

    void writeObject(Object o){objectBuffer.put(o);}

    public String get() {return defaultBuffer.get();}

    public int getInt() {return intBuffer.get();}

    public double getDouble(){return doubleBuffer.get();}

    public float getFloat(){return floatBuffer.get();}

    public boolean getBoolean(){return booleanBuffer.get();}

    public byte getByte(){return byteBuffer.get();}

    public long getLong(){return longBuffer.get();}

    public char getChar(){return charBuffer.get();}

    public short getShort(){return shortBuffer.get();}

    public String getString() {return stringBuffer.get(); }

    public UUID getUUID() {return _UUIDBuffer.get();}

    public World getWorld() {return worldBuffer.get();}

    public Object getObject() {return objectBuffer.get();}
}
