package org.redsnow.db.storage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Row implements Serializable {
    public static final int COLUMN_USERNAME_SIZE = 32;
    public static final int COLUMN_EMAIL_SIZE = 255;

    /**
     * column | size(bytes) | offset
     * id     | 4           | 0
     * user   | 32          | 4
     * email  | 255         | 36
     * total  | 291
     */

    public static final int INT_SIZE = Integer.SIZE / 8;
    public static final int ID_SIZE = INT_SIZE;  // 32bit
    public static final int ID_OFFSETS = 0;
    public static final int USERNAME_OFFSET = ID_SIZE;
    public static final int EMAIL_OFFSET = USERNAME_OFFSET + Row.COLUMN_USERNAME_SIZE + INT_SIZE;
    public static final int ROW_SIZE = ID_SIZE + Row.COLUMN_USERNAME_SIZE + Row.COLUMN_EMAIL_SIZE + 2 * INT_SIZE;

    private int id;
    private char[] username;
    private char[] email;

    public Row(int id, char[] username, char[] email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char[] getUsername() {
        return username;
    }

    public void setUsername(char[] username) {
        this.username = username;
    }

    public char[] getEmail() {
        return email;
    }

    public void setEmail(char[] email) {
        this.email = email;
    }


    public void write2Buffer(byte[] dest, byte[] src, int offset) {
        System.arraycopy(src, 0, dest, offset, src.length);
    }


    // id, username.length, username, email.length, email
    public byte[] serialize() {
        byte[] buffer = new byte[ROW_SIZE];
        write2Buffer(buffer, Converter.intToBytes(id), ID_OFFSETS);
        write2Buffer(buffer, Converter.intToBytes(username.length), USERNAME_OFFSET);
        write2Buffer(buffer, Converter.charsToBytes(username), USERNAME_OFFSET + INT_SIZE);
        write2Buffer(buffer, Converter.intToBytes(email.length), EMAIL_OFFSET);
        write2Buffer(buffer, Converter.charsToBytes(email), EMAIL_OFFSET + INT_SIZE);

        return buffer;
    }

    public static byte[] getBytes(byte[] buffer, int offset, int length) {
        byte[] res = new byte[length];
        System.arraycopy(buffer, offset, res, 0, length);
        return res;
    }


    public static Row deserialize(byte[] buffer) {
        int id = Converter.bytesToInt(getBytes(buffer, ID_OFFSETS, ID_SIZE));
        int uL = Converter.bytesToInt(getBytes(buffer, ID_SIZE, INT_SIZE));
        char[] username = Converter.bytesToChars(buffer, USERNAME_OFFSET + INT_SIZE, uL);
        int eL = Converter.bytesToInt(getBytes(buffer, EMAIL_OFFSET, INT_SIZE));
        char[] email = Converter.bytesToChars(buffer, EMAIL_OFFSET + INT_SIZE, eL);

        return new Row(id, username, email);
    }

    @Override
    public String toString() {
        return String.format("(%d, %s, %s)", this.id, new String(this.username), new String(this.email));
    }
}
