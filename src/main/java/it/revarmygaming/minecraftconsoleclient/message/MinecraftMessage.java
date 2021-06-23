package it.revarmygaming.minecraftconsoleclient.message;

public class MinecraftMessage {

    private String message;

    private MinecraftMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {
        private final StringBuilder builder;
        private String text;
        private String color;

        public Builder(){
            builder = new StringBuilder("{text:\"$text\", color:\"$color\"}");
            text = "";
            color = "";
        }

        public Builder setText(String text){
            this.text = text;
            return this;
        }

        public Builder setColor(String color){
            this.color = color;
            return this;
        }

        public MinecraftMessage build(){
            return new MinecraftMessage(builder.toString().replace("$text", text).replace("$color", color));
        }
    }
}
