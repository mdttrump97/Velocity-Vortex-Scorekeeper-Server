package net.jacobmason.VelocityVortexScorekeeperServer;

import net.jacobmason.VelocityVortexScorekeeperServer.messages.ScoreMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by JacobAMason on 12/25/16.
 */
public class Scorekeeper {
    private Set<ScoreMessage> scores = new CopyOnWriteArraySet<>();

    public Scorekeeper() {
        clearScores();
    }

    public void updateScore(ScoreMessage score) {
        scores.remove(score);
        scores.add(score);
    }

    public void clearScores() {
        scores = Stream.of("red", "blue").flatMap(
                alliance -> Stream.of("autonomous", "teleop").flatMap(
                        period -> Stream.of("corner", "center").map(
                                goal -> new ScoreMessage(alliance, period, goal, 0)
                        )
                )
        ).collect(Collectors.toCollection(CopyOnWriteArraySet::new));
    }

    public List<ScoreMessage> getScoresAsSendableList() {
        return new ArrayList<>(scores);
    }
}
