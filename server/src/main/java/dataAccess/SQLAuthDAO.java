package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
