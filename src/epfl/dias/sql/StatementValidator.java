package epfl.dias.sql;


import org.apache.log4j.Logger;

import java.sql.*;

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
     * The real statement that this StatementSpy wraps.
     */
    protected Statement realStatement;


    /**
     * Get the real Statement that this StatementSpy wraps.
     *
     * @return the real Statement that this StatementSpy wraps.
     */
    public Statement getRealStatement()
    {
        return realStatement;
    }


    private final static Logger logger = Logger.getLogger(StatementValidator.class);

    /**
     * Create a StatementSpy that wraps another Statement
     * for the purpose of logging all method calls, sql, exceptions and return values.
     *
     * @param connection Connection that created this Statement.
     * @param realStatement real underlying Statement that this StatementSpy wraps.
     */
    public StatementValidator(ConnectionValidator connection, Statement realStatement)
    {
        if (realStatement == null)
        {
            throw new IllegalArgumentException("Must pass in a non null real Statement");
        }
        if (connection == null)
        {
            throw new IllegalArgumentException("Must pass in a non null ConnectionSpy");
        }
        this.realStatement = realStatement;
        this.connection = connection;

    }

//    //@Override
//    public String getClassType()
//    {
//        return "Statement";
//    }
//
//    //@Override
//    public Integer getConnectionNumber()
//    {
//        return connection.getConnectionNumber();
//    }


    /**
     * Report SQL for logging with a warning that it was generated from a statement.
     *
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected void checkStatementSql(String sql, String methodCall)
    {
        // redirect to one more method call ONLY so that stack trace search is consistent
        // with the reportReturn calls
        _checkSql(sql, methodCall);
    }

    /**
     * Report SQL for logging.
     *
     * @param sql        the SQL being run
     * @param methodCall the name of the method that was running the SQL
     */
    protected void checkSql(String sql, String methodCall)
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
    private void _checkSql(String sql, String methodCall)
    {
        //TODO: add checking of sql here

        logger.info("method: " + methodCall + ", sql ->" + sql);
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
        String methodCall = "executeUpdate(" + sql + ", " + columnNames.toString() + ")";
        checkStatementSql(sql, methodCall);

        return realStatement.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
        String methodCall = "execute(" + sql + ", " + columnNames.toString() + ")";
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
        String methodCall = "addBatch(" + sql + ")";
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
        String methodCall = "executeQuery(" + sql + ")";
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
        String methodCall = "executeUpdate(" + sql + ")";
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
        String methodCall = "execute(" + sql + ")";

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
        String methodCall = "execute(" + sql + ", " + autoGeneratedKeys + ")";
        checkStatementSql(sql, methodCall);
        return realStatement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        String methodCall = "executeUpdate(" + sql + ", " + columnIndexes + ")";
        checkStatementSql(sql, methodCall);
        return  realStatement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
        String methodCall = "execute(" + sql + ", " + columnIndexes + ")";
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
