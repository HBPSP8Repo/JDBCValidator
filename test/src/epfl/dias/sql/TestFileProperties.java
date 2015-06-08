package epfl.dias.sql;

import com.sun.deploy.util.StringUtils;
import epfl.dias.Properties;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import java.io.File;
import java.io.PrintWriter;

import java.nio.file.*;
import java.util.Arrays;
import java.util.HashSet;


import static junit.framework.Assert.assertEquals;

/**
 * Created by torcato on 08.06.15.
 */
public class TestFileProperties {

    private static Logger log = Logger.getLogger(TestFileProperties.class);

    private static String filterConf = "/tmp/filter2.conf.yaml";
    private static HashSet<String> drivers = new HashSet<String>( Arrays.asList("lala", "cucu", "pipi") );
    private static boolean autoLoadDrivers = true;

    private static String propertiesFile = "/epfl.validator.properties";

    @BeforeClass
    public static void createFile() {
        //System.setProperty("epfl.jdbc.validator.properties.file",propertiesFile);
        // will create a new configuration file
        // here it gets the class path
        String path = TestFileProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try {

            PrintWriter writer = new PrintWriter(path + propertiesFile, "UTF-8");


            writer.write("validator.filterConfFile= "+filterConf+"\n");
            writer.write("validator.auto.load.popular.drivers="+autoLoadDrivers+"\n");

            // creates a comma separated string from drivers and sets to the property
            writer.write("validator.extra.drivers = " + StringUtils.join(drivers, ",") + "\n");
            writer.close();

        }
        catch(Exception e)
        {
            log.error("could not write properties file" , e);
            System.exit(1);
        }
    }

    @AfterClass
    public static void cleanUp()
    {
        // deletes the generated properties file
        String path = TestFileProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File f = new File( path + propertiesFile);
        f.delete();

    }

    @Test
    public void testSettingsFile()
    {

        Properties props = new Properties();

        assertEquals(filterConf, props.getFilterConFile());
        assertEquals(autoLoadDrivers, props.isAutoLoadPopularDrivers());

        log.debug(props.getAdditionalDrivers());
        assertEquals(drivers, props.getAdditionalDrivers());

    }
}
