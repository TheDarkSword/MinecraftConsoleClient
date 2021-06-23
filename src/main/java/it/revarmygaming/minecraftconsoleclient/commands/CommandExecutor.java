package it.revarmygaming.minecraftconsoleclient.commands;

import it.revarmygaming.minecraftconsoleclient.Minecraft;

public interface CommandExecutor {

    void onCommand(Minecraft minecraft, String... args);
}
