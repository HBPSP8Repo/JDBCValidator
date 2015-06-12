package epfl.dias.sql;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

import static org.junit.Assert.fail;

/**
 * Created by torcato on 12-06-2015.
 * Base class for query checks
 * This sets te properties and has some tests already defined.
 * The derived class will have to define he abstract methods to run the queries
 */
public abstract class TestStatementAncestor {

    /**
     * Default constructor
     */
    TestStatementAncestor()
    {

    }
    protected static Logger log = Logger.getLogger( Properties.class );


    /**
     * Sets up the configuration for the tests
     * @throws ClassNotFoundException
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    @BeforeClass
    public static void setProperties() throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException {

        // clears all properties to be sure
        System.clearProperty("validator.auto.load.popular.drivers");
        System.clearProperty("validator.extra.drivers");
        System.clearProperty("validator.default.filter.confFile");
        System.clearProperty("validator.default.query.delay");
        System.clearProperty("validator.filter.license.file");
        System.clearProperty("validator.configurations");
        System.clearProperty("validator.chuv.filter.confFile");
        System.clearProperty("validator.hug.filter.confFile");
        System.clearProperty("validator.hug.query.delay");
        System.clearProperty("validator.chuv.query.delay");

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
     * Runs a single executeQuery against our test DB
     * @param url the url of the db t connect
     * @param query the query to run
     * @throws SQLException
     */
    abstract void runExecuteQuery( String url, String query) throws SQLException ;

    /**
     * Runs a single execute against our test DB
     * @param url the url of the db t connect
     * @param query the query to run
     * @throws SQLException
     */
    abstract void runExecute( String url, String query) throws SQLException;


    @Test
    public void TestAllowedQuery() throws SQLException {

        String url = "validate:jdbc:postgresql://localhost/CHUV_MIPS";
        String query = "SELECT count(patient.year_of_birth) from patient";
        runExecuteQuery(url, query);
    }

    @Test
    public void TestNotAllowedQuery() throws  SQLException {

        String url = "validate:jdbc:postgresql://localhost/CHUV_MIPS";
        String query = "SELECT patient.* FROM patient";
        try {

            runExecuteQuery(url, query);
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
        String url1 = "validate:chuv:jdbc:postgresql://localhost/CHUV_MIPS";
        try {

            runExecuteQuery(url1, query);
            fail("query should not pass the filter");
        } catch (SQLFilterException e) {
            log.debug("Exception was thrown as it should");
            log.debug(e);
        }

        String url2 = "validate:hug:jdbc:postgresql://localhost/CHUV_MIPS";
        //this query has to pass
        // the limit for year_of_birth is not configured in query.filter.hug.yaml
        runExecuteQuery(url2, query);
    }

    @Test
    public void testExecute() throws SQLException {

        String query = "SELECT patient.year_of_birth FROM patient limit 1";
        String url1 = "validate:chuv:jdbc:postgresql://localhost/CHUV_MIPS";
        try {

            runExecute(url1, query);
            fail("query should not pass the filter");
        } catch (SQLFilterException e) {
            log.debug("Exception was thrown as it should");
            log.debug(e);
        }

        String url2 = "validate:hug:jdbc:postgresql://localhost/CHUV_MIPS";
        //this query has to pass
        // the limit for year_of_birth is not configured in query.filter.hug.yaml
        runExecute(url2, query);
    }


}
