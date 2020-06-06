package org.redsnow.db.storage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.redsnow.db.storage.Constants.PAGE_SIZE;
import static org.redsnow.db.storage.Constants.ROWS_PER_PAGE;

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

    // id, username.length, username, email.length, email
    public byte[] serialize() {
        byte[] buffer = new byte[ROW_SIZE];
        BufferUtils.write2Buffer(Converter.intToBytes(id), buffer, ID_OFFSETS);
        BufferUtils.write2Buffer(Converter.intToBytes(username.length), buffer, USERNAME_OFFSET);
        BufferUtils.write2Buffer(Converter.charsToBytes(username), buffer, USERNAME_OFFSET + INT_SIZE);
        BufferUtils.write2Buffer(Converter.intToBytes(email.length), buffer, EMAIL_OFFSET);
        BufferUtils.write2Buffer(Converter.charsToBytes(email), buffer, EMAIL_OFFSET + INT_SIZE);

        return buffer;
    }

    // id, username.length, username, email.length, email
    public void serialize(byte[] pageBuffer, int rowNum) {
        int rowOffset = getRowOffset(rowNum);
        BufferUtils.write2Buffer(Converter.intToBytes(id), pageBuffer, rowOffset + ID_OFFSETS);
        BufferUtils.write2Buffer(Converter.intToBytes(username.length), pageBuffer, rowOffset + USERNAME_OFFSET);
        BufferUtils.write2Buffer(Converter.charsToBytes(username), pageBuffer, rowOffset + USERNAME_OFFSET + INT_SIZE);
        BufferUtils.write2Buffer(Converter.intToBytes(email.length), pageBuffer, rowOffset+ EMAIL_OFFSET);
        BufferUtils.write2Buffer(Converter.charsToBytes(email), pageBuffer, rowOffset + EMAIL_OFFSET + INT_SIZE);
    }

    public static byte[] getBytes(byte[] buffer, int offset, int length) {
        byte[] res = new byte[length];
        System.arraycopy(buffer, offset, res, 0, length);
        return res;
    }


    public static Row deserialize(byte[] pageBuffer, int rowNum) {
        int rowOffset = getRowOffset(rowNum);
        int id = Converter.bytesToInt(pageBuffer, rowOffset + ID_OFFSETS);
        int uL = Converter.bytesToInt(pageBuffer, rowOffset + USERNAME_OFFSET);
        char[] username = Converter.bytesToChars(pageBuffer, rowOffset + USERNAME_OFFSET + INT_SIZE, uL);
        int eL = Converter.bytesToInt(pageBuffer, rowOffset + EMAIL_OFFSET);
        char[] email = Converter.bytesToChars(pageBuffer, rowOffset + EMAIL_OFFSET + INT_SIZE, eL);

        return new Row(id, username, email);
    }

    // in the context of page content
    public static int getRowOffset(int rowNum) {
        int pageNum = rowNum / ROWS_PER_PAGE;
        return pageNum * PAGE_SIZE + (rowNum - pageNum * ROWS_PER_PAGE) * ROW_SIZE;
    }

    @Override
    public String toString() {
        return String.format("(%d, %s, %s)", this.id, new String(this.username), new String(this.email));
    }
}
