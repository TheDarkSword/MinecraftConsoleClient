package it.revarmygaming.minecraftconsoleclient.logger;

import com.google.gson.*;
import it.revarmygaming.minecraftconsoleclient.message.MinecraftMessage;
import it.revarmygaming.minecraftconsoleclient.parser.ChatColor;
import javafx.application.Platform;
import javafx.scene.web.WebView;


public class Logger {

    /*private final static Pattern colorPattern = Pattern.compile("(?:&0([^&]+))?(?:&1([^&]+))?(?:&2([^&]+))?(?:&3([^&]+))?(?:&4([^&]+))?(?:&5([^&]+))?" +
            "(?:&6([^&]+))?(?:&7([^&]+))?(?:&8([^&]+))?(?:&9([^&]+))?(?:&a([^&]+))?(?:&b([^&]+))?(?:&c([^&]+))?" +
            "(?:&d([^&]+))?(?:&e([^&]+))?(?:&f([^&]+))?");
    private final static Pattern invalidCodes = Pattern.compile("(&k)|(&l)|(&m)|(&n)|(&o)|(&r)");*/
    private static final String TEMPLATE = "<span style='color: $color; font-weight: $weight;'>$text</span>";

    private WebView chat;
    private StringBuilder content;
    private String bottomFunction;

    public Logger(WebView chat){
        this.chat = chat;
        content = new StringBuilder("<html><body onload='toBottom()' style='background-color: #293133;'>");
        bottomFunction = "<script language=\"javascript\" type=\"text/javascript\">" +
                "function toBottom(){" +
                "window.scrollTo(0, document.body.scrollHeight);" +
                "}</script>";
    }

    public void write(String message){
        try {
            content.append(parseJson((JsonObject) JsonParser.parseString(message)));
        } catch (ClassCastException ignored){
            content.append(TEMPLATE.replace("$color", ChatColor.WHITE.toString())
                    .replace("$weight", "normal")
                    .replace("$text", message)).append("<br>");
        }
        Platform.runLater(() -> chat.getEngine().loadContent(content.toString() + bottomFunction + "</body></html>", "text/html"));
    }

    public void write(MinecraftMessage message){
        write(message.getMessage());
    }

    private static String parseJson(JsonObject json){
        StringBuilder builder = new StringBuilder();
        if(json.get("text").getAsString().isEmpty() && json.get("extra") != null) builder.append(parseJson(json.get("extra").getAsJsonArray()));
        else {
            return builder.append(TEMPLATE.replace("$color", ChatColor.getColorByName(json.get("color").getAsString()))
                    .replace("$weight", json.get("bold") == null ? "normal" : "bold")
                    .replace("$text", json.get("text").getAsString().replace("\n", "<br>"))
                    .replace("   ", " &nbsp; ")).append("<br>").toString();
        }
        return builder.append("<br>").toString();
    }

    private static String parseJson(JsonArray array){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < array.size(); i++){
            JsonElement jsonElement = array.get(i);
            //System.out.println("i- " + i + ": " + jsonElement);
            if(jsonElement instanceof JsonPrimitive){
                builder.append(jsonElement.getAsString().replace("\n", "<br>").replace("   ", " &nbsp; "));
            } else if(jsonElement instanceof JsonObject){
                JsonObject json = jsonElement.getAsJsonObject();
                if(json.get("text").getAsString().isEmpty() && json.get("extra") != null) builder.append(parseJson(json.get("extra").getAsJsonArray()));
                else builder.append(TEMPLATE.replace("$color", json.get("color") == null ? "" : ChatColor.getColorByName(json.get("color").getAsString()))
                        .replace("$weight", json.get("bold") == null ? "normal" : "bold")
                        .replace("$text", json.get("text").getAsString().replace("\n", "<br>"))
                        .replace("   ", " &nbsp; "));
            }
        }
        return builder.toString();
    }

    /*private String parseJson(String text){
        System.out.println(text);
        JsonObject message = (JsonObject) JsonParser.parseString(text);
        StringBuilder builder = new StringBuilder();
        if(message.get("text").getAsString().isEmpty()){
            JsonArray array = message.get("extra").getAsJsonArray();

            for(int i = 0; i < array.size(); i++){
                if(array.get(i) instanceof JsonObject){
                    JsonObject part = (JsonObject) array.get(i);
                    builder.append(TEMPLATE.replace("$color", ChatColor.getColorByName(part.get("color").getAsString()))
                            .replace("$text", part.get("text").getAsString()));
                }
            }

            return builder.append("<br>").toString();
        } else {
            //return builder.append(Colors.getByName(message.get("color").getAsString())).append(message.get("text").getAsString()).toString();
            return builder.append(TEMPLATE.replace("$color", ChatColor.getColorByName(message.get("color").getAsString()))
                    .replace("$text", message.get("text").getAsString())).append("<br>").toString();
        }
    }*/

    /*public static String parseColors(String colorStr) throws IllegalArgumentException {
        System.out.println("Start:" + colorStr);
        colorStr = colorStr.replaceAll(invalidCodes.pattern(), "");
        System.out.println("AfterInvalid:" + colorStr);
        final Matcher m = colorPattern.matcher(colorStr);
        StringBuilder builder = new StringBuilder();
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++) {
                if (m.group(i) != null && !m.group(i).isEmpty()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#000000").replace("$text", m.group(1)));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#00002A").replace("$text", m.group(2)));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#002A00").replace("$text", m.group(3)));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#002A2A").replace("$text", m.group(4)));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#2A0000").replace("$text", m.group(5)));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#2A002A").replace("$text", m.group(6)));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#2A2A00").replace("$text", m.group(7)));
                }
                if (m.group(8) != null && !m.group(8).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#2A2A2A").replace("$text", m.group(8)));
                }
                if (m.group(9) != null && !m.group(9).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#151515").replace("$text", m.group(9)));
                }
                if (m.group(10) != null && !m.group(10).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#15153F").replace("$text", m.group(10)));
                }
                if (m.group(11) != null && !m.group(11).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#153F15").replace("$text", m.group(11)));
                }
                if (m.group(12) != null && !m.group(12).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#153F3F").replace("$text", m.group(12)));
                }
                if (m.group(13) != null && !m.group(13).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#3F1515").replace("$text", m.group(13)));
                }
                if (m.group(14) != null && !m.group(14).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#3F153F").replace("$text", m.group(14)));
                }
                if (m.group(15) != null && !m.group(15).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#3F3F15").replace("$text", m.group(15)));
                }
                if (m.group(16) != null && !m.group(16).isEmpty()) {
                    builder.append(TEMPLATE.replace("$color", "#000000").replace("$text", m.group(16)));
                }
                    //break;
            }
            if (!found) builder.append(colorStr);
        }
        System.out.println(builder.toString());
        return builder.toString();
    }*/
}
