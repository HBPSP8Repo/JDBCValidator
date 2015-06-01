package epfl.dias.sql;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by torcato on 29-05-2015.
 */
public class ConnectionValidator implements Connection
{
    private Connection realConnection;

    /**
     * Get the real underlying Connection that this connection wraps.
     *
     * @return the real underlying Connection.
     */
    public Connection getRealConnection()
    {
        return realConnection;
    }



    private final Integer connectionNumber;
    private static int lastConnectionNumber = 0;

    /**
     * Contains a Mapping of connectionNumber to currently open ConnectionSpy
     * objects.
     */
    private static final Map<Integer, ConnectionValidator> connectionTracker =
            new HashMap<Integer, ConnectionValidator>();

    /**
     * Get a dump of how many connections are open, and which connection numbers
     * are open.
     *
     * @return an open connection dump.
     */
    public static String getOpenConnectionsDump()
    {
        StringBuffer dump = new StringBuffer();
        int size;
        Integer[] keysArr;
        synchronized (connectionTracker)
        {
            size = connectionTracker.size();
            if (size==0)
            {
                return "open connections:  none";
            }
            Set<Integer> keys = connectionTracker.keySet();
            keysArr = keys.toArray(new Integer[keys.size()]);
        }

        Arrays.sort(keysArr);

        dump.append("open connections:  ");
        for (int i=0; i < keysArr.length; i++)
        {
            dump.append(keysArr[i]);
            dump.append(" ");
        }

        dump.append("(");
        dump.append(size);
        dump.append(")");
        return dump.toString();
    }


    /**
     * Create a new ConnectionSpy that wraps a given Connection.
     *
     * @param realConnection &quot;real&quot; Connection that this ConnectionSpy wraps.
     */
    public ConnectionValidator(Connection realConnection)
    {

        if (realConnection == null)
        {
            throw new IllegalArgumentException("Must pass in a non null real Connection");
        }
        this.realConnection = realConnection;

        synchronized (connectionTracker)
        {
            connectionNumber = new Integer(++lastConnectionNumber);
            connectionTracker.put(connectionNumber, this);
        }
    }


    public Integer getConnectionNumber()
    {
        return connectionNumber;
    }

    public String getClassType()
    {
        return "Connection";
    }
    // forwarding methods

    @Override
    public boolean isClosed() throws SQLException
    {
        return realConnection.isClosed();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException
    {
        return realConnection.getWarnings();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException
    {
        return realConnection.setSavepoint();
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {
        realConnection.releaseSavepoint(savepoint);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException
    {
        realConnection.rollback();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException
    {
        return realConnection.getMetaData();

    }

    @Override
    public void clearWarnings() throws SQLException
    {
        realConnection.clearWarnings();
    }

    @Override
    public Statement createStatement() throws SQLException
    {

        Statement statement = realConnection.createStatement();
        return  new StatementValidator(this, statement);

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
    {

        Statement statement = realConnection.createStatement(resultSetType, resultSetConcurrency);
        return  new StatementValidator(this, statement);

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        Statement statement = realConnection.createStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability);
        return  new StatementValidator(this, statement);

    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException
    {
        realConnection.setReadOnly(readOnly);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException
    {

        PreparedStatement statement = realConnection.prepareStatement(sql);
        return  new PreparedStatementValidator(sql, this, statement);

    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
        PreparedStatement statement = realConnection.prepareStatement(sql, autoGeneratedKeys);
        return new PreparedStatementValidator(sql, this, statement);

    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        PreparedStatement statement = realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        return new PreparedStatementValidator(sql, this, statement);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException
    {

        PreparedStatement statement = realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
        return new PreparedStatementValidator(sql, this, statement);

    }

    @Override
    public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException
    {

        PreparedStatement statement = realConnection.prepareStatement(sql, columnIndexes);
        return  new PreparedStatementValidator(sql, this, statement);

    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException
    {
        return realConnection.setSavepoint(name);

    }

    @Override
    public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException
    {
            PreparedStatement statement = realConnection.prepareStatement(sql, columnNames);
            return new PreparedStatementValidator(sql, this, statement);

    }

    @Override
    public Clob createClob() throws SQLException {
        return realConnection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return realConnection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return realConnection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return realConnection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
            return realConnection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        realConnection.setClientInfo(name,value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        realConnection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return realConnection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return realConnection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return realConnection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return realConnection.createStruct(typeName, attributes);
    }

    @Override
    public boolean isReadOnly() throws SQLException
    {
        return realConnection.isReadOnly();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException
    {
        realConnection.setHoldability(holdability);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException
    {
        CallableStatement statement = realConnection.prepareCall(sql);
        return new CallableStatementValidator(sql, this, statement);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {

        CallableStatement statement = realConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
        return new CallableStatementValidator(sql, this, statement);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException
    {
        CallableStatement statement = realConnection.prepareCall(sql, resultSetType, resultSetConcurrency,
                    resultSetHoldability);
        return new CallableStatementValidator(sql, this, statement);

    }

    @Override
    public void setCatalog(String catalog) throws SQLException
    {
        realConnection.setCatalog(catalog);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException
    {
        return realConnection.nativeSQL(sql);
    }

    @Override
    public Map<String,Class<?>> getTypeMap() throws SQLException
    {
        return realConnection.getTypeMap();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException
    {
        realConnection.setAutoCommit(autoCommit);
    }

    @Override
    public String getCatalog() throws SQLException
    {
        return realConnection.getCatalog();
    }

    @Override
    public void setTypeMap(java.util.Map<String,Class<?>> map) throws SQLException
    {
        realConnection.setTypeMap(map);
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException
    {
        realConnection.setTransactionIsolation(level);
    }

    @Override
    public boolean getAutoCommit() throws SQLException
    {
        return realConnection.getAutoCommit();
    }

    @Override
    public int getHoldability() throws SQLException
    {
        return realConnection.getHoldability();
    }

    @Override
    public int getTransactionIsolation() throws SQLException
    {
        return realConnection.getTransactionIsolation();
    }

    @Override
    public void commit() throws SQLException
    {
        realConnection.commit();
    }

    @Override
    public void rollback() throws SQLException
    {
        realConnection.rollback();
    }

    @Override
    public void close() throws SQLException
    {
        realConnection.close();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return realConnection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return realConnection.isWrapperFor(iface);
    }

    @Override
    public void setSchema(String schema) throws SQLException
    {
        realConnection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException
    {
        return realConnection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException
    {
        realConnection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
    {
        realConnection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return realConnection.getNetworkTimeout();
    }
}