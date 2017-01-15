package net.jacobmason.VelocityVortexScorekeeperServer;

import java.util.concurrent.*;

/**
 * Created by JacobAMason on 12/22/16.
 */
public class Clock {
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture service;
    private ClockController clockController;

    public Clock(ClockController clockController) {

        this.clockController = clockController;
    }

    public void runAutonomous() {
        clockController.sendAudio("start-autonomous");
        clockController.sendTime(150);
        service = executorService.scheduleAtFixedRate(new Runnable() {
            int time = 150;
            @Override
            public void run() {
                clockController.sendTime(--time);
                if (time == 140) {
                    clockController.sendAudio("time-running-out");
                } else if (time <= 120) {
                    clockController.sendAudio("end-autonomous");
                    clockController.sendControl("end-autonomous");
                    service.cancel(true);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void runTeleop() {
        clockController.sendAudio("start-teleop");
        clockController.sendTime(120);
        service = executorService.scheduleAtFixedRate(new Runnable() {
            int time = 120;
            @Override
            public void run() {
                clockController.sendTime(--time);
                if (time == 30) {
                    clockController.sendAudio("time-running-out");
                } else if (time <= 0) {
                    clockController.sendAudio("end-teleop");
                    clockController.sendControl("end-teleop");
                    service.cancel(true);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void reset() {
        if (service == null || service.isDone()) {
            clockController.sendTime(150);
        }
    }

    public void eStop() {
        if (service != null) {
            service.cancel(true);
        }
        clockController.sendAudio("e-stop");
    }
}
