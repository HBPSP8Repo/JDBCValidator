package epfl.dias.sql;

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * Created by torcato on 29-05-2015.
 * A proxy jdbc driver that will perform a validation on the sql for agregates
 * This will contain the real driver and use gnubila's library for the actual sql validation
 *
 */
public class DriverValidator implements Driver {
    /**
     * The last actual, underlying driver that was requested via a URL.
     */
    private Driver lastUnderlyingDriverRequested;

    private final static Logger log = Logger.getLogger(DriverValidator.class);
    /**
     * A <code>String</code> representing the prefix of URL
     * to use validator.
     */
    static final private String urlPrefix = "jdbc:validate:";

    /**
     * Default constructor.
     */
    public DriverValidator()
    {

    }

    /**
     * Static initializer.
     */
    static
    {
        Set<String> subDrivers = new TreeSet<String>();

        if (epfl.dias.Properties.isAutoLoadPopularDrivers()) {

            log.info("Auto populating drivers");
            subDrivers.add("oracle.jdbc.driver.OracleDriver");
            subDrivers.add("oracle.jdbc.OracleDriver");
            subDrivers.add("com.sybase.jdbc2.jdbc.SybDriver");
            subDrivers.add("net.sourceforge.jtds.jdbc.Driver");

            // MS driver for Sql Server 2000
            subDrivers.add("com.microsoft.jdbc.sqlserver.SQLServerDriver");

            // MS driver for Sql Server 2005
            subDrivers.add("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            subDrivers.add("weblogic.jdbc.sqlserver.SQLServerDriver");
            subDrivers.add("com.informix.jdbc.IfxDriver");
            subDrivers.add("org.apache.derby.jdbc.ClientDriver");
            subDrivers.add("org.apache.derby.jdbc.EmbeddedDriver");
            subDrivers.add("com.mysql.jdbc.Driver");
            subDrivers.add("org.postgresql.Driver");
            subDrivers.add("org.hsqldb.jdbcDriver");
            subDrivers.add("org.h2.Driver");
        }

        // look for additional driver specified in properties
        subDrivers.addAll(epfl.dias.Properties.getAdditionalDrivers());

        try {
            DriverManager.registerDriver(new DriverValidator());
        } catch (SQLException s) {
            // this exception should never be thrown, JDBC just defines it
            // for completeness

            log.error("could not register DriverValidator ", s);
            throw (RuntimeException) new RuntimeException
                    ("could not register validator driver!").initCause(s);
        }

        // instantiate all the supported drivers and remove
        // those not found
        for (Iterator<String> i = subDrivers.iterator(); i.hasNext();) {
            String driverClass = i.next();
            try {
                Class.forName(driverClass);
                log.info("FOUND DRIVER " + driverClass);
            } catch (Throwable c) {
                i.remove();
            }
        }

        if (subDrivers.size() == 0) {
            log.warn("could not load any driver");
        }


    }

    /**
     * Get the major version of the driver.  This call will be delegated to the
     * underlying driver that is being spied upon (if there is no underlying
     * driver found, then 1 will be returned.)
     *
     * @return the major version of the JDBC driver.
     */
    @Override
    public int getMajorVersion()
    {
        if (lastUnderlyingDriverRequested == null) {
            return 1;
        }
        return lastUnderlyingDriverRequested.getMajorVersion();
    }

    /**
     * Get the minor version of the driver.  This call will be delegated to the
     * underlying driver that is being spied upon (if there is no underlying
     * driver found, then 0 will be returned.)
     *
     * @return the minor version of the JDBC driver.
     */
    @Override
    public int getMinorVersion()
    {
        if (lastUnderlyingDriverRequested == null) {
            return 0;
        }
        return lastUnderlyingDriverRequested.getMinorVersion();
    }

    /**
     * Report whether the underlying driver is JDBC compliant.  If there is no
     * underlying driver, false will be returned, because the driver cannot
     * actually do any work without an underlying driver.
     *
     * @return <code>true</code> if the underlying driver is JDBC Compliant;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean jdbcCompliant()
    {
        return lastUnderlyingDriverRequested != null &&
                lastUnderlyingDriverRequested.jdbcCompliant();
    }

    /**
     * Returns true if this is a <code>dbc:validade</code> URL and if the URL is for
     * an underlying driver that this DriverSpy can spy on.
     *
     * @param url JDBC URL.
     *
     * @return true if this Driver can handle the URL.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException
    {
        Driver d = getUnderlyingDriver(url);
        if (d != null) {
            lastUnderlyingDriverRequested = d;
            return true;
        }
        return false;
    }

    /**
     * Given a <code>dbc:validade</code> type URL, find the underlying real driver
     * that accepts the URL.
     *
     * @param url JDBC connection URL.
     *
     * @return Underlying driver for the given URL. Null is returned if the URL is
     *         not a <code>jdbc:validade</c type URL or there is no underlying
     *         driver that accepts the URL.
     *
     * @throws SQLException if a database access error occurs.
     */
    private Driver getUnderlyingDriver(String url) throws SQLException
    {
        if (url.startsWith(urlPrefix)) {
            url = this.getRealUrl(url);

            Enumeration<Driver> e = DriverManager.getDrivers();

            Driver d;
            while (e.hasMoreElements()) {
                d = e.nextElement();

                if (d.acceptsURL(url)) {
                    return d;
                }
            }
        }
        return null;
    }

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

        return new String[]{realUrl, conf};
    }

    /**
     * Get the actual URL that the real driver expects
     * (strip off <code>#urlPrefix</code> from <code>url</code>).
     *
     * @param url 	A <code>String</code> corresponding to a JDBC url
     * @return 		A <code>String</code> representing url
     * 				with <code>#urlPrefix</code> stripped off.
     */
    private String getRealUrl(String url) throws SQLException
    {
       return getUrlAndConf(url)[0];
    }

    private String getConfigurationName(String url)throws SQLException
    {
        return getUrlAndConf(url)[1];
    }

    /**
     * Get a Connection to the database from the underlying driver that this
     * validator is checking on
     * @param url  JDBC connection URL
     * .
     * @param info a list of arbitrary string tag/value pairs as
     *             connection arguments. Normally at least a "user" and
     *             "password" property should be included.
     *
     * @return     a <code>Connection</code> object that represents a
     *             connection to the URL.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Connection connect(String url, java.util.Properties info) throws SQLException
    {
        Driver d = getUnderlyingDriver(url);
        if (d == null) {
            return null;
        }

        // get actual URL that the real driver expects
        // (strip off validator from url)
        String realUrl = this.getRealUrl(url);

        lastUnderlyingDriverRequested = d;
        Connection c = d.connect(realUrl, info);

        if (c == null) {
            throw new SQLException("invalid or unknown driver url: " + realUrl);
        }

        String configName = getConfigurationName(url);
        log.debug("Creating connection-> url:"+ url +", real url:" + realUrl + ", conf: " + configName);
        return new ConnectionValidator(c, configName);
    }

    /**
     * Gets information about the possible properties for the underlying driver.
     *
     * @param url  the URL of the database to which to connect
     *
     * @param info a proposed list of tag/value pairs that will be sent on
     *             connect open
     * @return     an array of <code>DriverPropertyInfo</code> objects describing
     *             possible properties.  This array may be an empty array if no
     *             properties are required.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info)
            throws SQLException
    {
        Driver d = getUnderlyingDriver(url);
        if (d == null)
        {
            return new DriverPropertyInfo[0];
        }

        lastUnderlyingDriverRequested = d;
        return d.getPropertyInfo(url, info);
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        return lastUnderlyingDriverRequested.getParentLogger();
    }
}
