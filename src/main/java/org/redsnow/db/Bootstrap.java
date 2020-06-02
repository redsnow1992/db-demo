package org.redsnow.db;

import java.util.Scanner;
import org.redsnow.db.enums.*;
import org.redsnow.db.storage.Row;
import org.redsnow.db.storage.Table;

import static org.redsnow.db.storage.Row.COLUMN_EMAIL_SIZE;
import static org.redsnow.db.storage.Row.COLUMN_USERNAME_SIZE;

public class Bootstrap {
    public static int EXIT_SUCCESS = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Table table = new Table();

        while (true) {
            printPrompt();
            String cmd = scanner.nextLine().trim();

            if (cmd.charAt(0) == '.') {
                switch (doMetaCommand(cmd)) {
                    case META_COMMAND_SUCCESS:
                        continue;
                    case META_COMMAND_UNRECOGNIZED_COMMAND:
                        System.out.println(String.format("Unrecognized command '%s'.", cmd));
                        continue;
                }
            }

            Statement statement = new Statement();
            switch (prepareStatement(cmd, statement)) {
                case PREPARE_SUCCESS:
                    break;
                case PREPARE_NEGATIVE_ID:
                    System.out.println("ID must be positive.");
                    continue;
                case PREPARE_STRING_TOO_LONG:
                    System.out.println("String is too long.");
                    continue;
                case PREPARE_SYNTAX_ERROR:
                    System.out.println("Syntax error. Could not parse statement.");
                    continue;
                case PREPARE_UNRECOGNIZED_STATEMENT:
                    System.out.println(String.format("Unrecognized keyword at start of '%s'.", cmd));
                    continue;
            }

            switch (statement.execute(table)) {
                case EXECUTE_SUCCESS:
                    System.out.println("Executed.");
                    break;
                case EXECUTE_TABLE_FULL:
                    System.out.println("Error: Table full.");
                    break;
            }
        }
    }

    public static void printPrompt() {
        System.out.print("db > ");
    }

    public static MetaCommandResult doMetaCommand(String cmd) {
        if (cmd.equals(".exit")) {
            System.exit(EXIT_SUCCESS);
            return MetaCommandResult.META_COMMAND_SUCCESS;
        } else {
            return MetaCommandResult.META_COMMAND_UNRECOGNIZED_COMMAND;
        }
    }

    public static PrepareResult prepareStatement(String cmd, Statement statement) {
        if (cmd.startsWith("insert")) {
            statement.setStatementType(StatementType.STATEMENT_INSERT);
            String[] tokens = cmd.split(" ");

            if (tokens.length != 4) {
                return PrepareResult.PREPARE_SYNTAX_ERROR;
            } else if (Integer.parseInt(tokens[1]) < 0) {
                return PrepareResult.PREPARE_NEGATIVE_ID;
            } else if (tokens[2].length() > COLUMN_USERNAME_SIZE || tokens[3].length() > COLUMN_EMAIL_SIZE) {
                return PrepareResult.PREPARE_STRING_TOO_LONG;
            } else {
                try {
                    statement.setRow(new Row(Integer.parseInt(tokens[1]),
                            tokens[2].toCharArray(), tokens[3].toCharArray()));
                } catch (Exception ex) {
                    return PrepareResult.PREPARE_SYNTAX_ERROR;
                }
                return PrepareResult.PREPARE_SUCCESS;
            }
        } else if (cmd.startsWith("select")) {
            statement.setStatementType(StatementType.STATEMENT_SELECT);
            return PrepareResult.PREPARE_SUCCESS;
        } else {
            return PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT;
        }
    }
}
