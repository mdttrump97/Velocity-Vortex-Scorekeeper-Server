package net.jacobmason.VelocityVortexScorekeeperServer.messages;

/**
 * Created by JacobAMason on 12/21/16.
 */
public class ScoreMessage {
    private String alliance;
    private String gameMode;
    private String goal;
    private int score;

    public ScoreMessage() {
    }

    public ScoreMessage(String alliance, String gameMode, String goal, int score) {
        this.alliance = alliance;
        this.gameMode = gameMode;
        this.goal = goal;
        this.score = score;
    }

    public ScoreMessage(ScoreMessage that) {
        this.alliance = that.alliance;
        this.gameMode = that.gameMode;
        this.goal = that.goal;
        this.score = that.score;
    }

    public String getAlliance() {
        return alliance;
    }

    public String getGameMode() {
        return gameMode;
    }

    public String getGoal() {
        return goal;
    }

    public int getScore() {
        return score;
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreMessage that = (ScoreMessage) o;

        if (getAlliance() != null ? !getAlliance().equals(that.getAlliance()) : that.getAlliance() != null)
            return false;
        if (getGameMode() != null ? !getGameMode().equals(that.getGameMode()) : that.getGameMode() != null) return false;
        return getGoal() != null ? getGoal().equals(that.getGoal()) : that.getGoal() == null;
    }

    @Override
    public String toString() {
        return "ScoreMessage{" +
                "alliance='" + alliance + '\'' +
                ", gameMode='" + gameMode + '\'' +
                ", goal='" + goal + '\'' +
                ", score=" + score +
                '}';
    }
}
