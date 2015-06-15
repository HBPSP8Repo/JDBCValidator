package epfl.dias.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;


/**
 * Created by torcato on 29-05-2015.
 * Validator class for the for prepare statement
 */
public class PreparedStatementValidator  extends StatementValidator implements PreparedStatement
{

    /**
     * the keeps this in memory until execute just to check the sql
     */
    String sql;

    /**
     * The real PreparedStatement that this class wraps.
     */
    protected PreparedStatement realPreparedStatement;

    /**
     * Get the real PreparedStatement that this class wraps.
     *
     * @return the real PreparedStatement that this wraps.
     */
    public PreparedStatement getRealPreparedStatement()
    {
        return realPreparedStatement;
    }

    /**
     * Create a PreparedStatementValidator (JDBC 4 version) for logging activity of another PreparedStatement.
     *
     * @param sql                   SQL for the prepared statement that is being spied upon.
     * @param connection         Connection that was called to produce this PreparedStatement.
     * @param realPreparedStatement The actual PreparedStatement that is being spied upon.
     */
    public PreparedStatementValidator(String sql, ConnectionValidator connection, PreparedStatement realPreparedStatement, String config)
    {
        super(connection, realPreparedStatement, config);  // does null check for us
        this.sql = sql;
        this.realPreparedStatement = realPreparedStatement;
    }

    public String getClassType()
    {
        return "PreparedStatement";
    }

    // forwarding methods

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException
    {
        realPreparedStatement.setTime(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
    {
        realPreparedStatement.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
    {
        realPreparedStatement.setCharacterStream(parameterIndex, reader, length);

    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException
    {
        realPreparedStatement.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException
    {
        realPreparedStatement.setNull(paramIndex, sqlType, typeName);

    }

    @Override
    public void setRef(int i, Ref x) throws SQLException
    {
        realPreparedStatement.setRef(i, x);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException
    {
        realPreparedStatement.setBoolean(parameterIndex, x);
    }

    @Override
    public void setBlob(int i, Blob x) throws SQLException
    {
        realPreparedStatement.setBlob(i, x);
    }

    @Override
    public void setClob(int i, Clob x) throws SQLException
    {
        realPreparedStatement.setClob(i, x);
    }

    @Override
    public void setArray(int i, Array x) throws SQLException
    {
        realPreparedStatement.setArray(i, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException
    {
        realPreparedStatement.setByte(parameterIndex, x);
    }

    /**
     * @deprecated
     */
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
        realPreparedStatement.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException
    {
        realPreparedStatement.setShort(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException
    {
        String methodCall =  "execute()";
        //String dumpedSql = dumpedSql();
        //reportSql(dumpedSql, methodCall);
        checkSql(sql, methodCall);

        return realPreparedStatement.execute();

    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException
    {
        realPreparedStatement.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException
    {
        realPreparedStatement.setLong(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException
    {
        realPreparedStatement.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException
    {
        realPreparedStatement.setDouble(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
    {
        realPreparedStatement.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException
    {
        realPreparedStatement.setURL(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException
    {
        realPreparedStatement.setString(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException
    {
        realPreparedStatement.setBytes(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException
    {
        realPreparedStatement.setDate(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException
    {
        return realPreparedStatement.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        realPreparedStatement.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        realPreparedStatement.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        realPreparedStatement.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        realPreparedStatement.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        realPreparedStatement.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        realPreparedStatement.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        realPreparedStatement.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        realPreparedStatement.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
    {
        realPreparedStatement.setDate(parameterIndex, x, cal);
    }

    @Override
    public ResultSet executeQuery() throws SQLException
    {
        String methodCall = "executeQuery()";
        //String dumpedSql = dumpedSql();
        //reportSql(dumpedSql, methodCall);
        checkSql(sql, methodCall);
        return realPreparedStatement.executeQuery();
    }

//    private String getTypeHelp(Object x)
//    {
//        if (x==null)
//        {
//            return "(null)";
//        }
//        return "(" + x.getClass().getName() + ")";
//    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException
    {
        realPreparedStatement.setObject(parameterIndex, x, targetSqlType, scale);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

        realPreparedStatement.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        realPreparedStatement.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        realPreparedStatement.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
            realPreparedStatement.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        realPreparedStatement.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        realPreparedStatement.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        realPreparedStatement.setNCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        realPreparedStatement.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        realPreparedStatement.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        realPreparedStatement.setNClob(parameterIndex, reader);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
    {
        realPreparedStatement.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException
    {
        realPreparedStatement.setObject(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
    {
        realPreparedStatement.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
    {
        realPreparedStatement.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public int executeUpdate() throws SQLException
    {
        String methodCall = "executeUpdate()";
        //String dumpedSql = dumpedSql();
        //reportSql(dumpedSql, methodCall);
        checkSql(sql, methodCall);
        return realPreparedStatement.executeUpdate();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException
    {
        return realPreparedStatement.getGeneratedKeys();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
        realPreparedStatement.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
        realPreparedStatement.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void clearParameters() throws SQLException
    {
        realPreparedStatement.clearParameters();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException
    {
        return realPreparedStatement.getMetaData();
    }

    @Override
    public void addBatch() throws SQLException
    {
        realPreparedStatement.addBatch();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return realPreparedStatement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return realPreparedStatement.isWrapperFor(iface);
    }

}
