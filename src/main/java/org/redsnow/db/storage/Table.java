package org.redsnow.db.storage;

import org.redsnow.db.Cursor;
import org.redsnow.db.Statement;
import org.redsnow.db.enums.ExecuteResult;

import java.io.IOException;

import static org.redsnow.db.storage.Constants.*;

public class Table {
    private int numOfRows;
    private Pager pager;

    public Table(Pager pager) {
        this.pager = pager;
        this.numOfRows = this.pager.getNumOfRows();
    }

    public int getNumOfRows() {
        return this.numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    // rowNum从0开始
    private Page allocatePage(Cursor cursor) {
        int pageNum = cursor.getRowNum() / ROWS_PER_PAGE;

        try {
            return this.pager.getPage(pageNum);
        } catch (IOException ex) {
            System.exit(EXIT_FAILURE);
        }
        return null;
    }

    private ExecuteResult insert(Row row) {
        Page page = allocatePage(Cursor.tableEnd(this));
        page.insert(row);
        numOfRows += 1;
        return ExecuteResult.EXECUTE_SUCCESS;
    }

    public ExecuteResult executeInsert(Statement statement) {
        if (numOfRows >= TABLE_MAX_ROWS) {
            return ExecuteResult.EXECUTE_TABLE_FULL;
        }

        return insert(statement.getRow());
    }

    public ExecuteResult executeSelect() {
        Cursor cursor = Cursor.tableStart(this);
        while (!cursor.isEndOfTable()) {
            Page page = allocatePage(cursor);
            System.out.println(page.getRow(cursor.getRowNum()));
            cursor.advance();
        }

        return ExecuteResult.EXECUTE_SUCCESS;
    }

    public void close() {
        this.pager.flushNumOfRow(this.numOfRows);
        this.pager.close();
    }
}
