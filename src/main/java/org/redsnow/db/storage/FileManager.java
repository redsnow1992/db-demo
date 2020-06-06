package org.redsnow.db.storage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.file.StandardOpenOption.*;
import static org.redsnow.db.storage.Constants.EXIT_FAILURE;

public class FileManager {
    private String filename;
    private FileChannel fc;
    private ByteBuffer buffer;
    private long fileLength;

    public FileManager(String filename, int bufferSize) {
        this.filename = filename;
        this.buffer = ByteBuffer.allocate(bufferSize);

        try {
            this.fc = (FileChannel.open(new File(this.filename).toPath(), READ, WRITE, CREATE));
            this.fileLength = this.fc.size();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(EXIT_FAILURE);
        }
    }

    public byte[] read(int offset, int length) {
        byte[] res = null;
        try {
            this.buffer.clear();

            this.fc.position(offset);
            int n = this.fc.read(this.buffer);
            int actualSize = Math.min(n, length);
            if (actualSize > 0) {
                res = new byte[actualSize];
                BufferUtils.write2Buffer(this.buffer.array(),0, res,0, actualSize);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public void write(byte[] data, int offset) {
        try {
            this.fc.write(ByteBuffer.wrap(data), offset);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getCurrentStatus() {
        try {
            return String.format("position: %d, size: %d", this.fc.position(), this.fc.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public void close() {
        try {
            this.fc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
