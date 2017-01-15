package net.jacobmason.VelocityVortexScorekeeperServer;

import net.jacobmason.VelocityVortexScorekeeperServer.messages.Match;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by JacobAMason on 1/14/17.
 */
@Controller
public class MatchContoller {
    private SimpMessagingTemplate messenger;
    private List<Match> matches;
    private Match currentMatch;

    public MatchContoller(SimpMessagingTemplate messenger) {
        this.messenger = messenger;

        try {
            matches = Match.readMatchFile("matches.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Can't find matches.txt\nPut it in the same folder as this jar.");
        } catch (IOException e) {
            System.out.println("Some weird line reading error from matches.txt");
        }

        currentMatch = matches.get(0);
    }

    @SubscribeMapping("/matches")
    public List<Match> matches_greeter() {
        return matches;
    }

    @SubscribeMapping("/matches/currentMatch")
    public Match currentMatch_greeter() {
        return currentMatch;
    }

    @MessageMapping("/matches/currentMatch")
    public void setCurrentMatch(Match currentMatch) {
        this.currentMatch = currentMatch;
    }
}
