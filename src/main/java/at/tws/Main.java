package at.tws;

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

            WordleView view = new WordleView(screen);
            WordleModel model = new WordleModel();
            WordleController controller = new WordleController(model, view);

            controller.startGameLoop();

            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
