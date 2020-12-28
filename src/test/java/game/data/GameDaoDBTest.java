package game.data;

import game.TestApplicationConfiguration;
import game.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoDBTest {
    @Autowired
    GameDao dao;

    @Before
    public void setUp() throws Exception {
        List<Game> games = dao.getAll();
        for (Game game : games) {
            List<Round> rounds = dao.getAllRounds(game.getId());
            for (Round round : rounds) {
                dao.deleteRoundById(round.getId());
            }

            dao.deleteGameById(game.getId());
        }
    }

    @Test
    public void testAddGetGame() {
        Game game = new Game();
        int gameId = dao.addGame(game).getId();
        game.setId(gameId);

        List<Game> games = dao.getAll();
        assertEquals(1, games.size());

        Game fromDao = games.get(0);
        assertEquals(game.getAnswer(), fromDao.getAnswer());
        assertEquals(game.isFinished(), fromDao.isFinished());

        fromDao = dao.findById(game.getId());
        assertEquals(game.getAnswer(), fromDao.getAnswer());
        assertEquals(game.isFinished(), fromDao.isFinished());
    }

    @Test
    public void testUpdateGame() {
        Game game = new Game();
        int gameId = dao.addGame(game).getId();
        game.setId(gameId);

        game.setFinished(true);
        dao.updateGame(game);

        Game fromDao = dao.findById(gameId);
        assertEquals(true, fromDao.isFinished());
    }

    @Test
    public void testAddGetRounds() {
        Game game = new Game();
        game.setAnswer("1234");
        int gameId = dao.addGame(game).getId();
        game.setId(gameId);

        Round r1 = new Round();
        r1.setGameId(gameId);
        r1.setTime(LocalDateTime.now());
        r1.setGuess("1432");
        r1.setResult("e:2:p:2");
        int r1Id = dao.addRound(r1).getId();
        r1.setId(r1Id);

        List<Round> roundList = dao.getAllRounds(gameId);
        assertEquals(1, roundList.size());
        Round fromDao = roundList.get(0);
        assertEquals("1432", fromDao.getGuess());
        assertEquals("e:2:p:2", fromDao.getResult());
        // Some precision is lost when a Timestamp is sent to the database
        //assertEquals(r1.getTime(), fromDao.getTime());
    }
}