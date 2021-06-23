package it.revarmygaming.minecraftconsoleclient.commands;

import it.revarmygaming.minecraftconsoleclient.Minecraft;
import it.revarmygaming.minecraftconsoleclient.entity.Player;
import it.revarmygaming.minecraftconsoleclient.message.MinecraftMessage;

public class MoveCommand implements CommandExecutor {

    @Override
    public void onCommand(Minecraft minecraft, String... args) {
        if(args.length == 0){
            minecraft.getLogger().write(new MinecraftMessage.Builder()
                    .setText("Invalid Command Arguments. Available: [forward, back, left, right, jump]").setColor("red").build());
            return;
        }

        Player player = minecraft.getPlayer();

        switch (args[0]){
            case "forward":
                minecraft.getSession().send(player.teleport(true, player.getX() + 1, player.getY(), player.getZ()));
                break;
            case "back":
                minecraft.getSession().send(player.teleport(true, player.getX() - 1, player.getY(), player.getZ()));
                break;
            case "left":
                break;
            case "right":
                break;
            case "jump":
                player.jump(minecraft);
                break;
        }

        minecraft.getLogger().write(new MinecraftMessage.Builder()
                .setText("Player moved " + args[0]).setColor("green").build());
    }
}
