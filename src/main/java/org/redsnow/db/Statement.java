package org.redsnow.db;

import org.redsnow.db.enums.ExecuteResult;
import org.redsnow.db.enums.StatementType;
import org.redsnow.db.storage.Row;
import org.redsnow.db.storage.Table;

public class Statement {
    private StatementType type;
    private Row row;

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Statement() {
    }

    public void setStatementType(StatementType type) {
        this.type = type;
    }

    public StatementType getStatementType() {
        return this.type;
    }

    public ExecuteResult executeInsert(Table table) {
        return table.executeInsert(this);
    }

    public ExecuteResult executeSelect(Table table) {
        return table.executeSelect();
    }

    public ExecuteResult execute(Table table) {
        switch (this.type) {
            case STATEMENT_INSERT:
                return executeInsert(table);
            case STATEMENT_SELECT:
                return executeSelect(table);
        }
        return ExecuteResult.EXECUTE_FAILURE;
    }
}
