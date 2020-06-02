package org.redsnow.db.storage;

import org.redsnow.db.Statement;
import org.redsnow.db.enums.ExecuteResult;

import static org.redsnow.db.storage.Constants.*;

public class Table {
    private int numOfRows;
    private Page[] pages;

    public Table() {
        this.pages = new Page[TABLE_MAX_PAGES];
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Page[] getPages() {
        return pages;
    }

    public void setPages(Page[] pages) {
        this.pages = pages;
    }

    // rowNum从1开始
    public Page allocatePage(int rowNum) {
        int pageNum = rowNum / ROWS_PER_PAGE;
        if (null == this.pages[pageNum]) {
            this.pages[pageNum] = new Page();
        }

        return this.pages[pageNum];
    }

    public ExecuteResult insert(Row row) {
        Page page = allocatePage(numOfRows);
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
        for (int i = 1; i <= numOfRows; i++) {
            Page page = allocatePage(i);
            System.out.println(page.getRow(i));
        }
        return ExecuteResult.EXECUTE_SUCCESS;
    }
}
