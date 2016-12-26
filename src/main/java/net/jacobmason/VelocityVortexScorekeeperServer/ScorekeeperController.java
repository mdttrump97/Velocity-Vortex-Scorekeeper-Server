package net.jacobmason.VelocityVortexScorekeeperServer;

import net.jacobmason.VelocityVortexScorekeeperServer.messages.ScoreMessage;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
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

    @MessageMapping("/scores/reset")
    @SendTo("/topic/scores")
    public List<ScoreMessage> reset() {
        scorekeeper.clearScores();
        return scorekeeper.getScoresAsSendableList();
    }

    @PostMapping(value = "/scorekeeper/submit", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResponse submitScore(@RequestBody ScoreMessage score) {
        messenger.convertAndSend("/topic/scores", Arrays.asList(score));
        scorekeeper.updateScore(score);
        return new JsonResponse();
    }

    private class JsonResponse {
        String status = "ok";

        public JsonResponse() {
        }

        public JsonResponse(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
