package at.tws;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingWordleController {
    private final WordleModel model;
    private final SwingWordleView view;

    public SwingWordleController(WordleModel model, SwingWordleView view) {
        this.model = model;
        this.view = view;

        // Podłącz interakcje widoku z kontrolerem
        this.view.setSubmitListener(new SubmitListener());
        this.view.setRestartListener(new RestartListener());
    }

    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = view.getInputWord().toUpperCase();
            if (input.length() != 5) {
                view.showTemporaryMessage("Input must be exactly 5 letters!", 1000);
                return;
            }

            if (!model.isWordValid(input)) {
                view.showTemporaryMessage("Invalid word! Try again.", 1000);
                return;
            }

            if (model.guessWord(input)) {
                view.displayMessage("Congratulations! You guessed the word!");
                view.disableInput(); // Zablokuj pole po wygranej
            } else {
                WordleModel.Color[] colors = model.hint(input);
                view.displayHint(input, colors);

                if (model.getAttempts() >= model.getMaxAttempts()) {
                    view.displayMessage("Game over! The word was: " + model.getWordToGuess());
                    view.disableInput(); // Zablokuj pole po przegranej
                }
            }
            view.clearInputField(); // Czyszczenie pola tekstowego
        }
    }


    private class RestartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.resetGame();
            view.resetView();
            view.displayMessage("New game started!");
        }
    }
}