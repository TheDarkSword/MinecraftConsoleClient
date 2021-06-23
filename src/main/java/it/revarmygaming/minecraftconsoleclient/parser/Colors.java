package it.revarmygaming.minecraftconsoleclient.parser;

import org.fusesource.jansi.Ansi;

public enum Colors {

    BLACK("black", "0", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString()),
    DARK_BLUE("dark_blue", "1", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString()),
    DARK_GREEN("dark_green", "2", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString()),
    DARK_AQUA("dark_aqua", "3", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString()),
    DARK_RED("dark_red",  "4",Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString()),
    DARK_PURPLE("dark_purple", "5", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString()),
    GOLD("gold", "6", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString()),
    GRAY("gray", "7", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString()),
    DARK_GRAY("dark_gray", "8", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString()),
    BLUE("blue", "9", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString()),
    GREEN("green", "a", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString()),
    AQUA("aqua", "b", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString()),
    RED("red", "c", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString()),
    LIGHT_PURPLE("light_purple", "d", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString()),
    YELLOW("yellow", "e", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString()),
    WHITE("white", "f", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString()),

    MAGIC("magic", "k", Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString()),
    BOLD("bold", "m", Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString()),
    STRIKETHROUGH("strikethrough", "l", Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString()),
    UNDERLINE("underline", "n", Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString()),
    ITALIC("italic", "o", Ansi.ansi().a(Ansi.Attribute.ITALIC).toString()),
    RESET("reset", "r", Ansi.ansi().a(Ansi.Attribute.RESET).toString());

    private final String name;
    private final String colorCode;
    private final String color;

    Colors(String name, String colorCode, String color){
        this.name = name;
        this.colorCode = "&" + colorCode;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString(){
        return color;
    }

    public static String getByName(String name){
        for(Colors color : Colors.values()){
            if(color.getName().equals(name)){
                return color.getColor();
            }
        }
        return "";
    }

    public static String translateColorCodes(String text){
        if (text == null || text.trim().isEmpty()) return text;

        for(Colors color : Colors.values()){
            text = text.replace(color.getColorCode(), color.getColor());
        }

        return text;
    }
}
