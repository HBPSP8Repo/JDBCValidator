package epfl.dias.sql;

import epfl.dias.Properties;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;


import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;

/**
 * Created by torcato on 08.06.15.
 * Test cases for sql queries
 */
public class TestQueries {

    private static Logger log = Logger.getLogger( Properties.class );


    @BeforeClass
    public static void setProperties() throws ClassNotFoundException {

        // will load only the postgres driver
        System.setProperty("validator.auto.load.popular.drivers", "false");
        System.setProperty("validator.extra.drivers", "org.postgresql.Driver");
        System.setProperty("validator.default.filter.confFile","query-filter-configuration-file.yaml");

        Class.forName("epfl.dias.sql.DriverValidator");
    }

    @Test
    public void TestAllowedQuery() throws SQLException {

        String url = "jdbc:validate:postgresql://localhost/CHUV_MIPS";
        String user = "ipython";
        String passwd="ipython4thewin";

        Connection con = DriverManager.getConnection(url, user, passwd);
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT count(patient.year_of_birth) from patient");
        if (rs.next()) {
            log.debug("result of the query :" + rs.getString(1));
        }
        rs.close();
        st.close();
        con.close();
    }

    @Test
    public void TestNotAllowedQuery() throws  SQLException {

        String url = "jdbc:validate:postgresql://localhost/CHUV_MIPS";
        String user = "ipython";
        String passwd="ipython4thewin";

        Connection con = DriverManager.getConnection(url, user, passwd);
        Statement st = con.createStatement();

        ResultSet rs = null;
        try {
            rs = st.executeQuery("SELECT patient.* FROM patient");
            if (rs.next()) {
                log.debug("result of the query :" + rs.getString(1));
            }

            fail("query should not pass the filter");
        }
        catch(SQLFilterException e)
        {
            log.debug("Exception was thrown as it should");
            log.debug(e);
        }

        if (rs != null) rs.close();
        st.close();
        con.close();
    }

    void printMatch(java.util.regex.Pattern p, String s)
    {
        java.util.regex.Matcher m = p.matcher(s);
        if(m.find())
        {
            log.debug("group count " + m.groupCount());
            for (int n = 0 ; n <= m.groupCount() ; n ++)
            {
                log.debug("Found value: " + n +":"+ m.group(n));
            }
        }
        else
        {
            log.debug(" No match found for: " + s);
        }
    }

    static private String urlPrefix = "jdbc:validate:";

    private String[] getUrlAndConf(String url) throws SQLException
    {
        String[] parts = url.split("://");
        String header = parts[0];
        String rest = parts[1];

        header = header.substring(urlPrefix.length());
        parts =header.split(":");

        String realUrl = "jdbc:";
        String conf= null;
        int start = 0;
        if(parts.length > 1)
        {
            conf= parts[0];
            start = 1;
        }

        for(int n = start ; n < parts.length; n++ )
        {
            realUrl += parts[n] + ":";
        }

        realUrl += "//"+ rest;
        log.debug(url + " ->real url: " + realUrl + ", conf : " + conf);

        String[] out = {realUrl, conf};
        return out;
    }
    @Test
    public void testConfigurations() throws SQLException
    {
//        String url = "jdbc:validate:conf1:postgresql://localhost/CHUV_MIPS";
//        String user = "ipython";
//        String passwd="ipython4thewin";
//
//        Connection con = DriverManager.getConnection(url, user, passwd);
//        Statement st = con.createStatement();
//
//        ResultSet rs = null;
//
//        rs = st.executeQuery("SELECT count(patient.year_of_birth) FROM patient");
//        if (rs.next()) {
//            log.debug("result of the query :" + rs.getString(1));
//        }
//
//
//
//        if (rs != null) rs.close();
//        st.close();
//        con.close();

        log.debug(getUrlAndConf("jdbc:validate:conf1:postgresql://localhost/CHUV_MIPS"));
        fail("testing");

    }
}
