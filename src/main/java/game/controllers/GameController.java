package game.controllers;

import game.data.GameDao;
import game.models.Game;
import game.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    GameDao dao;

    public GameController(GameDao dao) {
        this.dao = dao;
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int create() {
        Game game = new Game();
        game = dao.addGame(game);
        return game.getId();
    }

    @PostMapping("/guess")
    public Round guess(@RequestBody Round guess) {
        // Set time of guess
        LocalDateTime currentTime = LocalDateTime.now();
        guess.setTime(currentTime);

        // Compute result of guess
        Game game = dao.findById(guess.getGameId());
        String result = computeGuessResult(guess.getGuess(), game.getAnswer());
        guess.setResult(result);

        // Add new round to db
        guess = dao.addRound(guess);

        // Check if game is finished
        if (guess.getGuess().equals(game.getAnswer())) {
            game.setFinished(true);
            dao.updateGame(game);
        }

        return guess;
    }

    @GetMapping("/game")
    List<Game> all() {
        List<Game> games = dao.getAll();

        for (Game game : games) {
            if ( !game.isFinished() ) {
                game.setAnswer("****");
            }
        }

        return games;
    }

    @GetMapping("/game/{gameId}")
    Game findById(@PathVariable int gameId) {
        Game game = dao.findById(gameId);
        if ( !game.isFinished() ) {
            game.setAnswer("****");
        }
        return game;
    }

    @GetMapping("/rounds/{gameId}")
    List<Round> allRoundsForGame(@PathVariable int gameId) {
        return dao.getAllRounds(gameId);
    }

    private String computeGuessResult(String guess, String answer) {
        int exactMatches = 0;
        int partialMatches = 0;

        for (int g = 0; g < guess.length(); g++) {
            for (int a = 0; a < answer.length(); a++) {
                if (guess.charAt(g) == answer.charAt(a)) {
                    if (g == a) {
                        exactMatches++;
                    }
                    else {
                        partialMatches++;
                    }
                    a = answer.length();
                }
            }
        }

        String result = "e:" + exactMatches + ":p:" + partialMatches;
        return result;
    }

}
