package at.tws;

import java.io.IOException;

public class WordleController {
    private final WordleModel model;
    private final WordleView view;

    public WordleController(WordleModel model, WordleView view) {
        this.model = model;
        this.view = view;
    }

    public void startGame() throws IOException {
        while (true) {
            view.displayMessage(1,1,"Input 5-letter word: ");
            String word = view.readInput().toUpperCase();

            if (word.length() != 5) {
                view.displayMessage(22,1,"Input word with exactly 5 letters!");
                view.clearInputArea();
                continue;
            }

            if (model.guessWord(word)) {
                view.displayWinMessage();
                break;
            } else {
                // Generowanie podpowiedzi
                WordleModel.Color[] colors = model.hint(word);
                view.displayHint(word, colors); // Wyświetlenie podpowiedzi
                view.displayAlphabet(model.getAlphabetColors());
            }

            if (model.getAttempts() >= model.getMaxAttempts()) {
                view.displayLoseMessage(model.getWordToGuess());
                break;
            }
            view.clearInputArea();
        }
    }
}
