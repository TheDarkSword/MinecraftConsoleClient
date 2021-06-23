package it.revarmygaming.minecraftconsoleclient.controllers;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import it.revarmygaming.minecraftconsoleclient.Minecraft;
import it.revarmygaming.minecraftconsoleclient.MinecraftConsoleClient;
import it.revarmygaming.minecraftconsoleclient.commands.CommandExecutor;
import it.revarmygaming.minecraftconsoleclient.logger.Logger;
import it.revarmygaming.minecraftconsoleclient.tasks.MinecraftThread;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;

import java.util.Arrays;
import java.util.LinkedList;

public class ChatController {

    public WebView chat;
    public TextField input;

    public static Minecraft minecraft;
    public static String host;
    public static int port;

    private final LinkedList<String> cacheMessage = new LinkedList<>();
    private int cachePointer = 0;

    private int transactionId = 1;

    public void initialize(){
        chat.setFocusTraversable(false);
        input.setFocusTraversable(false);
        input.setDisable(true);
        new Thread(new MinecraftThread(minecraft, host, port, new Logger(chat), input)).start();
        input.setOnKeyReleased(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                if(cacheMessage.size() == 20){
                    cacheMessage.removeLast();
                }
                cacheMessage.addFirst(input.getText());

                if(isCommand(input.getText())){
                    String[] parts = input.getText().split(" ");
                    String command = parts[0];
                    String[] args;
                    if(parts.length > 1){
                        args = Arrays.copyOfRange(parts, 1, parts.length);
                    } else {
                        args = new String[0];
                    }
                    executeCommand(command, args);
                } else {
                    minecraft.sendChatMessage(input.getText());
                }
                input.setText("");
            } else if(event.getCode().equals(KeyCode.UP) && cachePointer < cacheMessage.size()){
                input.setText(cacheMessage.get(cachePointer++));
            } else if(event.getCode().equals(KeyCode.DOWN) && cachePointer-1 >= 0 && !cacheMessage.isEmpty()){
                input.setText(cacheMessage.get(--cachePointer));
            }
        });
        input.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.TAB)) {
                if (input.getText().startsWith("/")) {
                    minecraft.getSession().send(new ClientTabCompletePacket(transactionId++, input.getText().substring(1)));
                }
            }
        });
    }

    private boolean isCommand(String command){
        return command.startsWith("./");
    }

    private void executeCommand(String command, String... args){
        CommandExecutor executor = MinecraftConsoleClient.getInstance().getCommand(command);
        if(executor == null){
            minecraft.getLogger().write("{text:\"Invalid Command. Type ./help to show all commands.\", color:\"red\"}");
        } else {
            executor.onCommand(minecraft, args);
        }
    }
}
