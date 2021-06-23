package it.revarmygaming.minecraftconsoleclient.commands;

import it.revarmygaming.minecraftconsoleclient.Minecraft;
import it.revarmygaming.minecraftconsoleclient.MinecraftConsoleClient;
import it.revarmygaming.minecraftconsoleclient.message.MinecraftMessage;

public class HelpCommand implements CommandExecutor {

    @Override
    public void onCommand(Minecraft minecraft, String... args) {
        StringBuilder builder = new StringBuilder();
        String[] commands = MinecraftConsoleClient.getInstance().getCommands().toArray(new String[0]);
        for(int i = 0; i < commands.length; i++){
            builder.append(commands[i]);
            if(i != commands.length-1) builder.append(", ");
        }

        minecraft.getLogger().write(new MinecraftMessage.Builder()
                .setText(builder.toString())
                .setColor("green")
                .build());
    }
}
