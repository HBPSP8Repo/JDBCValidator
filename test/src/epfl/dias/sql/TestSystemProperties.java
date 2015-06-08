package epfl.dias.sql;

import com.sun.deploy.util.StringUtils;
import epfl.dias.Properties;
import org.apache.log4j.Logger;
import org.junit.Test;


import java.util.Arrays;

import java.util.HashSet;


import static junit.framework.Assert.assertEquals;


/**
 * Created by torcato on 08-06-2015.
 * Unit tests for the properties class
 */
public class TestSystemProperties {


    private static Logger log = Logger.getLogger( Properties.class );


    /**
     * Will set all the system properties here
     */
    public void TestSystemProperties()
    {

    }

    @Test
    public void testGetFilterConfFile()
    {

        String filterConf = "/tmp/filter.conf.yaml";
        System.setProperty("validator.filterConfFile", filterConf);

        System.setProperty("validator.auto.load.popular.drivers", "true");

        HashSet<String> drivers = new HashSet<String>( Arrays.asList( "lala", "cucu", "pipi") );
        // creates a comma separated string from drivers and sets to the property
        System.setProperty("validator.extra.drivers", StringUtils.join(drivers, ","));

        Properties props = new Properties();

        assertEquals(filterConf, props.getFilterConFile());
        assertEquals(true, props.isAutoLoadPopularDrivers());


        log.debug(props.getAdditionalDrivers());
        assertEquals(drivers, props.getAdditionalDrivers());


    }

}
