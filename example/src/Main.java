import epfl.dias.sql.ConnectionValidator;
import epfl.dias.sql.DriverValidator;

import java.sql.*;

/**
 * Created by torcato on 6/2/2015.
 */
public class Main {
    public static void main(String[] args) {

        try {
            Class.forName("epfl.dias.sql.DriverValidator");
            Class.forName("org.postgresql.Driver");
        }
        catch(ClassNotFoundException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {

            String url = "jdbc:validate:postgresql://localhost/CHUV_MIPS";
            String user = "ipython";
            String passwd="ipython4thewin";

            Connection con = DriverManager.getConnection(url, user, passwd);
            Statement st = con.createStatement();
            ResultSet rs;
            try {
                rs = st.executeQuery("SELECT VERSION()");
                if (rs.next()) {
                    System.out.println(rs.getString(1));
                }
                rs.close();
            }
            catch (SQLDataException ex) {
                System.out.println("got exception");
                System.out.println(ex);
            }

            st.close();
            con.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
