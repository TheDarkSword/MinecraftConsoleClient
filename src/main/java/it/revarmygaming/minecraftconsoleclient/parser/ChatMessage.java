package it.revarmygaming.minecraftconsoleclient.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChatMessage {

    private final JsonObject message;

    public ChatMessage(JsonObject message){
        this.message = message;
    }

    public ChatMessage(String message){
        this((JsonObject) JsonParser.parseString(message));
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        if(message.get("text").getAsString().isEmpty()){
            JsonArray array = message.get("extra").getAsJsonArray();

            for(int i = 0; i < array.size(); i++){
                if(array.get(i) instanceof JsonObject){
                     JsonObject part = (JsonObject) array.get(i);
                     builder.append(Colors.getByName(part.get("color").getAsString())).append(part.get("text").getAsString());
                }
            }

            return builder.toString();
        } else {
            return builder.append(Colors.getByName(message.get("color").getAsString())).append(message.get("text").getAsString()).toString();
        }
    }
}
