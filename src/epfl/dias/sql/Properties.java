package epfl.dias.sql;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.log4j.Logger;




/**
 * Created by torcato on 29-05-2015.
 * Class to load properties for the proxy driver
 */
public final class Properties
{

    /**
     * Attempt to Automatically load a set of popular JDBC drivers?
     */
    static private boolean AutoLoadPopularDrivers;
    /**
     * A <code>Collection</code> of <code>String</code>s listing the additional drivers
     * to use beside the default drivers auto-loaded.
     */
    static private Collection<String> AdditionalDrivers;


    static private final Logger log= Logger.getLogger(Properties.class);

    /**
     *  Default configuration file for the query filter
     */
    static private String filterConfFile;

    /**
     * The license file for the query filter
     */
    static private String filterLicenseFile;

    /**
     *  Default query delay
     */
    static Integer queryDelay;


    /**
     * Created by torcato on 09.06.15.
     * Struct like class to hold specific settings for a single connection
     */
    static private class ConnectionsConfig {
        /**
         * This will hold the query filter yaml script
         */
        public String filterConfigFile;
        /**
         * This is a optional parameter to set a delay in each query
         */
        public Integer queryDelay;
    }

    /**
     *A dictionary to keep all
     */
    static private java.util.Map<String, ConnectionsConfig> configs;


    /**
     * Static initializer.
     */
    static
    {
        reloadConfig();
    }

    static void reloadConfig()
    {
        java.util.Properties props = getProperties();

        AutoLoadPopularDrivers = getBooleanOption(props,
                "validator.auto.load.popular.drivers", true);

        // look for additional driver specified in properties
        String moreDrivers = getStringOption(props, "validator.extra.drivers");

        AdditionalDrivers = new HashSet<String>();
        if (moreDrivers != null) {
            String[] moreDriversArr = moreDrivers.split(",");
            AdditionalDrivers.addAll(Arrays.asList(moreDriversArr));
        }

        filterConfFile = getStringOption(props, "validator.default.filter.confFile");
        if (filterConfFile == null) filterConfFile = "default.filter.yaml";

        queryDelay = getIntegerOption(props, "validator.default.query.delay");
        if(queryDelay == null) queryDelay = 0;

        filterLicenseFile = getStringOption(props, "validator.filter.license.file");
        //if(filterConfFile == null) filterConfFile = "";

        configs = new HashMap<String, ConnectionsConfig>();

        String conf = getStringOption(props, "validator.configurations");
        if ( conf!= null)
        {
            String[] configsArr = conf.split(",");
            for(String configName: configsArr)
            {
                ConnectionsConfig config = new ConnectionsConfig();

                config.filterConfigFile = getStringOption(props, "validator."+configName+".filter.confFile");
                if (config.filterConfigFile == null) config.filterConfigFile = filterConfFile;

                config.queryDelay = getIntegerOption(props, "validator." + configName + ".query.delay");
                if (config.queryDelay == null) config.queryDelay = queryDelay;

                configs.put(configName, config);
                //connectionsConfig[configName]=config;
            }
        }

        log.info(printConfig());
    }

    public static String printConfig()
    {
        String s ="configuration settings :" +
                "\n\tvalidator.extra.drivers = " + getAdditionalDrivers() +
                "\n\tvalidator.auto.load.popular.drivers = " + isAutoLoadPopularDrivers() +
                "\n\tvalidator.default.filter.confFile = " + getFilterConFile() +
                "\n\tvalidator.filter.license.file = " + getFilterLicenseFile() +
                "\n\tvalidator.default.query.delay = " + getQueryDelay() +
                "\n\tvalidator.configurations = " + getAvailableConfigurations();

        for (String conf : configs.keySet())
        {
            s += "\n\tvalidator." + conf + ".filter.confFile = " + getFilterConFile(conf);
            s += "\n\tvalidator." + conf + ".query.delay = " + getQueryDelay(conf);
        }

        return s;
    }

