package net.jacobmason.VelocityVortexScorekeeperServer;

import net.jacobmason.VelocityVortexScorekeeperServer.messages.ScoreMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by JacobAMason on 12/22/16.
 */
@Controller
public class ScorekeeperController {
    private SimpMessagingTemplate messenger;
    private final Scorekeeper scorekeeper = new Scorekeeper();


    public ScorekeeperController(SimpMessagingTemplate messenger) {
        this.messenger = messenger;
    }

    @SubscribeMapping("/scores")
    public List<ScoreMessage> greet() {
        return scorekeeper.getScoresAsSendableList();
    }

    @MessageMapping("/scores")
    public void submitScores(List<ScoreMessage> scores) {
        for (ScoreMessage score : scores) {
            scorekeeper.updateScore(score);
        }
    }

    @MessageMapping("/scores/reset")
    @SendTo("/topic/scores")
    public List<ScoreMessage> reset() {
        scorekeeper.clearScores();
        return scorekeeper.getScoresAsSendableList();
    }
}
