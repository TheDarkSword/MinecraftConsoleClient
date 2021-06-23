package it.revarmygaming.minecraftconsoleclient.tasks;

import it.revarmygaming.minecraftconsoleclient.Minecraft;
import it.revarmygaming.minecraftconsoleclient.logger.Logger;
import javafx.application.Platform;
import javafx.scene.control.TextField;

public class MinecraftThread implements Runnable {

    private Minecraft minecraft;
    private String host;
    private int port;
    private Logger logger;
    private TextField input;

    public MinecraftThread(Minecraft minecraft, String host, int port, Logger logger, TextField input){
        this.minecraft = minecraft;
        this.host = host;
        this.port = port;
        this.logger = logger;
        this.input = input;
    }

    public MinecraftThread(Minecraft minecraft, String host, int port, Logger logger){
        this(minecraft, host, port, logger, null);
    }

    @Override
    public void run() {
        minecraft.ping(host, port);
        minecraft.login(host, port, logger, input);
    }
}