    /**
     * Get a String option from a property and
     * log a debug message about this.
     *
     * @param props Properties to get option from.
     * @param propName property key.
     * @return the value of that property key.
     */
    private static String getStringOption(java.util.Properties props, String propName)
    {
        String propValue = props.getProperty(propName);
        if (propValue == null)
        {
            log.warn("x " + propName + " is not defined");
        }
        else if (propValue.length()==0)
        {
            // force to null, even if empty String
            log.warn( propName + " is empty, setting to null");
            propValue = null;
        }

        return propValue;
    }

    /**
     * Get a boolean option from a property and
     * log a debug message about this.
     *
     * @param props Properties to get option from.
     * @param propName property name to get.
     * @param defaultValue default value to use if undefined.
     *
     * @return boolean value found in property, or defaultValue if no property
     *         found.
     */
    private static boolean getBooleanOption(java.util.Properties props, String propName,
                                            boolean defaultValue)
    {
        String propValue = props.getProperty(propName);
        boolean val;
        if (propValue == null) {
            log.warn("x " + propName + " is not defined");
            return defaultValue;
        }
        propValue = propValue.trim().toLowerCase();
        if (propValue.length() == 0) {
            val = defaultValue;
        } else {
            val= "true".equals(propValue) ||
                    "yes".equals(propValue) ||
                    "on".equals(propValue);
        }
        return val;
    }

    /**
     * Get a Long option from a property and
     * log a debug message about this.
     *
     * @param props Properties to get option from.
     * @param propName property key.
     *
     * @return the value of that property key, converted
     * to a Long.  Or null if not defined or is invalid.
     */
    private static Integer getIntegerOption(java.util.Properties props, String propName)
    {
        String propValue = props.getProperty(propName);
        Integer intValue = null;

        if (propValue == null)
        {
            log.warn("x " + propName + " is not defined");
        }
        else
        {
            try
            {
                propValue = propValue.trim();
                intValue = Integer.parseUnsignedInt(propValue);
                log.debug("  " + propName + " = " + intValue);
            }
            catch (NumberFormatException n)
            {
                log.warn("x " + propName + " \"" + propValue  +
                        "\" is not a valid number");
            }
        }
        return intValue;
    }

    /**
     * Get the <code>java.util.Properties</code> either from the System properties,
     * or from a configuration file.
     * @return 		The <code>java.util.Properties</code> to get the properties from.
     */
    private static java.util.Properties getProperties()
    {
        java.util.Properties props = new java.util.Properties(System.getProperties());
        //try to get the properties file.
        //check first if an alternative name has been provided in the System properties
        String propertyFile = props.getProperty("epfl.jdbc.validator.properties.file",
                            "/epfl.validator.properties");

        InputStream propStream = Properties.class.getResourceAsStream(propertyFile);

        if (propStream != null) {
            try {
                props.load(propStream);
            } catch (IOException e) {
                log.error("Error when loading properties from classpath: ", e);

            } finally {
                try {
                    propStream.close();
                } catch (IOException e) {
                    log.error("Error closing properties input stream: ", e);
                }
            }

            log.info("properties loaded from " + propertyFile);

        } else {
            log.warn("file " + propertyFile + " not found in classpath. Using System properties.");
        }
        return props;
    }

    /**
     * @return the AutoLoadPopularDrivers
     * @see #AutoLoadPopularDrivers
     */
    public static boolean isAutoLoadPopularDrivers()
    {
        return AutoLoadPopularDrivers;
    }
    /**
     * @return the AdditionalDrivers
     * @see #AdditionalDrivers
     */
    public static Collection<String> getAdditionalDrivers()
    {
        return AdditionalDrivers;
    }

    public static String getFilterConFile()
    {
        return filterConfFile;
    }

    public static String getFilterConFile(String configName)
    {
        if(configName != null) {
            ConnectionsConfig conf = configs.get(configName);
            return conf.filterConfigFile;
        }
        else
        {
            return getFilterConFile();
        }
    }

    public static Integer getQueryDelay()
    {
        return queryDelay;
    }

    public static Integer getQueryDelay(String configName)
    {
        ConnectionsConfig conf = configs.get(configName);
        return conf.queryDelay;
    }

    public static Set<String> getAvailableConfigurations()
    {

        return configs.keySet();
    }
    public static String getFilterLicenseFile()
    {
        return filterLicenseFile;
    }
}
