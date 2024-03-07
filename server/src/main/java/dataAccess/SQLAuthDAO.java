package dataAccess;

import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO extends DAO implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auth";

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
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);

        String sql = "insert into auth (username, authToken) values (? ,?) ";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, authToken);
                stmt.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }

        return newAuth;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var sql = "SELECT username, authToken FROM auth WHERE authToken = ?";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, authToken);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String rsUsername = rs.getString(1);
                    String rsAuthToken = rs.getString(2);
                    return new AuthData(rsUsername, rsAuthToken);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
