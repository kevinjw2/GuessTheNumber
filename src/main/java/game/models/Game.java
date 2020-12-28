package game.models;

public class Game {

    private int id;
    private String answer;
    private boolean isFinished;

    public Game() {
        int numAnswer = (int)(Math.random() * 10000);
        this.answer = Integer.toString(numAnswer);
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

}
