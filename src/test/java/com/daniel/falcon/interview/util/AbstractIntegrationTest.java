package com.daniel.falcon.interview.util;

import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractIntegrationTest {

    private static final String INIT_DB_QUERY = "CREATE TABLE message" +
            "(ID INT PRIMARY KEY auto_increment, message VARCHAR(255));";

    private static final String TEAR_DOWN_DB_QUERY = "DROP TABLE IF EXISTS message";

    @Before
    public void setUpDb() {
        addJDBCTemplate().execute(TEAR_DOWN_DB_QUERY);
        addJDBCTemplate().execute(INIT_DB_QUERY);
    }

    @After
    public void tearDownDb() {
        addJDBCTemplate().execute(TEAR_DOWN_DB_QUERY);
    }

    protected abstract JdbcTemplate addJDBCTemplate();

    protected void waitUntilWithCondition(long millsToWait, long wakePeriod, WaitCondition waitCondition) throws InterruptedException {
        if (millsToWait > 0) {
            long start = System.currentTimeMillis();
            long current = System.currentTimeMillis();
            while (current - start < millsToWait && !waitCondition.isReady()) {
                Thread.sleep(wakePeriod);
                current = System.currentTimeMillis();
            }
        } else {
            throw new IllegalArgumentException("Requested time period has to be greater than zero.");
        }
    }

}
