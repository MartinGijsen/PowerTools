/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

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
    private boolean mFirstConnect;


    public Database (String name, String connectString, String userName, String password) {
        mName          = name;
        mConnectString = connectString;
        mUserName      = userName;
        mPassword      = password;
        mConnection    = null;
        mFirstConnect  = true;
    }

    public String getName () {
        return mName;
    }
    
    private void connectOnce () {
        if (mFirstConnect) {
            try {
                if (mConnection == null) {
                    mConnection = DriverManager.getConnection (mConnectString, mUserName, mPassword);
                }
            } catch (SQLException sqle) {
                throw new ExecutionException ("failed to connect to " + mName + ": " + sqle.getMessage (), sqle.getStackTrace ());
            }
        } else if (mConnection == null) {
            throw new ExecutionException ("not connected to database " + mName);
        }
    }

    public void disconnect () {
        if (mConnection != null) {
            try {
                mConnection.close ();
                mConnection = null;
            } catch (SQLException sqle) {
                throw newSqlException (sqle);
            }
        }
    }
    
    
    public Map<String, String> getRow (String tableName, List<String> columnNames, String keyName, String keyValue) throws SQLException {
        connectOnce ();
        String query = String.format ("SELECT %s FROM %s WHERE %s = %s",
                                      getColumnNames (columnNames), tableName, keyName, keyValue);
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultset = statement.executeQuery (query);
            return createRow (resultset, columnNames);
        } finally {
            close (statement);
        }
    }

    public Map<String, String> getRow (String tableName, String keyName, String keyValue) throws SQLException {
        SelectQuery query = new SelectQuery ()
                .select ("*")
                .from (tableName)
                .where (column (keyName).equal (new Value (keyValue)));
        return getRow (query.toString ());
        //return getRow (String.format ("SELECT * FROM %s WHERE %s = %s", tableName, keyName, keyValue));
    }

    public Map<String, String> getRow (String query) throws SQLException {
        connectOnce ();
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultSet = statement.executeQuery (query);
            return createRow (resultSet);
        } finally {
            close (statement);
        }
    }

    public List<Map<String, String>> getRows (String tableName, String keyName, String keyValue) throws SQLException {
        connectOnce ();
        SelectQuery query = new SelectQuery ()
                .select ("*")
                .from (tableName)
                .where (column (keyName).equal (new Value (keyValue)));
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultSet = statement.executeQuery (query.toString ());
            return createRows (resultSet);
        } finally {
            close (statement);
        }
    }

    public List<Map<String, String>> getRows (String tableName) throws SQLException {
        connectOnce ();
        SelectQuery query = new SelectQuery ()
                .select ("*")
                .from (tableName);
        Statement statement = null;
        try {
            statement           = mConnection.createStatement ();
            ResultSet resultSet = statement.executeQuery (query.toString ());
            return createRows (resultSet);
        } finally {
            close (statement);
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

    private void close (Statement statement) {
        if (statement != null) {
            try {
                statement.close ();
            } catch (SQLException sqle) {
                // ignore
            }
        }
    }
    
    private ExecutionException newSqlException (SQLException sqle) {
        return new ExecutionException ("SQL exception: " + sqle.getMessage (), sqle.getStackTrace ());
    }
}
