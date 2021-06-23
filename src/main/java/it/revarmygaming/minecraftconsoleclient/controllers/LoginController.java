package it.revarmygaming.minecraftconsoleclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import it.revarmygaming.minecraftconsoleclient.Minecraft;
import it.revarmygaming.minecraftconsoleclient.MinecraftConsoleClient;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class LoginController {

    public JFXTextField host;
    public JFXTextField port;

    public JFXToggleButton premium;
    public JFXTextField username;
    public JFXPasswordField password;

    public JFXButton login;
    public JFXButton restore;

    public static Stage stage;

    private File cache;

    public void initialize(){
        cache = new File(MinecraftConsoleClient.getInstance().getDataFolder(), "cache.txt");
        if(!cache.exists()) {
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        port.setText("25565");
        port.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                login.setDisable(true);
            } else if(!newValue.matches("\\d*") || Integer.parseInt(newValue) > 65535){
                port.setText(oldValue);
                login.setDisable(false);
            } else {
                login.setDisable(false);
            }
        });
        premium.selectedProperty().addListener((observable, oldValue, newValue) -> password.setVisible(newValue));

        login.setOnMouseClicked(event -> {
            login.setDisable(true);
            try (PrintWriter writer = new PrintWriter(cache)){
                writer.println(host.getText());
                writer.println(port.getText());
                writer.println(premium.isSelected());
                writer.println(username.getText());
                writer.println(Base64.getEncoder().encodeToString(password.getText().getBytes(StandardCharsets.UTF_8)));
                writer.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Minecraft minecraft;
            if(premium.isSelected()) minecraft = new Minecraft(username.getText(), password.getText());
            else minecraft = new Minecraft(username.getText());

            stage.hide();

            ChatController.minecraft = minecraft;
            ChatController.host = host.getText();
            ChatController.port = Integer.parseInt(port.getText());

            try {
                showNextStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        restore.setOnMouseClicked(event -> {
            ArrayList<String> lines = new ArrayList<>();
            try(BufferedReader reader = new BufferedReader(new FileReader(cache))) {
                String line;
                while ((line = reader.readLine()) != null){
                    lines.add(line);
                }
                if(lines.size() >= 5){
                    host.setText(lines.get(0));
                    port.setText(lines.get(1));
                    premium.setSelected(Boolean.parseBoolean(lines.get(2)));
                    username.setText(lines.get(3));
                    password.setText(new String(Base64.getDecoder().decode(lines.get(4))));
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Previous Session");
                    alert.setHeaderText(null);
                    alert.setContentText("You haven't a previous session, just log in");

                    alert.showAndWait();
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Previous Session");
                alert.setHeaderText(null);
                alert.setContentText("You haven't a previous session, just log in");

                alert.showAndWait();
            }
        });
    }

    private void showNextStage() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(MinecraftConsoleClient.getInstance().getView("chat"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        ChatController chatController = loader.getController();

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
    }
}
