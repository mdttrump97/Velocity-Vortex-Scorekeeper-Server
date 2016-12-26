package net.jacobmason.VelocityVortexScorekeeperServer.messages;

/**
 * Created by JacobAMason on 12/21/16.
 */
public class ClockControlMessage {
    private String control;

    public ClockControlMessage() {
    }

    public ClockControlMessage(String control) {
        this.control = control;
    }

    public String getControl() {
        return control;
    }

    @Override
    public String toString() {
        return "ClockControlMessage{" +
                "control='" + control + '\'' +
                '}';
    }
}
