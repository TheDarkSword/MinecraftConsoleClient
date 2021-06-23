package it.revarmygaming.minecraftconsoleclient;

import it.revarmygaming.minecraftconsoleclient.commands.AfkCommand;
import it.revarmygaming.minecraftconsoleclient.commands.CommandExecutor;
import it.revarmygaming.minecraftconsoleclient.commands.HelpCommand;
import it.revarmygaming.minecraftconsoleclient.commands.MoveCommand;
import it.revarmygaming.minecraftconsoleclient.controllers.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class MinecraftConsoleClient extends Application {

    private static MinecraftConsoleClient instance;

    private File dataFolder;
    private HashMap<String, CommandExecutor> commands;

    public static void main(String... args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        dataFolder = new File(System.getProperty("user.dir") + File.separator + "MinecraftConsoleClient");
        if(!dataFolder.exists()) dataFolder.mkdir();

        registerCommands();

        BorderPane root = new BorderPane();
        FXMLLoader loader = new FXMLLoader(getView("login"));
        Parent parent = loader.load();

        Scene scene = new Scene(root, 407, 502);

        LoginController.stage = stage;
        LoginController loginController = loader.getController();

        stage.setTitle("Minecraft Console Client");
        //stage.getIcons().add(new Image(getResourceAsStream("images/key.png")));
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        stage.show();

        root.setCenter(parent);
    }

    public InputStream getResourceAsStream(String path) {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    public URL getResource(String path) {
        return this.getClass().getClassLoader().getResource(path);
    }

    public URL getView(String name) {
        return this.getResource("views/" + name + ".fxml");
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public static MinecraftConsoleClient getInstance(){
        return instance;
    }

    public CommandExecutor getCommand(String command){
        return commands.get(command);
    }

    public Set<String> getCommands() {
        return commands.keySet();
    }

    private void registerCommands(){
        commands = new HashMap<>();

        commands.put("./help", new HelpCommand());
        commands.put("./afk", new AfkCommand());
        commands.put("./move", new MoveCommand());
    }
}
