package it.revarmygaming.minecraftconsoleclient.schedule;

import java.util.TimerTask;

public class MinecraftTaskRepeating extends TimerTask {

    private Runnable runnable;
    private boolean cancelled;

    public MinecraftTaskRepeating(Runnable runnable){
        this.runnable = runnable;
        this.cancelled = false;
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public boolean cancel() {
        cancelled = true;
        return super.cancel();
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
