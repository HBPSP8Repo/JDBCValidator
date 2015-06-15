package epfl.dias.sql;


import org.apache.log4j.Logger;
import java.sql.*;


import fr.maatg.fedehr.queryfilter.QueryAnalyzer;
import fr.maatg.fedehr.queryfilter.status.QueryFilterStatus;


/**
 * Created by torcato on 29-05-2015.
 * Class that implements a
 */
public class StatementValidator implements Statement
{

    /**
     * The Connection that created this Statement.
     */
    protected ConnectionValidator connection;

    /**
     * The real statement that this StatementValidator wraps.
     */
    protected Statement realStatement;

    protected QueryAnalyzer queryAnalyzer;

    protected String configName;

    /**
     * Get the real Statement that this StatementValidator wraps.
     *
     * @return the real Statement that this StatementValidator wraps.
     */
    public Statement getRealStatement()
    {
        return realStatement;
    }



    private final static Logger logger = Logger.getLogger(StatementValidator.class);

    /**
     * Create a Statement validator that wraps another Statement
     * for the purpose of logging all method calls, sql, exceptions and return values.
     *
     * @param connection Connection that created this Statement.
     * @param realStatement real underlying Statement that this StatementValidator wraps.
     */
    public StatementValidator(ConnectionValidator connection, Statement realStatement, String config)
    {
        if (realStatement == null)
        {
            throw new IllegalArgumentException("Must pass in a non null real Statement");
        }
        if (connection == null)
        {
            throw new IllegalArgumentException("Must pass in a non null ConnectionValidator");
        }
        this.realStatement = realStatement;
        this.connection = connection;

        queryAnalyzer = new QueryAnalyzer();
        configName = config;

        logger.info("using configuration " + ((configName==null)? "default": configName) );

    }


    /**
     *Checks the
     *
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected void checkStatementSql(String sql, String methodCall) throws SQLException
    {
        // redirect to one more method call ONLY so that stack trace search is consistent
        // with the reportReturn calls
        _checkSql(sql, methodCall);
    }

    /**
     * Check SQL query
     *
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected void checkSql(String sql, String methodCall) throws SQLException
    {
        // redirect to one more method call ONLY so that stack trace search is consistent
        // with the reportReturn calls
        _checkSql(sql, methodCall);
    }

    /**
     * The method that actually checks the sql statement
     * @param sql   the sql being run
     * @param methodCall the method that called the execution
     */
    private void _checkSql(String sql, String methodCall) throws SQLException
    {
        //TODO: add checking of sql here

        logger.debug("Checking sql, conf: " +configName+ ", method: "
                +this.getClass().getName()+ "."+ methodCall + ", sql: " + sql);

        //TODO: check the license path
        String licencePath =Properties.getFilterLicenseFile();
        String queryFilterScript = Properties.getFilterConFile(configName);

        QueryFilterStatus status;
        try {
            status = queryAnalyzer.isQueryValid(licencePath, sql, queryFilterScript);

        }
        catch (Exception e)
        {
            throw new ValidatorException("Error Analysing the query ", e);
        }

        if (status.isNOK())
        {
            logger.error("query did not pass filter :" +
                                "\n\treason:" +status.getFields().toString() +
                                "\n\tsql='"+ sql + "'" );

            throw new SQLFilterException("Query did not pass query-filter, fields:" +status.getFields().toString() );
        }
    }


