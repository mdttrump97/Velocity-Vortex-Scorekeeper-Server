package net.jacobmason.VelocityVortexScorekeeperServer;

/**
 * Created by JacobAMason on 12/22/16.
 */
public class ClockTimeMessage {
    private int time;

    public ClockTimeMessage() {
    }

    public ClockTimeMessage(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
