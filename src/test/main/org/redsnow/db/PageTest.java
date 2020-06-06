package org.redsnow.db;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.redsnow.db.storage.Page;
import org.redsnow.db.storage.Row;

public class PageTest {
    @Test
    public void insertTest() {
        Page page = new Page(1);
        int id = 1;
        String username = "username";
        String email = "test@163.com";
        Row r = new Row(1, username.toCharArray(), email.toCharArray());
        page.insert(r);
        Row res = page.getRow(0);
        assertEquals(1, res.getId());
        assertEquals(username, new String(res.getUsername()));
        assertEquals(email, new String(res.getEmail()));
    }
}
