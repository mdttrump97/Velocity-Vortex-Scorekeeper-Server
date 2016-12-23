package net.jacobmason.VelocityVortexScorekeeperServer;

/**
 * Created by JacobAMason on 12/22/16.
 */
public class ClockAudioMessage {
    private String audioFile;

    public ClockAudioMessage() {
    }

    public ClockAudioMessage(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getAudioFile() {
        return audioFile;
    }
}
