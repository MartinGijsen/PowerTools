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
import org.powertools.engine.ExecutionException;


public final class Database {
    public enum JdbcClient {
        ORACLE_THIN_CLIENT ("jdbc:oracle:thin");
        
        private final String mText;
        
        JdbcClient (String text) {
            mText = text;
        }
        
        @Override
        public String toString () {
            return mText;
        }
    }
    
    
//    private final String mHostName;
//    private final JdbcClient mClient;
    private final String mDatabaseName;
    private final String mConnectString;
    private final String mUserName;
    private final String mPassword;

    private Connection mConnection;
    private boolean mFirstConnect;


    public Database (String hostName, JdbcClient client, String databaseName, String userName, String password) {
//        mHostName      = hostName;
//        mClient        = client;
        mDatabaseName  = databaseName;
        mConnectString = String.format ("%s:@//%s:1521/%s", client, hostName, mDatabaseName);
        mUserName      = userName;
        mPassword      = password;
        mFirstConnect  = true;
    }

    public Database (String connectString, String databaseName, String userName, String password) {
        mDatabaseName  = databaseName;
        mConnectString = connectString;
        mUserName      = userName;
        mPassword      = password;
        mFirstConnect  = true;
    }

    public String getName () {
        return mDatabaseName;
    }

    private void connectOnce () {
        if (mFirstConnect) {
            try {
                mFirstConnect        = false;
                //String connectString = String.format ("%s:@//%s:1521/%s", mClient, mHostName, mDatabaseName);
                mConnection          = DriverManager.getConnection (mConnectString, mUserName, mPassword);
            } catch (SQLException sqle) {
                throw newSqlException (sqle);
            }
        } else if (mConnection == null) {
            throw new ExecutionException ("not connected to database " + getName ());
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

    private Map<String, String> createRow (ResultSet resultSet) throws SQLException {
        if (!resultSet.next ()) {
            throw new RuntimeException ("no record found");
        } else {
            return createOneRow (resultSet);
        }
    }
    
    private List<Map<String, String>> createRows (ResultSet resultSet) throws SQLException {
        List<Map<String, String>> rows = new LinkedList<Map<String, String>> ();
        while (resultSet.next ()) {
            rows.add (createOneRow (resultSet));
        }
        return rows;
    }

    private Map<String, String> createOneRow (ResultSet resultSet) throws SQLException {
        Map<String, String> row = new HashMap<String, String> ();
        int numColumns = resultSet.getMetaData ().getColumnCount ();
        for (int i = 1 ; i <= numColumns ; ++i) {
            row.put (resultSet.getMetaData ().getColumnName (i), resultSet.getString (i));
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
