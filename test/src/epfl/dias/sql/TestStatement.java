package epfl.dias.sql;

import epfl.dias.Properties;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

import static org.junit.Assert.fail;

/**
 * Created by torcato on 08.06.15.
 * Test cases for statement queries
 */
public class TestStatement {

    protected static Logger log = Logger.getLogger( Properties.class );


    /**
     * Sets up the configuration for the tests
     * @throws ClassNotFoundException
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    @BeforeClass
    public static void setProperties() throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException {

        // will load only the postgres driver
        System.setProperty("validator.auto.load.popular.drivers", "false");
        System.setProperty("validator.extra.drivers", "org.postgresql.Driver");
        System.setProperty("validator.default.filter.confFile","query.filter.chuv.yaml");
        //Maybe create a different configuration file for the default config
        System.setProperty("validator.default.filter.confFile","query.filter.chuv.yaml");

        System.setProperty("validator.configurations", "chuv,hug");
        System.setProperty("validator.chuv.filter.confFile", "query.filter.chuv.yaml");
        System.setProperty("validator.hug.filter.confFile", "query.filter.hug.yaml");

        Class.forName("epfl.dias.sql.DriverValidator");
    }

    /**
     * Runs a single query against our test DB
     * @param url the url of the db t connect
     * @param query the query to run
     * @throws SQLException
     */
    protected void runSingleQuery(String url, String query) throws SQLException {

        String user = "ipython";
        String passwd="ipython4thewin";

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, passwd);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next())
            {
                log.debug("result of the query :" + rs.getString(1));
            }
        }
        catch(SQLException e)
        {
            log.debug(e);
            throw e;
        }
        finally {
            if (con != null) con.close();
            if (st != null) st.close();
            if (rs != null) rs.close();
        }
        con.close();
    }


    @Test
    public void TestAllowedQuery() throws SQLException {

        String url = "jdbc:validate:postgresql://localhost/CHUV_MIPS";
        String query = "SELECT count(patient.year_of_birth) from patient";
        runSingleQuery(url, query);
    }

    @Test
    public void TestNotAllowedQuery() throws  SQLException {

        String url = "jdbc:validate:postgresql://localhost/CHUV_MIPS";
        String query = "SELECT patient.* FROM patient";
        try {

            runSingleQuery(url, query);
            fail("query should not pass the filter");
        }
        catch(SQLFilterException e)
        {
            log.debug("Exception was thrown as it should");
            log.debug(e);
        }

    }

    @Test
    public void testConfigurations() throws SQLException {

        String query = "SELECT patient.year_of_birth FROM patient limit 1";
        String url1 = "jdbc:validate:chuv:postgresql://localhost/CHUV_MIPS";
        try {

            runSingleQuery(url1, query);
            fail("query should not pass the filter");
        } catch (SQLFilterException e) {
            log.debug("Exception was thrown as it should");
            log.debug(e);
        }

        String url2 = "jdbc:validate:hug:postgresql://localhost/CHUV_MIPS";
        //this query has to pass
        // the limit for year_of_birth is not configured in query.filter.hug.yaml
        runSingleQuery(url2, query);
    }
}
