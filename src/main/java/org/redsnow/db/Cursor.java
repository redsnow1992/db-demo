package org.redsnow.db;

import org.redsnow.db.storage.Table;

public class Cursor {
    private Table table;
    private int rowNum;
    private boolean endOfTable;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public boolean isEndOfTable() {
        return endOfTable;
    }

    public void setEndOfTable(boolean endOfTable) {
        this.endOfTable = endOfTable;
    }

    // Indicates a position one past the last element
    public Cursor(Table table, int rowNum, boolean endOfTable) {
        this.table = table;
        this.rowNum = rowNum;
        this.endOfTable = endOfTable;
    }

    public void advance() {
        this.rowNum += 1;
        if (this.rowNum >= this.table.getNumOfRows()) {
            this.endOfTable = true;
        }
    }

    public static Cursor tableStart(Table table) {
        return new Cursor(table, 0, table.getNumOfRows() == 0);
    }

    public static Cursor tableEnd(Table table) {
        return new Cursor(table, table.getNumOfRows(), true);
    }
}
