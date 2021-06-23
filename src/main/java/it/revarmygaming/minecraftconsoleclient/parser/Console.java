package it.revarmygaming.minecraftconsoleclient.parser;

import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;

public class Console {

    static {
        try {
            AnsiConsole.out().install();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void print(String message, boolean translate) {
        if (translate) message = Colors.translateColorCodes(message);
        AnsiConsole.out().print(message + Colors.RESET.getColor());
    }

    public static void print(Object message, boolean translate) {
        print(message.toString(), translate);
    }

    public static void print(String message) {
        print(message, true);
    }

    public static void print(Object message) {
        print(message.toString());
    }

    public static void println(String message, boolean translate) {
        if (translate) message = Colors.translateColorCodes(message);
        AnsiConsole.out().println(message + Colors.RESET.getColor());
    }

    public static void println(Object message, boolean translate) {
        println(message.toString(), translate);
    }

    public static void println(String message) {
        println(message, true);
    }

    public static void println(Object message) {
        println(message.toString());
    }
}
