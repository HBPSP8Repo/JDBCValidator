package epfl.dias.sql;

import com.sun.deploy.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;


import epfl.dias.Properties;

/**
 * Created by torcato on 08-06-2015.
 * Unit tests for the properties class
 */
public class TestSystemProperties {

    private static HashSet<String> drivers = new HashSet<String>( Arrays.asList("lala", "cucu", "pipi") );
    private static HashSet<String> configurations = new HashSet<String>( Arrays.asList("chuv", "hug") );
    private static boolean autoLoadDrivers = true;


    private static Integer defaultQueryDelay = 1;
    private static String defaultFilterConf = "some.conf.yaml";
    private static String licenseFile=  "/tmp/license.txt";

    // will set all the properties in this function
    @BeforeClass
    public static void setProperties()
    {

        System.setProperty("validator.auto.load.popular.drivers", autoLoadDrivers ? "true" : "false");
        // creates a comma separated string from drivers and sets to the property
        System.setProperty("validator.extra.drivers" , StringUtils.join(drivers, ",") );
        System.setProperty("validator.default.filter.confFile" , defaultFilterConf );
        System.setProperty("validator.default.query.delay" , defaultQueryDelay.toString() );
        System.setProperty("validator.filter.license.file" , licenseFile );

        System.setProperty("validator.configurations", StringUtils.join(configurations, ",") );
        System.setProperty("validator.chuv.filter.confFile", "/tmp/conf1.yaml");
        System.setProperty("validator.hug.query.delay", "2");

    }

    /**
     * Tests the global configurations/default
     */
    @Test
    public void testDefaultconfig()
    {

        assertEquals(defaultFilterConf, Properties.getFilterConFile());
        assertEquals(autoLoadDrivers, Properties.isAutoLoadPopularDrivers());

        assertEquals(drivers, Properties.getAdditionalDrivers());
        assertEquals(licenseFile, Properties.getFilterLicenseFile());

    }

    /**
     * Tests the non global settings, filter config script and  query delay
     */
    @Test
    public void testConfigurations()
    {
        assertEquals(configurations, Properties.getAvailableConfigurations());

        // the values defined in the properties file
        assertEquals("/tmp/conf1.yaml", Properties.getFilterConFile("chuv"));
        assertEquals( new Integer(2) , Properties.getQueryDelay("hug") );

        //as these are not defined in the properties file it will take the default values
        assertEquals(defaultFilterConf, Properties.getFilterConFile("hug"));
        assertEquals( defaultQueryDelay, Properties.getQueryDelay("chuv") );

    }

}
