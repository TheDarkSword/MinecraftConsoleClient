package it.revarmygaming.minecraftconsoleclient.schedule;

import java.util.Timer;

public class Scheduler {

    private static Timer timer;

    static {
        timer = new Timer();
    }

    public static MinecraftTaskRepeating scheduleAsyncRepeatingTask(Runnable runnable, long delay, long period){
        MinecraftTaskRepeating timerTask = new MinecraftTaskRepeating(runnable);

        timer.schedule(timerTask, delay, period);
        return timerTask;
    }

    public static MinecraftTask runAsyncTask(Runnable runnable){
        MinecraftTask task = new MinecraftTask(runnable);

        task.start();

        return task;
    }
}
