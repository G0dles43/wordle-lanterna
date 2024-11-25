package at.tws;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WordleView {
    private final Screen screen;
    private final TextGraphics textGraphics;

    public WordleView(Screen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();
    }

    public void displayMenu(List<String> options, int selectedOption) throws IOException {
        screen.clear();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

        int yPosition = 1; // Startowa pozycja Y dla menu
        textGraphics.putString(5, 0, "WORDLE" , SGR.BOLD);
        for (int i = 0; i < options.size(); i++) {
            if (i == selectedOption) {
                textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                textGraphics.putString(1, yPosition, "> " + options.get(i), SGR.BOLD);
            } else {
                textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                textGraphics.putString(1, yPosition, options.get(i));
            }
            yPosition++;
        }

        screen.refresh();
    }

    public int getMenuSelection() throws IOException {
        KeyStroke keyStroke = screen.readInput();
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            return -1; // Strzałka w górę
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            return 1; // Strzałka w dół
        } else if (keyStroke.getKeyType() == KeyType.Enter) {
            return 0; // Klawisz Enter
        }
        return Integer.MIN_VALUE; // Ignoruj inne klawisze
    }

    public void displayMessage(int positionX, int positionY, String message) throws IOException {
        textGraphics.putString(positionX, positionY, message, SGR.BOLD);
        screen.refresh();
    }

    public void displayHint(String word, WordleModel.Color[] colors, int yPosition) throws IOException {
        int xPosition = 2;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);

            switch (colors[i]) {
                case GREEN:
                    textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                    break;
                case YELLOW:
                    textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
                    break;
                case RED:
                    textGraphics.setForegroundColor(TextColor.ANSI.RED);
                    break;
                default:
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
            }

            textGraphics.putString(xPosition, yPosition, String.valueOf(letter), SGR.BOLD);
            xPosition++;
        }

        screen.refresh();
    }


    public String readInput(int currentYPosition) throws IOException {
        StringBuilder userInput = new StringBuilder();
        KeyStroke keyStroke;

        while (true) {
            keyStroke = screen.readInput();
            if (keyStroke.getKeyType() == KeyType.Enter) {
                break;
            } else if (keyStroke.getKeyType() == KeyType.Character && userInput.length() < 5) {
                userInput.append(keyStroke.getCharacter());
                textGraphics.putString(2 + userInput.length() - 1, currentYPosition,
                        String.valueOf(keyStroke.getCharacter()), SGR.BOLD);
                screen.refresh();
            } else if (keyStroke.getKeyType() == KeyType.Backspace && userInput.length() > 0) {
                int deletePosition = 2 + userInput.length() - 1;
                textGraphics.putString(deletePosition, currentYPosition, " ", SGR.BOLD);
                userInput.deleteCharAt(userInput.length() - 1);
                screen.refresh();
            }
        }
        return userInput.toString();
    }

    public void displayAlphabet(Map<Character, WordleModel.Color> alphabetColors) throws IOException {
        int yPosition = 7;
        int xPosition = 2;

        for (char letter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            WordleModel.Color color = alphabetColors.get(letter);

            if (color != null) {
                switch (color) {
                    case GREEN:
                        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                        break;
                    case YELLOW:
                        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
                        break;
                    case RED:
                        textGraphics.setForegroundColor(TextColor.ANSI.RED);
                        break;
                    default:
                        textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
            } else {
                textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
            }

            if (letter == 'N') {
                yPosition++;
                xPosition = 2;
            }

            textGraphics.putString(xPosition, yPosition, String.valueOf(letter), SGR.BOLD);
            xPosition++;
        }

        screen.refresh();
    }

    public void displayWinMessage() throws IOException {
        textGraphics.putString(2, 9, "Congratulations! You win!", SGR.BOLD);
        screen.refresh();
        screen.readInput(); // Czeka na interakcję
    }

    public void displayLoseMessage(String wordToGuess) throws IOException {
        textGraphics.putString(2, 9, "You lost! The word was: " + wordToGuess, SGR.BOLD);
        screen.refresh();
        screen.readInput(); // Czeka na interakcję
    }
    public void clearArea(int x, int y, int quantity) throws IOException {
        for (int i = 0; i < quantity; i++) {
            textGraphics.putString(x, y, " ".repeat(50), SGR.BOLD);
        }
        screen.refresh();
    }
}
