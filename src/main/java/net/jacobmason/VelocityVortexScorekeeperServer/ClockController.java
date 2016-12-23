package net.jacobmason.VelocityVortexScorekeeperServer;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by JacobAMason on 12/21/16.
 */
@Controller
@MessageMapping("/clock")
public class ClockController {
    private SimpMessagingTemplate messenger;
    private final Clock clock = new Clock(this);

    public ClockController(SimpMessagingTemplate messenger) {
        this.messenger = messenger;
    }

    public void sendTime(int time) {
        messenger.convertAndSend("/topic/clock/time", new ClockTimeMessage(time));
    }

    public void sendAudio(String audio) {
        messenger.convertAndSend("/topic/clock/audio", new ClockAudioMessage(audio));
    }

    public void sendControl(String control) {
        messenger.convertAndSend("/topic/clock/control", new ClockControlMessage(control));
    }

    @MessageMapping("/control")
    public void handleControlMessage(ClockControlMessage message) throws Exception {
        switch (message.getControl()) {
            case "start-autonomous":
                clock.runAutonomous();
                break;
            case "start-teleop":
                clock.runTeleop();
                break;
            case "reset-clock":
                clock.reset();
                break;
            case "stop-clock":
                clock.eStop();
                break;
            default:
                break;
        }
    }

}
