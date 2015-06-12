package epfl.dias.sql;

import java.sql.*;

/**
 * Created by torcato on 12-06-2015.
 * Tests for the prepared statement
 * Will perform the same tests as TestQueries but use instead a PreparedStatement
 */
public class TestPreparedStatement extends TestStatementAncestor {

    @Override
    protected void runExecuteQuery(String url, String query) throws SQLException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = connect(url);
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            if (rs.next())
            {
                log.debug("result of the query :" + rs.getString(1));
            }
        }
        catch(SQLException e)
        {
            log.debug(e);
            throw e;
        }
        finally {
            if (con != null) con.close();
            if (st != null) st.close();
            if (rs != null) rs.close();
        }
        con.close();
    }

    @Override
    protected void runExecute(String url, String query) throws SQLException
    {
        Connection con = null;
        PreparedStatement st = null;

        try {
            con = connect(url);
            st = con.prepareStatement(query);
            st.execute();
        }
        catch(SQLException e)
        {
            log.debug(e);
            throw e;
        }
        finally {
            if (con != null) con.close();
            if (st != null) st.close();
        }
        con.close();
    }



}
