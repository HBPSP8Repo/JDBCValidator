package epfl.dias.sql;

import epfl.dias.Properties;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.*;

/**
 * Created by torcato on 08.06.15.
 */
public class TestConnection {

    private static Logger log = Logger.getLogger( Properties.class );

    @Test
    public void TestSimpleQuery() throws ClassNotFoundException, SQLException {


        Class.forName("epfl.dias.sql.DriverValidator");

        String url = "jdbc:validate:postgresql://localhost/CHUV_MIPS";
        String user = "ipython";
        String passwd="ipython4thewin";

        Connection con = DriverManager.getConnection(url, user, passwd);
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT VERSION()");
        if (rs.next()) {
            log.debug(rs.getString(1));
        }
        rs.close();
        st.close();
        con.close();
    }
}
