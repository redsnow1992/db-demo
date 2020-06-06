package org.redsnow.db.storage;

public class BufferUtils {
    public static void write2Buffer(byte[] src, byte[] dest, int offset) {
        write2Buffer(src, 0, dest, offset);
    }

    public static void write2Buffer(byte[] src, int srcPos, byte[] dest, int offset) {
        write2Buffer(src, srcPos, dest, offset, src.length - srcPos);
    }

    public static void write2Buffer(byte[] src, int srcPos, byte[] dest, int offset, int length) {
        System.arraycopy(src, srcPos, dest, offset, length);
    }
}
