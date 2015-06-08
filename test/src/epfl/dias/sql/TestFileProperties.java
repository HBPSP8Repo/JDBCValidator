package epfl.dias.sql;

import com.sun.deploy.util.StringUtils;
import epfl.dias.Properties;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;

/**
 * Created by torcato on 08.06.15.
 */
public class TestFileProperties {

    Logger log = Logger.getLogger(TestFileProperties.class);

    private String filterConf = "/tmp/filter.conf.yaml";
    HashSet<String> drivers = new HashSet<String>( Arrays.asList("lala", "cucu", "pipi") );
    boolean autoLoadDrivers = true;

    String propertiesFile = "epfl.validator.properties";

    public TestFileProperties ()
    {
        System.setProperty("epfl.jdbc.validator.properties.file",propertiesFile);
        // will create a new configuration file
        try {

            PrintWriter writer = new PrintWriter(propertiesFile, "UTF-8");


            writer.write("validator.filterConfFile= "+filterConf+"\n");
            writer.write("validator.auto.load.popular.drivers=autoLoadDrivers"+autoLoadDrivers+"\n");

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
