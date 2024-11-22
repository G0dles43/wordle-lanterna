package at.tws;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.io.IOException;

public class WordleView {
    private final Screen screen;
    private final TextGraphics textGraphics;

    private int currentYPosition=2;

    public WordleView(Screen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();
    }

    public void displayMessage(int positonX, int positionY, String message) throws IOException {
        textGraphics.putString(positonX, positionY, message, SGR.BOLD);
        screen.refresh();
    }

    public void displayHint(String word, WordleModel.Color[] colors) throws IOException {


        int xPosition = 2; // Początkowa pozycja X

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);

            // Ustawiamy kolor na podstawie tablicy kolorów
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

            // Wyświetlanie litery w odpowiednim kolorze
            textGraphics.putString(xPosition, currentYPosition, String.valueOf(letter), SGR.BOLD);
            xPosition++;
        }

        currentYPosition++;
        // Odswieżenie ekranu po wyświetleniu podpowiedzi
        screen.refresh();
    }

    public void displayAlphabet(Map<Character, WordleModel.Color> alphabetColors) throws IOException {
        int yPosition = 7;
        int xPosition = 2;

        for (char letter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            WordleModel.Color color = alphabetColors.get(letter);
            // Ustawiamy odpowiedni kolor na podstawie stanu litery
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
                textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT); // Domyślny kolor
            }
            if (letter=='N'){
                yPosition++;
                xPosition=2;
            }
            // Wyświetlanie litery
            textGraphics.putString(xPosition, yPosition, String.valueOf(letter), SGR.BOLD);
            xPosition++;
        }

        screen.refresh();
    }
    // Dodanie metody wyświetlającej komunikat o wygranej
    public void displayWinMessage() throws IOException {
        textGraphics.putString(2, 9, "Congratulations! You win!", SGR.BOLD);
        screen.refresh();
        screen.readInput();
    }

    // Dodanie metody wyświetlającej komunikat o przegranej
    public void displayLoseMessage(String wordToGuess) throws IOException {
        textGraphics.putString(2, 9, "You lost! The word was: " + wordToGuess, SGR.BOLD);
        screen.refresh();
        screen.readInput();
    }

    public String readInput() throws IOException {
        StringBuilder userInput = new StringBuilder();
        KeyStroke keyStroke;

        while (true) {
            keyStroke = screen.readInput();
            if (keyStroke.getKeyType() == KeyType.Enter) {
                break;
            } else if (keyStroke.getKeyType() == KeyType.Character && userInput.length() < 5) {
                // Dodanie znaku
                userInput.append(keyStroke.getCharacter());
                textGraphics.putString(2 + userInput.length() - 1, currentYPosition,
                        String.valueOf(keyStroke.getCharacter()), SGR.BOLD);
                screen.refresh();
            } else if (keyStroke.getKeyType() == KeyType.Backspace && userInput.length() > 0) {
                // Usunięcie ostatniego znaku
                int deletePosition = 2 + userInput.length() - 1;
                textGraphics.putString(deletePosition, currentYPosition, " ", SGR.BOLD);
                userInput.deleteCharAt(userInput.length() - 1);

                screen.refresh();
            }
        }
        return userInput.toString();
    }

    public void clearInputArea() throws IOException {
        // Czyszczenie dokładnie pięciu pozycji w wierszu
        for (int i = 0; i < 5; i++) {
            textGraphics.putString(2 + i, currentYPosition, " ", SGR.BOLD);
        }
        screen.refresh();
    }


}
