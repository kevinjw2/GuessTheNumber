package game.data;

import game.models.Game;
import game.models.Round;

import java.util.List;

public interface GameDao {
    Game addGame(Game game);

    List<Game> getAll();

    Game findById(int gameId);

    boolean updateGame(Game game);

    boolean deleteGameById(int gameId);

    Round addRound(Round round);

    List<Round> getAllRounds(int gameId);

    boolean deleteRoundById(int roundId);
}
