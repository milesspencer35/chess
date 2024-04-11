package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GameData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO extends DAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM game";

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
    public int createGame(String gameName) throws DataAccessException {
        String sql = "insert into game (whiteUsername, blackUsername, gameName, game) values (?,?,?,?)";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, null);
                stmt.setString(2, null);
                stmt.setString(3, gameName);
                ChessGame chessGame = new ChessGame();
                String chessGameText = new Gson().toJson(chessGame);
                stmt.setString(4, chessGameText);

                if (stmt.executeUpdate() == 1) {
                    try(ResultSet generatedKey = stmt.getGeneratedKeys()){
                        generatedKey.next();
                        return generatedKey.getInt(1);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return 0;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> arrayOfGames = new ArrayList<>();
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    int gameID = rs.getInt(1);
                    String whiteUsername = rs.getString(2);
                    String blackUsername = rs.getString(3);
                    String gameName = rs.getString(4);
                    String gameText = rs.getString(5);
                    ChessGame game = new Gson().fromJson(gameText, ChessGame.class);
                    arrayOfGames.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }

        return arrayOfGames;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";

        Connection connection = null;
        try (Connection c = DatabaseManager.getConnection()) {
            connection = c;
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, gameID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) { // make sure this goes into if when there is a user
                    int rsGameID = rs.getInt(1);
                    String rsWhiteUsername = rs.getString(2);
                    String rsBlackUsername = rs.getString(3);
                    String rsGameName = rs.getString(4);
                    String chessGameText = rs.getString(5);
                    ChessGame chessGame = new Gson().fromJson(chessGameText, ChessGame.class);
                    return new GameData(rsGameID, rsWhiteUsername, rsBlackUsername, rsGameName, chessGame);
                } else {
                    return null;
                }

            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void joinGame(Integer gameID, ChessGame.TeamColor color, String username) throws DataAccessException {
        try {
            GameData game = getGame(gameID);

            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            if (color == ChessGame.TeamColor.BLACK) {
                blackUsername = username;
            } else if (color == ChessGame.TeamColor.WHITE) {
                whiteUsername = username;
            }

            String sql = "update game " +
                    "set whiteUsername = ?, blackUsername = ? " +
                    "where gameID = ?";

            Connection connection = null;
            try (Connection c = DatabaseManager.getConnection()) {
                connection = c;
                try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, whiteUsername);
                    stmt.setString(2, blackUsername);
                    stmt.setInt(3, gameID);
                    stmt.executeUpdate();
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage());
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void updateGame(Integer gameID, String whiteUsername, String blackUsername, ChessGame updatedGame) throws DataAccessException {
        try {
//            GameData game = getGame(gameID);

            String sql = "update game " +
                    "set whiteUsername = ?, blackUsername = ?, game = ? " +
                    "where gameID = ?";

            Connection connection = null;
            try (Connection c = DatabaseManager.getConnection()) {
                connection = c;
                try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, whiteUsername);
                    stmt.setString(2, blackUsername);
                    stmt.setString(3, new Gson().toJson(updatedGame, ChessGame.class));
                    stmt.setInt(4, gameID);
                    stmt.executeUpdate();
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage());
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
