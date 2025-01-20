package at.tws;

import java.io.IOException;
import java.util.List;

public class WordleController {
    private final WordleModel model;
    private final WordleView view;

    private int currentYPosition = 2;



    public WordleController(WordleModel model, WordleView view) {
        this.model = model;
        this.view = view;
    }

    public void startGameLoop() throws IOException {
        while (true) {
            if (!displayMainMenu()) {
                break;
            }

            playGame();

            if (!displayPlayAgainMenu()) {
                break;
            }
        }
    }

    private boolean displayMainMenu() throws IOException {
        List<String> menuOptions = List.of("Start Game", "Exit");
        int selectedOption = 0;

        while (true) {
            view.displayMenu(menuOptions, selectedOption);
            int userInput = view.getMenuSelection();

            switch (userInput) {
                case -1: // Strzałka w górę
                    selectedOption = (selectedOption - 1 + menuOptions.size()) % menuOptions.size();
                    break;
                case 1: // Strzałka w dół
                    selectedOption = (selectedOption + 1) % menuOptions.size();
                    break;
                case 0: // Enter
                    view.clearArea(0, 0, 40);
                    view.clearArea(0, 1, 40);
                    view.clearArea(0, 2, 40);
                    return selectedOption == 0;
            }
        }
    }

    private boolean displayPlayAgainMenu() throws IOException {
        List<String> menuOptions = List.of("Play Again", "Exit");
        int selectedOption = 0;

        while (true) {
            view.displayMenu(menuOptions, selectedOption);
            int userInput = view.getMenuSelection();

            switch (userInput) {
                case -1: // Strzałka w górę
                    selectedOption = (selectedOption - 1 + menuOptions.size()) % menuOptions.size();
                    break;
                case 1: // Strzałka w dół
                    selectedOption = (selectedOption + 1) % menuOptions.size();
                    break;
                case 0: // Enter
                    view.clearArea(0, 0, 40);
                    view.clearArea(0, 1, 40);
                    return selectedOption == 0; // "Play Again" -> true, "Exit" -> false
            }
        }
    }

    private void playGame() throws IOException {
        model.resetGame();
        currentYPosition = 2;

        while (true) {
            view.displayMessage(1, 1, "Input 5-letter word: ");
            String word = view.readInput(currentYPosition).toUpperCase();

            if (word.length() != 5) {
                view.displayMessage(22, 1, "Input word with exactly 5 letters!");
                view.clearArea(2, currentYPosition, 40);
                continue;
            }

            if (!model.isWordValid(word)) {
                view.displayMessage(22, 1, "Incorrect word! Please try again.");
                view.clearArea(2, currentYPosition, 40);
                continue;
            }

            view.clearArea(2, currentYPosition, 10);
            if (model.guessWord(word)) {
                view.displayWinMessage();
                break;
            } else {
                WordleModel.Color[] colors = model.hint(word);
                view.displayHint(word, colors, currentYPosition);
                view.displayAlphabet(model.getAlphabetColors());
            }

            if (model.getAttempts() >= model.getMaxAttempts()) {
                view.displayLoseMessage(model.getWordToGuess());
                break;
            }

            currentYPosition++;
        }
    }

}