package epfl.dias.sql;

import com.sun.deploy.util.StringUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;


import static org.junit.Assert.assertEquals;

/**
 * Created by torcato on 08.06.15.
 * Tests for loading properties from file
 */
public class TestFileProperties {

    private static Logger log = Logger.getLogger(TestFileProperties.class);

    private static String propertiesFile = "/epfl.test.validator.properties";

    private static HashSet<String> drivers = new HashSet<String>( Arrays.asList("lala", "cucu", "pipi") );
    private static HashSet<String> configurations = new HashSet<String>( Arrays.asList("chuv", "hug") );
    private static boolean autoLoadDrivers = true;

    private static Integer defaultQueryDelay = 1;
    private static String defaultFilterConf = "some.conf.yaml";
    private static String licenseFile=  "/tmp/license.txt";

    /**
     * Will create a properties file
     */
    @BeforeClass
    public static void createFile() {

        // will create a new configuration file, here it gets the class path
        String path = TestFileProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try {

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

            // sets the file to use
            System.setProperty("epfl.jdbc.validator.properties.file", propertiesFile);
            PrintWriter writer = new PrintWriter(path + propertiesFile, "UTF-8");
            writer.write("validator.auto.load.popular.drivers="+autoLoadDrivers+"\n");
            // creates a comma separated string from drivers and sets to the property
            writer.write("validator.extra.drivers = " + StringUtils.join(drivers, ",") + "\n");
            writer.write("validator.default.filter.confFile= "+defaultFilterConf+"\n");
            writer.write("validator.default.query.delay= "+defaultQueryDelay+"\n");
            writer.write("validator.filter.license.file= "+licenseFile+"\n");

            writer.write("validator.configurations = " + StringUtils.join(configurations, ",") + "\n");
            writer.write("validator.chuv.filter.confFile = /tmp/conf1.yaml\n");
            // to be sure there are no properties set

            writer.write("validator.hug.query.delay = 2");

            writer.close();

            Properties.reloadConfig();

        }
        catch(Exception e)
        {
            log.error("could not write properties file" , e);
            System.exit(1);
        }
    }

    /**
     * Deletes the file just created
     */
    @AfterClass
    public static void cleanUp()
    {
        // deletes the generated properties file
        String path = TestFileProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File f = new File( path + propertiesFile);
        if (!f.delete()) {
            log.error("could not delete file: " + path + propertiesFile);
        }

    }

    @Test
    public void testDefaultconfig()
    {

        assertEquals(defaultFilterConf, Properties.getFilterConFile());
        assertEquals(autoLoadDrivers, Properties.isAutoLoadPopularDrivers());

        assertEquals(drivers, Properties.getAdditionalDrivers());
        assertEquals(licenseFile, Properties.getFilterLicenseFile());

    }

    @Test
    public void testConfigurations()
    {
        assertEquals(configurations, Properties.getAvailableConfigurations());

        // the values defined in the properties file
        assertEquals("/tmp/conf1.yaml", Properties.getFilterConFile("chuv"));
        assertEquals( new Integer(2) , Properties.getQueryDelay("hug") );

        //as these are not defined in the properties file it will take the default values
        assertEquals(defaultFilterConf, Properties.getFilterConFile("hug"));
        assertEquals( defaultQueryDelay, Properties.getQueryDelay("chuv"));

    }
}
