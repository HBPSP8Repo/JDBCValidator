package epfl.dias.sql;


import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by torcato on 12-06-2015.
 * Tests for common urls
 */
public class TestURLParsing {

    @Test
    public void testPostgres() throws SQLException {

        Assert.assertEquals(DriverValidator.getRealUrl("validate:jdbc:postgresql://host:1234/database"),
                "jdbc:postgresql://host:1234/database");
        assertEquals( DriverValidator.getConfigurationName("validate:jdbc:postgresql://host:1234/database"),
                null);

        assertEquals( DriverValidator.getRealUrl("validate:conf:jdbc:postgresql://host:1234/database"),
                "jdbc:postgresql://host:1234/database");
        assertEquals( DriverValidator.getConfigurationName("validate:conf:jdbc:postgresql://host:1234/database"),
                "conf");

        assertEquals( DriverValidator.getRealUrl("validate:jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true"),
                "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true");
        assertEquals( DriverValidator.getConfigurationName("validate:jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true"),
                null);

        assertEquals( DriverValidator.getRealUrl("validate:conf:jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true"),
                "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true");
        assertEquals( DriverValidator.getConfigurationName("validate:conf:jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true"),
                "conf");

    }

    @Test
    public void testOracle() throws SQLException {

        assertEquals( DriverValidator.getRealUrl("validate:jdbc:oracle:thin:@myhost:1521:orcl"),
                "jdbc:oracle:thin:@myhost:1521:orcl");

        assertEquals( DriverValidator.getConfigurationName("validate:jdbc:oracle:thin:@myhost:1521:orcl"),
                null);

        assertEquals( DriverValidator.getRealUrl("validate:conf:jdbc:oracle:thin:@myhost:1521:orcl"),
                "jdbc:oracle:thin:@myhost:1521:orcl");
        assertEquals( DriverValidator.getConfigurationName("validate:conf:jdbc:oracle:thin:@myhost:1521:orcl"),
                "conf");
    }

    @Test
    public void testMySQL() throws SQLException {

        assertEquals( DriverValidator.getRealUrl("validate:jdbc:mysql://localhost"),
                "jdbc:mysql://localhost");

        assertEquals( DriverValidator.getConfigurationName("validate:jdbc:mysql://localhost"),
                null);

        assertEquals( DriverValidator.getRealUrl("validate:conf:jdbc:mysql://localhost"),
                "jdbc:mysql://localhost");
        assertEquals( DriverValidator.getConfigurationName("validate:conf:jdbc:mysql://localhost:jdbc"),
                "conf");
    }



}
