package game.models;

import org.apache.tomcat.jni.Local;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Round {

    private int id;
    private int gameId;
    private String guess;
    private LocalDateTime time;
    private String result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(Game game) {
        this.gameId = game.getId();
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
