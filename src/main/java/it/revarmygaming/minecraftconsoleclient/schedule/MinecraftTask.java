package it.revarmygaming.minecraftconsoleclient.schedule;

public class MinecraftTask extends Thread {

    private Runnable runnable;

    public MinecraftTask(Runnable runnable){
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();
    }
}
