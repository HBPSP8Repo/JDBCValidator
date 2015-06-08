package epfl.dias;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
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
    static final boolean AutoLoadPopularDrivers;
    /**
     * A <code>Collection</code> of <code>String</code>s listing the additional drivers
     * to use beside the default drivers auto-loaded.
     */
    static final Collection<String> AdditionalDrivers;

    static private final Logger logger = Logger.getLogger(Properties.class);

    static private String filterConfFile;




    /**
     * Static initializer.
     */
    static
    {
        java.util.Properties props = getProperties();

        AutoLoadPopularDrivers = getBooleanOption(props,
                "validator.auto.load.popular.drivers", true);

        // look for additional driver specified in properties
        String moreDrivers = getStringOption(props, "validator.extra.drivers");
        AdditionalDrivers = new HashSet<String>();

        if (moreDrivers != null) {
            String[] moreDriversArr = moreDrivers.split(",");
            for (String s : moreDriversArr) {
                AdditionalDrivers.add(s);
            }
        }

        filterConfFile = getStringOption(props, "validator.filterConfFile");
        if (filterConfFile == null) filterConfFile = "query.filter.yaml";
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
        if (propValue == null || propValue.length()==0)
        {
            propValue = null; // force to null, even if empty String
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
                            "./epfl.validator.properties");

        InputStream propStream = Properties.class.getResourceAsStream(propertyFile);

        if (propStream != null) {
            try {
                props.load(propStream);
            } catch (IOException e) {
                logger.error("Error when loading properties from classpath: ", e);

            } finally {
                try {
                    propStream.close();
                } catch (IOException e) {
                    logger.error("Error closing properties input stream: ", e);
                }
            }

            logger.info("properties loaded from " + propertyFile);

        } else {
            logger.warn("properties not found in classpath. Using System properties.");
        }
        return props;
    }

    /**
     * @return the AutoLoadPopularDrivers
     * @see #AutoLoadPopularDrivers
     */
    public static boolean isAutoLoadPopularDrivers() {
        return AutoLoadPopularDrivers;
    }
    /**
     * @return the AdditionalDrivers
     * @see #AdditionalDrivers
     */
    public static Collection<String> getAdditionalDrivers() {
        return AdditionalDrivers;
    }

    public static String getFilterConFile(){ return filterConfFile; }
}
