package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends DAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException {
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM user";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var sql = "SELECT username, password, email FROM user WHERE username = ?";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) { // make sure this goes into if when there is a user
                    String rsUsername = rs.getString(1);
                    String rsPassword = rs.getString(2);
                    String rsEmail = rs.getString(3);
                    return new UserData(rsUsername, rsPassword, rsEmail);
                } else {
                    return null;
                }

            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        String sql = "insert into user (username, password, email) values (? ,? ,?) ";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public UserData verifyUser(String username, String password) throws DataAccessException {
        return null;
    }
}
