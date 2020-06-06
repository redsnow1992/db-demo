package org.redsnow.db.storage;

import org.redsnow.db.enums.InsertResult;
import org.redsnow.db.enums.UpdateResult;

import static org.redsnow.db.storage.Constants.*;
import static org.redsnow.db.storage.Row.*;


// memory presentation

public class Page {
    private int pageNo;
    private int numOfRows;
    private byte[] content;

    public Page(int pageNo) {
        this.pageNo = pageNo;
        this.numOfRows = 0;
        this.content = new byte[PAGE_SIZE];
    }

    public Page(int pageNo, int numOfRows, byte[] content) {
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.content = content;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Row getRow(int rowNum) {
        return Row.deserialize(this.content, rowNum);
    }

    public UpdateResult updateRow(Row r, int rowNum) {
        if (rowNum < 0 || rowNum >= ROWS_PER_PAGE) {
            return UpdateResult.UPDATE_FAILURE;
        }

        update(r, rowNum);
        return UpdateResult.UPDATE_SUCCESS;
    }

    public void update(Row r, int rowNum) {
        r.serialize(this.content, rowNum);
    }

    public InsertResult insert(Row r) {
        if (numOfRows >= ROWS_PER_PAGE) {
            return InsertResult.INSERT_PAGE_FULL;
        }

        update(r, numOfRows);

        numOfRows += 1;
        return InsertResult.INSERT_SUCCESS;
    }

    public byte[] serialize() {
        byte[] buffer = new byte[this.content.length + INT_SIZE];
        BufferUtils.write2Buffer(Converter.intToBytes(this.numOfRows), buffer, 0);
        BufferUtils.write2Buffer(this.content, buffer, INT_SIZE);

        return buffer;
    }

    public static Page deserialize(int pageNo, byte[] buffer) {
        int numOfRows = Converter.bytesToInt(getBytes(buffer, 0, INT_SIZE));
        byte[] content = new byte[buffer.length - INT_SIZE];
        BufferUtils.write2Buffer(buffer, INT_SIZE, content, 0);

        return new Page(pageNo, numOfRows, content);
    }
}
