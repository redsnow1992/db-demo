package org.redsnow.db.storage;

public class Constants {
    public static final int PAGE_SIZE = 4096;
    public static final int TABLE_MAX_PAGES = 100;
    public static final int ROWS_PER_PAGE = PAGE_SIZE / Row.ROW_SIZE;
    public static final int TABLE_MAX_ROWS = ROWS_PER_PAGE * TABLE_MAX_PAGES;
}
