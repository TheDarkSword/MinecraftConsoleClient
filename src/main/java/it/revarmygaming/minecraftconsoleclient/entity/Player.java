package it.revarmygaming.minecraftconsoleclient.entity;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import it.revarmygaming.minecraftconsoleclient.Minecraft;
import it.revarmygaming.minecraftconsoleclient.schedule.MinecraftTask;
import it.revarmygaming.minecraftconsoleclient.schedule.Scheduler;

public class Player {

    private int entityId;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    private MinecraftTask jumpTask;

    public Player(int entityId, int x, int y, int z, int yaw, int pitch){
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Player(int entityId){
        this(entityId, 0, 0, 0, 0, 0);
    }

    public ClientPlayerPositionRotationPacket teleport(boolean onGround, double x, double y, double z, float yaw, float pitch){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;

        return new ClientPlayerPositionRotationPacket(onGround, x, y, z, yaw, pitch);
    }

    public ClientPlayerPositionRotationPacket teleport(boolean onGround, double x, double y, double z){
        return teleport(onGround, x, y, z, yaw, pitch);
    }

    public void jump(Minecraft minecraft){
        jumpTask = Scheduler.runAsyncTask(() -> {
            double startY = getY();
            try{
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 0.82f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 1.06f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 1.22f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 1.31f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 1.23f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 1.08f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 0.86f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 0.55f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(false, getX(), startY + 0.18f, getZ()));
                Thread.sleep(50);
                minecraft.getSession().send(new ClientPlayerPositionPacket(true, getX(), startY, getZ()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public int getEntityId() {
        return entityId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setLocation(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setLocation(double x, double y, double z, float yaw, float pitch){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
