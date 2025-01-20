package at.tws;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import javax.swing.*;
import java.io.IOException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose mode:");
        System.out.println("1. Text mode (Lanterna)");
        System.out.println("2. GUI mode (Swing)");

        int choice = scanner.nextInt();
        if (choice == 1) {
            startLanternaMode();
        } else if (choice == 2) {
            startSwingMode();
        } else {
            System.out.println("Invalid choice!");
        }
    }

    private static void startLanternaMode() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            WordleView view = new WordleView(screen);
            WordleModel model = new WordleModel();
            WordleController controller = new WordleController(model, view);

            controller.startGameLoop();

            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startSwingMode() {
                WordleModel model = new WordleModel();
                SwingWordleView view = new SwingWordleView(model);
                SwingWordleController controller = new SwingWordleController(model, view);
                view.setVisible(true);
    }
}

