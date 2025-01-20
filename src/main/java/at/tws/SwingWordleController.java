package at.tws;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingWordleController {
    private final WordleModel model;
    private final SwingWordleView view;

    public SwingWordleController(WordleModel model, SwingWordleView view) {
        this.model = model;
        this.view = view;
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

            int currentAttempt = model.getAttempts();

            if (model.guessWord(input)) {
                view.displayHint(currentAttempt, input, model.hint(input));
                view.showWinDialog();
                view.disableInput();
                view.disableSubmitButton();
            } else {
                WordleModel.Color[] colors = model.hint(input);
                view.displayHint(currentAttempt, input, colors);

                // Show hint after incorrect guess
                String hint = "Try a letter that contains: ";
                for (int i = 0; i < input.length(); i++) {
                    if (colors[i] == WordleModel.Color.GREEN) {
                        hint += input.charAt(i) + " ";
                    }
                }
                view.displayMessage(hint);

                // Check if attempts are over
                if (model.getAttempts() >= model.getMaxAttempts()) {
                    String correctWord = model.getWordToGuess();
                    view.showGameOverDialog(correctWord);
                    view.displayMessage("Game over! The word was: " + correctWord);
                }
            }


            view.clearInputField();
            view.updateAlphabetPanel();
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