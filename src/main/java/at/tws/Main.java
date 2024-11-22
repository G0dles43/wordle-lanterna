package at.tws;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.clear();

            WordleModel model = new WordleModel();
            WordleView view = new WordleView(screen);
            WordleController controller = new WordleController(model, view);

            controller.startGame();
            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}








