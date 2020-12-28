package game.data;

import game.models.Game;
import game.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.List;

@Repository
public class GameDaoDB implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoDB (JdbcTemplate jdbc) { this.jdbcTemplate = jdbc; }

    @Override
    public Game addGame(Game game) {
        final String sql = "INSERT INTO game(answer, isFinished) VALUES (?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement stmt = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, game.getAnswer());
            stmt.setBoolean(2, game.isFinished());
            return stmt;
        }, keyHolder);

        game.setId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public Round addRound(Round round) {
        final String sql = "INSERT INTO round(gameId, guess, time, result) " +
                "VALUES (?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement stmt = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, round.getGameId());
            stmt.setString(2, round.getGuess());
            stmt.setTimestamp(3, Timestamp.valueOf(round.getTime()));
            stmt.setString(4, round.getResult());
            return stmt;
        }, keyHolder);

        round.setId(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public boolean updateGame(Game game) {

        final String sql = "UPDATE game SET " +
                "isFinished = ? " +
                "WHERE gameId = ?;";

        return jdbcTemplate.update(sql, game.isFinished(), game.getId()) > 0;
    }

    @Override
    public List<Game> getAll() {
        final String sql = "SELECT * FROM game;";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game findById(int gameId) {
        final String sql = "SELECT * FROM game WHERE gameId = ?;";
        return jdbcTemplate.queryForObject(sql, new GameMapper(), gameId);
    }

    @Override
    public List<Round> getAllRounds(int gameId) {
        final String sql = "SELECT * FROM round WHERE gameId = ?;";
        return jdbcTemplate.query(sql, new RoundMapper(), gameId);
    }

    @Override
    public boolean deleteGameById(int gameId) {
        final String sql = "DELETE FROM game WHERE gameId = ?;";

        return jdbcTemplate.update(sql, gameId) > 0;
    }

    @Override
    public boolean deleteRoundById(int roundId) {
        final String sql = "DELETE FROM round WHERE roundId = ?;";
        return jdbcTemplate.update(sql, roundId) > 0;
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setId(rs.getInt("gameId"));
            game.setAnswer(rs.getString("answer"));
            game.setFinished(rs.getBoolean("isFinished"));
            return game;
        }
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setId(rs.getInt("roundId"));
            round.setGameId(rs.getInt("gameId"));
            round.setGuess(rs.getString("guess"));
            round.setTime(rs.getTimestamp("time").toLocalDateTime());
            round.setResult(rs.getString("result"));
            return round;
        }
    }
}