    // implementation of interface methods
    @Override
    public SQLWarning getWarnings() throws SQLException
    {
        return realStatement.getWarnings();
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException
    {
        String methodCall = "executeUpdate";
        checkStatementSql(sql, methodCall);

        return realStatement.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
        String methodCall = "execute";
        checkStatementSql(sql, methodCall);

        return realStatement.execute(sql, columnNames);
    }

    @Override
    public void setMaxRows(int max) throws SQLException
    {
        realStatement.setMaxRows(max);
    }

    @Override
    public boolean getMoreResults() throws SQLException
    {
        return realStatement.getMoreResults();
    }

    @Override
    public void clearWarnings() throws SQLException
    {
        realStatement.clearWarnings();
    }

    @Override
    public void addBatch(String sql) throws SQLException
    {
        String methodCall = "addBatch";
        checkSql(sql, methodCall);
        realStatement.addBatch(sql);
    }

    @Override
    public int getResultSetType() throws SQLException
    {
        return  realStatement.getResultSetType();
    }

    @Override
    public void clearBatch() throws SQLException
    {
        realStatement.clearBatch();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException
    {
        realStatement.setFetchDirection(direction);
    }

    @Override
    public int[] executeBatch() throws SQLException
    {
        //TODO: add cheking of queries on executeBatch instead of
        return realStatement.executeBatch();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException
    {
        realStatement.setFetchSize(rows);
    }

    @Override
    public int getQueryTimeout() throws SQLException
    {
        return realStatement.getQueryTimeout();
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return connection;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException
    {
        return realStatement.getGeneratedKeys();
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException
    {
        realStatement.setEscapeProcessing(enable);
    }

    @Override
    public int getFetchDirection() throws SQLException
    {
        return  realStatement.getFetchDirection();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException
    {
        realStatement.setQueryTimeout(seconds);
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException
    {
        return realStatement.getMoreResults(current);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException
    {
        String methodCall = "executeQuery";
        checkStatementSql(sql, methodCall);

        return realStatement.executeQuery(sql);
    }

    @Override
    public int getMaxFieldSize() throws SQLException
    {
            return  realStatement.getMaxFieldSize();
    }

    @Override
    public int executeUpdate(String sql) throws SQLException
    {
        String methodCall = "executeUpdate";
        checkStatementSql(sql, methodCall);

        return realStatement.executeUpdate(sql);
    }

    @Override
    public void cancel() throws SQLException
    {
        realStatement.cancel();
    }

    @Override
    public void setCursorName(String name) throws SQLException
    {
        realStatement.setCursorName(name);
    }

    @Override
    public int getFetchSize() throws SQLException
    {

        return realStatement.getFetchSize();

    }

    @Override
    public int getResultSetConcurrency() throws SQLException
    {
        return  realStatement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetHoldability() throws SQLException
    {
        return realStatement.getResultSetHoldability();

    }

    @Override
    public boolean isClosed() throws SQLException {
        return realStatement.isClosed();

    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

        realStatement.setPoolable(poolable);

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return realStatement.isPoolable();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException
    {
        realStatement.setMaxFieldSize(max);
    }

    @Override
    public boolean execute(String sql) throws SQLException
    {
        String methodCall = "execute";

        checkStatementSql(sql, methodCall);
        return realStatement.execute(sql);

    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        String methodCall = "executeUpdate(" + sql + ", " + autoGeneratedKeys + ")";
        checkStatementSql(sql, methodCall);
        return  realStatement.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
    {
        String methodCall = "execute";
        checkStatementSql(sql, methodCall);
        return realStatement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        String methodCall = "executeUpdate";
        checkStatementSql(sql, methodCall);
        return  realStatement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
        String methodCall = "execute";
        checkStatementSql(sql, methodCall);
        return  realStatement.execute(sql, columnIndexes);
    }

    @Override
    public ResultSet getResultSet() throws SQLException
    {
        return  realStatement.getResultSet();
    }

    @Override
    public int getMaxRows() throws SQLException
    {
        return realStatement.getMaxRows();
    }

    @Override
    public void close() throws SQLException
    {
        realStatement.close();
    }

    @Override
    public int getUpdateCount() throws SQLException
    {
        return realStatement.getUpdateCount();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        return realStatement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return realStatement.isWrapperFor(iface);
    }

    @Override
    public void closeOnCompletion() throws SQLException
    {
        realStatement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException
    {
        return realStatement.isCloseOnCompletion();
    }

}
