package net.jacobmason.VelocityVortexScorekeeperServer;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by JacobAMason on 12/22/16.
 */
@Controller
public class ScorekeeperController {
    private SimpMessagingTemplate messenger;
    private Set<ScoreMessage> scores;

    public ScorekeeperController(SimpMessagingTemplate messenger) {
        this.messenger = messenger;
        initializeScores();
    }

    private void initializeScores() {
        scores = Stream.of("red", "blue").flatMap(
                alliance -> Stream.of("autonomous", "teleop").flatMap(
                        period -> Stream.of("corner", "center").map(
                                goal -> new ScoreMessage(alliance, period, goal, 0)
                        )
                )
        ).collect(Collectors.toCollection(CopyOnWriteArraySet::new));
    }

    @SubscribeMapping("/scores")
    public List<ScoreMessage> greet() {
        return new ArrayList<>(scores);
    }

    @MessageMapping("/scores/reset")
    @SendTo("/topic/scores")
    public List<ScoreMessage> reset() {
        initializeScores();
        return new ArrayList<>(scores);
    }

    @PostMapping(value = "/scorekeeper/submit", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResponse submitScore(@RequestBody ScoreMessage score) {
        messenger.convertAndSend("/topic/scores", Arrays.asList(score));
        scores.remove(score);
        scores.add(score);
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
