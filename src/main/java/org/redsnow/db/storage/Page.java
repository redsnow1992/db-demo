package org.redsnow.db.storage;

import org.redsnow.db.enums.InsertResult;
import org.redsnow.db.enums.UpdateResult;

import java.util.ArrayList;
import java.util.List;

import static org.redsnow.db.storage.Constants.*;
import static org.redsnow.db.storage.Row.ROW_SIZE;

public class Page {
    private int numOfRows;
    private byte[] content;

    public Page() {
        this.content = new byte[PAGE_SIZE];
        this.numOfRows = 0;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Row getRow(int rowNum) {
        byte[] buffer = new byte[ROW_SIZE];
        System.arraycopy(content, (rowNum - 1) * ROW_SIZE, buffer, 0, ROW_SIZE);
        return Row.deserialize(buffer);
    }

    public UpdateResult updateRow(Row r, int rowNum) {
        if (rowNum <= 0 || rowNum > ROWS_PER_PAGE) {
            return UpdateResult.UPDATE_FAILURE;
        }

        update(r, rowNum);
        return UpdateResult.UPDATE_SUCCESS;
    }

    public void update(Row r, int rowNum) {
        byte[] bytes = r.serialize();
        System.arraycopy(bytes, 0, content, (rowNum - 1) * ROW_SIZE, bytes.length);
    }

    public InsertResult insert(Row r) {
        if (numOfRows >= ROWS_PER_PAGE) {
            return InsertResult.INSERT_PAGE_FULL;
        }

        update(r, numOfRows + 1);

        numOfRows += 1;
        return InsertResult.INSERT_SUCCESS;
    }
}
