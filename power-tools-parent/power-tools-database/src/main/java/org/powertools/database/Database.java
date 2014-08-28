package org.powertools.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.powertools.database.Query.column;
import org.powertools.engine.ExecutionException;


public final class Database {
    private final String mName;
    private final String mConnectString;
    private final String mUserName;
    private final String mPassword;

    private Connection mConnection;


    Database (String name, String connectString, String userName, String password) {
        mName          = name;
        mConnectString = connectString;
        mUserName      = userName;
        mPassword      = password;
    }

    String getName () {
        return mName;
    }
    
    boolean connect () {
        try {
            if (mConnection == null) {
                mConnection = DriverManager.getConnection (mConnectString, mUserName, mPassword);
            }
            return true;
        } catch (SQLException sqle) {
            throw newSqlException (sqle);
        }
    }

    void disconnect () {
        if (mConnection != null) {
            try {
                mConnection.close ();
                mConnection = null;
            } catch (SQLException sqle) {
                throw newSqlException (sqle);
            }
        }
    }
    
    
    Map<String, String> getRow (String tableName, List<String> columnNames, String keyName, String keyValue) throws SQLException {
        connect ();
        String query = String.format ("SELECT %s FROM %s WHERE %s = %s",
                        getColumnNames (columnNames), tableName, keyName, keyValue);
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultset = statement.executeQuery (query);
            return createRow (resultset, columnNames);
        } finally {
            if (statement != null) {
                statement.close ();
            }
        }
    }

    Map<String, String> getRow (String tableName, String keyName, String keyValue) throws SQLException {
        SelectQuery query = new SelectQuery ()
                .select ("*")
                .from (tableName)
                .where (column (keyName).equal (keyValue));
        return getRow (query.toString ());
        //return getRow (String.format ("SELECT * FROM %s WHERE %s = %s", tableName, keyName, keyValue));
    }

    Map<String, String> getRow (String query) throws SQLException {
        connect ();
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultSet = statement.executeQuery (query);
            return createRow (resultSet);
        } finally {
            if (statement != null) {
                statement.close ();
            }
        }
    }

    List<Map<String, String>> getRows (String tableName, String keyName, String keyValue) throws SQLException {
        connect ();
        SelectQuery query = new SelectQuery ()
                .select ("*")
                .from (tableName)
                .where (column (keyName).equal (keyValue));
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultSet = statement.executeQuery (query.toString ());
            //ResultSet resultSet = statement.executeQuery (String.format ("SELECT * FROM %s WHERE %s = %s", tableName, keyName, keyValue));
            return createRows (resultSet);
        } finally {
            if (statement != null) {
                statement.close ();
            }
        }
    }

    List<Map<String, String>> getRows (String tableName) throws SQLException {
        connect ();
        SelectQuery query = new SelectQuery ()
                .select ("*")
                .from (tableName);
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultSet = statement.executeQuery (query.toString ());
            //ResultSet resultSet = statement.executeQuery (String.format ("SELECT * FROM %s", tableName));
            return createRows (resultSet);
        } finally {
            if (statement != null) {
                statement.close ();
            }
        }
    }

    private String getColumnNames (List<String> columnNames) {
        if (columnNames.isEmpty ()) {
            throw new ExecutionException ("no column names provided for query");
        }
        
        StringBuilder sb = new StringBuilder ();
        boolean isFirst  = true;
        for (String columnName : columnNames) {
            if (isFirst) {
                sb.append (columnName);
                isFirst = false;
            } else {
                sb.append (", ").append (columnName);
            }
        }
        return sb.toString ();
    }


    private Map<String, String> createRow (ResultSet resultSet, List<String> columnNames) throws SQLException {
        if (!resultSet.next ()) {
            throw new RuntimeException ("no record found");
        } else {
            return createOneRow (resultSet, columnNames);
        }
    }

    private Map<String, String> createRow (ResultSet resultSet) throws SQLException {
        if (!resultSet.next ()) {
            throw new RuntimeException ("no record found");
        } else {
            return createOneRow (resultSet);
        }
    }
    
    private List<Map<String, String>> createRows (ResultSet resultSet) throws SQLException {
        List<Map<String, String>> list = new LinkedList<Map<String, String>> ();
        while (resultSet.next ()) {
            list.add (createOneRow (resultSet));
        }
        return list;
    }

    private Map<String, String> createOneRow (ResultSet resultSet) throws SQLException {
        Map<String, String> row = new HashMap<String, String> ();
        int numColumns = resultSet.getMetaData ().getColumnCount ();
        for (int i = 1 ; i <= numColumns ; ++i) {
            row.put (resultSet.getMetaData ().getColumnName (i), resultSet.getString (i));
        }
        return row;
    }

    private Map<String, String> createOneRow (ResultSet resultSet, List<String> columnNames) throws SQLException {
        Map<String, String> row = new HashMap<String, String> ();
        for (String columnName : columnNames) {
            row.put (columnName, resultSet.getString (columnName));
        }
        return row;
    }

    private ExecutionException newSqlException (SQLException sqle) {
        return new ExecutionException ("SQL exception: " + sqle.getMessage (), sqle.getStackTrace ());
    }
}
