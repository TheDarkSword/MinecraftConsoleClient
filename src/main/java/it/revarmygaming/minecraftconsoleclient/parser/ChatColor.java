package it.revarmygaming.minecraftconsoleclient.parser;

public enum ChatColor {

    BLACK("black", "#000000"),
    DARK_BLUE("dark_blue", "#00008B"),
    DARK_GREEN("dark_green", "#006400"),
    DARK_AQUA("dark_aqua", "#008B8B"),
    DARK_RED("dark_red", "#8B0000"),
    DARK_PURPLE("dark_purple", "#9400D3"),
    GOLD("gold", "#FFD700"),
    GRAY("gray", "#C0C0C0"),
    DARK_GRAY("dark_gray", "#808080"),
    BLUE("blue", "#6495ED"),
    GREEN("green", "#00FF00"),
    AQUA("aqua", "#00FFFF"),
    RED("red", "#FF0000"),
    LIGHT_PURPLE("light_purple", "#DA70D6"),
    YELLOW("yellow", "#FFFF00"),
    WHITE("white", "#FFFFFF");

    private final String name;
    private final String color;

    ChatColor(String name, String color){
        this.name = name;
        this.color = color;
    }

    public static String getColorByName(String name){
        for(ChatColor color : values()){
            if(color.name.equals(name)) return color.color;
        }
        return "";
    }

    @Override
    public String toString(){
        return color;
    }
}
