package game.models;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private int id;
    private String answer;
    private boolean isFinished;

    public Game() {
        this.answer = generateAnswer();
        this.isFinished = false;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String generateAnswer() {
        String answer = "";

        // Create a list of digits to draw from
        List<Integer> digits = new ArrayList<Integer>();
        for (int i = 0; i <= 9; i++) {
            digits.add(i);
        }

        // Draw 4 digits at random, removing each to avoid repeats
        for (int i = 0; i < 4; i++) {
            int index = (int)( Math.random() * digits.size() );
            answer += Integer.toString(digits.get(index));
            digits.remove(index);
        }

        return answer;
    }

}
