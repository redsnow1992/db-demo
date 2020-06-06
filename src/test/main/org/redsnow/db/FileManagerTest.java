package org.redsnow.db;

import static org.junit.Assert.*;
import org.junit.Test;
import org.redsnow.db.storage.Converter;
import org.redsnow.db.storage.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileManagerTest {
    @Test
    public void testReadAndCreate() throws IOException {
        FileManager fm = new FileManager("test.data", 20);

//        fm.write("test".getBytes(), 0);
//        assertEquals("test", new String(fm.read(0, 4)));
//
////        assertNull(fm.read(4, 5));
//
//        fm.write("aabb2".getBytes(), 10);
//        assertEquals("aabb2", new String(fm.read(10, 5)));

        fm.write(Converter.intToBytes(1), 0);
        fm.write("1".getBytes(), 4);
        fm.write("user1".getBytes(), 8);
        fm.write("person1@example.com".getBytes(), 40);
        fm.close();
    }
}
