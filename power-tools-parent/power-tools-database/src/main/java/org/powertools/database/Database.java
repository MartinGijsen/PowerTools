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
import java.sql.SQLRecoverableException;
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
    
    
    private final String _databaseName;
    private final String _connectString;
    private final String _userName;
    private final String _password;

    private Connection _connection;
    private boolean    _isFirstConnect;


    public Database (String hostName, JdbcClient client, String databaseName, String userName, String password) {
        this (String.format ("%s:@//%s:1521/%s", client, hostName, databaseName), databaseName, userName, password);
    }

    public Database (String connectString, String databaseName, String userName, String password) {
        _databaseName    = databaseName;
        _connectString   = connectString;
        _userName        = userName;
        _password        = password;
        _isFirstConnect  = true;
    }

    public String getName () {
        return _databaseName;
    }
    
    private Connection getConnection () {
        if (_isFirstConnect) {
            openConnection ();
            _isFirstConnect = false;
        } else if (_connection == null) {
            throw new ExecutionException ("not connected to database '%s'", _databaseName);
        }
        return _connection;
    }

    private Connection reconnect () {
        disconnect ();
        openConnection ();
        return _connection;
    }
    
    private void openConnection () {
        try {
            _connection = DriverManager.getConnection (_connectString, _userName, _password);
        } catch (SQLException sqle) {
            throw new ExecutionException ("failed to connect to " + _databaseName + ": " + sqle.getMessage (), sqle.getStackTrace ());
        }
    }

    public void disconnect () {
        if (_connection != null) {
            try {
                _connection.close ();
                _connection = null;
            } catch (SQLException sqle) {
                throw new ExecutionException ("SQL exception: " + sqle.getMessage (), sqle.getStackTrace ());
            }
        }
    }
    
    
//    public Map<String, String> getRow (String tableName, List<String> columnNames, String keyName, String keyValue) throws SQLException {
//        SelectQuery query = new SelectQuery ()
//                .from (tableName)
//                .where (Query.column (keyName).equal (new Value (keyValue)));
//        for (String columnName : columnNames) {
//            query.select (columnName);
//        }
//        return getRow (query.toString ());
//    }
//
//    public Map<String, String> getRow (String tableName, String keyName, String keyValue) throws SQLException {
//        SelectQuery query = new SelectQuery ()
//                .select ("*")
//                .from (tableName)
//                .where (Query.column (keyName).equal (new Value (keyValue)));
//        return getRow (query.toString ());
//    }

    public Map<String, String> getRow (String query) throws SQLException {
        Statement statement = null;
        Map<String, String> record;
        try {
            try {
                statement = getConnection ().createStatement ();
                record    = createRow (statement.executeQuery (query));
            } catch (SQLRecoverableException sqlre) {
                statement = reconnect ().createStatement ();
                record    = createRow (statement.executeQuery (query));
            }
        } catch (SQLException sqle) {
            if (sqle.getMessage ().contains ("no record found")) {
                throw new ExecutionException ("no record found");
            } else {
                throw sqle;
            }
        } finally {
            close (statement);
        }
        return record;
    }

//    public List<Map<String, String>> getRows (String tableName, String keyName, String keyValue) throws SQLException {
//        SelectQuery query = new SelectQuery ()
//                .select ("*")
//                .from (tableName)
//                .where (Query.column (keyName).equal (new Value (keyValue)));
//        return getRows (query.toString ());
//    }

    public List<Map<String, String>> getRows (String query) throws SQLException {
        try (Statement statement = getConnection ().createStatement ()) {
            ResultSet resultSet = statement.executeQuery (query);
            return createRows (resultSet);
        }
    }

//    private Map<String, String> createRow (ResultSet resultSet, List<String> columnNames) throws SQLException {
//        if (!resultSet.next ()) {
//            throw new RuntimeException ("no record found");
//        } else {
//            Map<String, String> row = createOneRow (resultSet, columnNames);
//            if (resultSet.next()) {
//                throw new RuntimeException ("more than one record found");
//            } else {
//                return row;
//            }
//        }
//    }

    private Map<String, String> createRow (ResultSet resultSet) throws SQLException {
        if (!resultSet.next ()) {
            throw new RuntimeException ("no record found");
        } else {
            Map<String, String> row = createOneRow (resultSet);
            if (resultSet.next()) {
                throw new RuntimeException ("more than one record found");
            } else {
                return row;
            }
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

    private Map<String, String> createOneRow (ResultSet resultSet, List<String> columnNames) throws SQLException {
        Map<String, String> row = new HashMap<String, String> ();
        for (String columnName : columnNames) {
            row.put (columnName, resultSet.getString (columnName));
        }
        return row;
    }

    public void executeStatement (String statement) throws SQLException {
        Statement stat = null;
        try {
            stat = getConnection ().createStatement ();
            stat.execute (statement);
        } finally {
            close (stat);
        }
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
}
