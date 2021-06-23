package it.revarmygaming.minecraftconsoleclient.commands;

import it.revarmygaming.minecraftconsoleclient.Minecraft;
import it.revarmygaming.minecraftconsoleclient.entity.Player;
import it.revarmygaming.minecraftconsoleclient.message.MinecraftMessage;
import it.revarmygaming.minecraftconsoleclient.schedule.MinecraftTaskRepeating;
import it.revarmygaming.minecraftconsoleclient.schedule.Scheduler;

public class AfkCommand implements CommandExecutor {

    private static MinecraftTaskRepeating afkTask;

    @Override
    public void onCommand(Minecraft minecraft, String... args) {
        if(afkTask == null || afkTask.isCancelled()){
            //afkTask = Scheduler.scheduleAsyncRepeatingTask(() -> minecraft.getSession().send(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND)), 0, 10000);
            afkTask = Scheduler.scheduleAsyncRepeatingTask(() -> {
                Player player = minecraft.getPlayer();
                player.jump(minecraft);
            }, 0, 10000);
            minecraft.getLogger().write(new MinecraftMessage.Builder()
                    .setText("AFKTask Started")
                    .setColor("green").build());
        } else {
            afkTask.cancel();
            minecraft.getLogger().write(new MinecraftMessage.Builder()
                    .setText("AFKTask Cancelled")
                    .setColor("red").build());
        }
    }
}
