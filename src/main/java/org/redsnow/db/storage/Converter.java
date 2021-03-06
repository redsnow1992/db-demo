package org.redsnow.db.storage;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Converter {
    public static byte[] charsToBytes(char[] chars) {
        return charsToBytes(chars, 0, chars.length);
    }

    public static byte[] charsToBytes(char[] chars, int offset, int length) {
        final ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(chars, offset, length));
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
    }

    public static char[] bytesToChars(byte[] bytes) {
        return bytesToChars(bytes, 0, bytes.length);
    }

    public static char[] bytesToChars(byte[] bytes, int offset, int length) {
        char[] data = new char[length];
        for (int i = 0; i < length; i++) {
            data[i] = (char) bytes[offset + i];
        }
        return data;
    }

    public static int bytesToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static int bytesToInt(byte[] b, int offset) {
        return   b[offset + 3] & 0xFF |
                (b[offset + 2] & 0xFF) << 8 |
                (b[offset + 1] & 0xFF) << 16 |
                (b[offset] & 0xFF) << 24;
    }

    public static byte[] intToBytes(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
