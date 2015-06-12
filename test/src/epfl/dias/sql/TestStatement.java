package epfl.dias.sql;

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
public class TestStatement extends TestStatementAncestor {

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
        Properties.reloadConfig();
    }

    protected Connection connect(String url) throws SQLException {
        String user = "ipython";
        String passwd="ipython4thewin";
        return  DriverManager.getConnection(url, user, passwd);

    }
    /**
     * Runs a single query against our test DB
     * @param url the url of the db t connect
     * @param query the query to run
     * @throws SQLException
     */
    protected void runExecuteQuery( String url, String query) throws SQLException {


        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = connect(url);
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

    protected void runExecute( String url, String query) throws SQLException
    {
        Connection con = null;
        Statement st = null;

        try {
            con = connect(url);
            st = con.createStatement();
            st.execute( query);
        }
        catch(SQLException e)
        {
            log.debug(e);
            throw e;
        }
        finally {
            if (con != null) con.close();
            if (st != null) st.close();
        }
        con.close();
    }

    protected void runBatch(String url, String[] queries) throws SQLException {
        Connection con = null;
        Statement st = null;

        try {
            con = connect(url);
            st = con.createStatement();

            for(String query: queries)
            {
                st.addBatch(query);
            }

            st.executeBatch();
        }
        catch(SQLException e)
        {
            log.debug(e);
            log.debug(e.getNextException());
            throw e;
        }
        finally {
            if (con != null) con.close();
            if (st != null) st.close();
        }
        con.close();
    }

    @Test
    public void testBatch() throws SQLException {

        // middle query should not pass in first configuration
        String[] queries = {
                "SELECT count(patient.year_of_birth) from patient where patient.year_of_birth = 0",
                "SELECT patient.year_of_birth FROM patient where patient.year_of_birth = 0",
                "SELECT count(patient.year_of_birth) from patient where patient.year_of_birth = 0"
        };

        String url1 = "validate:chuv:jdbc:postgresql://localhost/CHUV_MIPS";
        try {
            runBatch(url1, queries);
            fail("query should not pass the filter");
        } catch (SQLFilterException e) {
            log.debug("Exception was thrown as it should");
            log.debug(e);
        }


        String url2 = "validate:hug:jdbc:postgresql://localhost/CHUV_MIPS";
        try {
            //this query has to pass
            // the limit for year_of_birth is not configured in query.filter.hug.yaml
            runBatch(url2, queries);
        }
        // this exception is normal
        catch( BatchUpdateException e)
        {
            log.debug("this exception will happen because we are making selects in a batch");
            log.debug(e);
            log.debug(e.getNextException());

        }
    }



}
