package org.redsnow.db.storage;

import java.io.IOException;

import static org.redsnow.db.storage.Constants.*;
import static org.redsnow.db.storage.Row.*;

public class Pager {
    private int fileLength;
    private Page[] pages;
    private FileManager fm;

    public Pager(String filename) {
        this.fm = new FileManager(filename, PAGE_SIZE);
        this.pages = new Page[TABLE_MAX_PAGES];
    }

    // pageNo from [0, TABLE_MAX_PAGES)
    public Page getPage(int pageNo) throws IOException {
        if (pageNo > TABLE_MAX_PAGES) {
            System.out.println(String.format("Tried to fetch page number out of bounds. %d > %d", pageNo, TABLE_MAX_PAGES));
            System.exit(EXIT_FAILURE);
        }

        if (null == this.pages[pageNo]) {
            // load from disk into memory with data
            byte[] data = this.fm.read(getPageOffset(pageNo), PAGE_SIZE);
            Page page = null;
            // init page content and numOfRows
            if (null != data) {
                page = Page.deserialize(pageNo, data);
            } else {
                page = new Page(pageNo);
            }

            this.pages[pageNo] = page;
        }

        return this.pages[pageNo];
    }

    public void flushPage(int pageNo) {
        byte[] data = this.pages[pageNo].serialize();
        this.fm.write(data, getPageOffset(pageNo));
    }

    public void flushNumOfRow(int num) {
        this.fm.write(Converter.intToBytes(num), 0);
    }

    public void close() {
        for (int i = 0; i < this.pages.length; i++) {
            if (this.pages[i] != null) {
                flushPage(i);
            }
        }
        this.fm.close();
    }

    public int getPageOffset(int pageNo) {
        return pageNo * PAGE_SIZE + INT_SIZE;
    }

    public int getNumOfRows() {
        byte[] data = this.fm.read(0, INT_SIZE);
        if (data == null) {
            return 0;
        } else {
            return Converter.bytesToInt(data);
        }
    }
}
