package at.tws;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

public class SwingWordleView extends JFrame {
    private final WordleModel model;
    private final JTextField[] letterFields = new JTextField[5];
    private final JTextPane gameArea;
    private final JButton submitButton;
    private final JButton restartButton;
    private final JPanel alphabetPanel;
    private final JTextField[][] hintFields = new JTextField[5][5];
    private Timer gameTimer;
    private long startTime;
    private JLabel timerLabel;


    public SwingWordleView(WordleModel model) {
        this.model = model;
        this.gameArea = new JTextPane();
        this.submitButton = new JButton("Submit");
        this.restartButton = new JButton("Restart");
        this.alphabetPanel = new JPanel();
        initializeGUI();
    }

    private void appendToPane(JTextPane textPane, String text, Color color) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void showTemporaryMessage(String message, int duration) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);
        dialog.setSize(200, 100);
        dialog.setLocationRelativeTo(this);

        new Timer(duration, e -> dialog.dispose()).start();

        dialog.setVisible(true);
    }


    private void initializeGUI() {
        setTitle("Wordle - GUI Mode");
        setSize(600, 700);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);


        setJMenuBar(createMenuBar());

        JPanel hintGridPanel = new JPanel(new GridLayout(5, 5, 5, 5)); // 5 rzędów, 5 kolumn z odstępami
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                JTextField hintField = new JTextField();
                hintField.setEditable(false);
                hintField.setHorizontalAlignment(JTextField.CENTER);
                hintField.setFont(new Font("Monospaced", Font.BOLD, 18));
                hintFields[row][col] = hintField;
                hintGridPanel.add(hintField);
            }
        }

        JPanel inputGridPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        for (int i = 0; i < 5; i++) {
            JTextField letterField = new JTextField(1);
            letterField.setHorizontalAlignment(JTextField.CENTER);
            letterField.setFont(new Font("Monospaced", Font.BOLD, 24));
            letterField.setDocument(new LimitedDocument(1));
            letterField.addKeyListener(new LetterFieldKeyListener(i));
            letterField.addActionListener(e -> submitButton.doClick());
            letterFields[i] = letterField;
            inputGridPanel.add(letterField);
        }


        alphabetPanel.setLayout(new GridLayout(3, 9, 5, 5));
        updateAlphabetPanel();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        submitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        restartButton.setFont(new Font("Arial", Font.PLAIN, 14));
        buttonPanel.add(submitButton);
        buttonPanel.add(restartButton);

        add(hintGridPanel, BorderLayout.NORTH);
        add(alphabetPanel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputGridPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        startTime = System.currentTimeMillis();
        gameTimer = new Timer(1000, e -> updateTimer());
        gameTimer.start();
    }

    private void updateTimer() {
        long elapsed = System.currentTimeMillis() - startTime;
        long seconds = (elapsed / 1000) % 60;
        long minutes = (elapsed / 60000) % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void showStats() {
        JOptionPane.showMessageDialog(this,
                "Games Played: " + model.getGamesPlayed() + "\n" +
                        "Games Won: " + model.getGamesWon() + "\n" +
                        "Games Lost: " + model.getGamesLost() + "\n" +
                        "Win Percentage: " + (model.getGamesWon()*100/model.getGamesPlayed()) +"%",
                "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("----{MENU}----");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem statsItem = new JMenuItem("Statistics");
        JMenuItem resetStatsItem = new JMenuItem("Reset Statistics");
        JMenuItem exitItem = new JMenuItem("Exit");

        newGameItem.addActionListener(e -> resetGame());
        statsItem.addActionListener(e -> showStats());

        resetStatsItem.addActionListener(e -> resetStatistics());
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGameItem);
        gameMenu.add(statsItem);
        gameMenu.add(exitItem);
        gameMenu.add(resetStatsItem);
        menuBar.add(gameMenu);
        return menuBar;
    }


    private void resetStatistics() {
        model.resetStatistics();
        JOptionPane.showMessageDialog(this, "Statistics have been reset.", "Reset Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
    private void resetGame() {
        model.resetGame();
        resetView();
        displayMessage("New game started!");
    }

    public String getInputWord() {
        StringBuilder word = new StringBuilder();
        for (JTextField letterField : letterFields) {
            word.append(letterField.getText().toUpperCase());
        }
        return word.toString();
    }

    public void displayHint(int row, String word, WordleModel.Color[] colors) {
        Timer timer = new Timer(500, null);
        final int[] index = {0};

        timer.addActionListener(e -> {
            if (index[0] < word.length()) {
                char letter = word.charAt(index[0]);
                Color color;
                switch (colors[index[0]]) {
                    case GREEN -> color = Color.GREEN;
                    case YELLOW -> color = Color.ORANGE;
                    default -> color = Color.RED;
                }

                hintFields[row][index[0]].setText(String.valueOf(letter));
                hintFields[row][index[0]].setBackground(color);

                index[0]++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });

        timer.start();

    }


    public void clearInputField() {
        for (JTextField letterField : letterFields) {
            letterField.setText("");
        }
        letterFields[0].requestFocus();
    }


    public void setSubmitListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void setRestartListener(ActionListener listener) {
        restartButton.addActionListener(listener);
    }

    public void displayMessage(String message) {
        appendToPane(gameArea, message + "\n", Color.BLACK);
    }

    public void disableInput() {
        for (JTextField letterField : letterFields) {
            letterField.setEnabled(false);
        }
        submitButton.setEnabled(false);
        gameTimer.stop();
    }


    public void showGameOverDialog(String correctWord) {
        // Tworzymy przyciski dla opcji
        Object[] options = {"Play Again", "Exit"};

        // Wyświetlamy dialog z pytaniem, co zrobić
        int response = JOptionPane.showOptionDialog(this,
                "You lost! The correct word was: " + correctWord,
                "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (response == 0) { // "Play Again"
            resetGame(); // Zresetowanie gry
        } else if (response == 1) { // "Exit"
            System.exit(0); // Zakończenie aplikacji
        }
    }



    public void disableSubmitButton() {
        submitButton.setEnabled(false);
    }

    public void showWinDialog() {
        JOptionPane.showMessageDialog(this, "Congratulations! You win!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLoseDialog(String correctWord) {
        JOptionPane.showMessageDialog(this, "You lose! The correct word was: " + correctWord, "Game Over", JOptionPane.ERROR_MESSAGE);
    }

    public void resetView() {
        gameArea.setText("");
        clearInputField();
        for (JTextField letterField : letterFields) {
            letterField.setEnabled(true);
        }
        submitButton.setEnabled(true);
        updateAlphabetPanel();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                hintFields[row][col].setText("");
                hintFields[row][col].setBackground(Color.WHITE);
            }
        }
    }

    public void updateAlphabetPanel() {
        alphabetPanel.removeAll();  // Usuń wszystkie przyciski z panelu, by dodać nowe zaktualizowane

        // Pobierz aktualne kolory liter z modelu
        Map<Character, WordleModel.Color> alphabetColors = model.getAlphabetColors();

        // Dodaj przyciski dla każdej litery w alfabecie
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            JButton letterButton = new JButton(String.valueOf(letter));
            letterButton.setEnabled(false);  // Przyciski tylko do wyświetlania (brak interakcji)

            // Pobierz kolor przypisany danej literze
            WordleModel.Color color = alphabetColors.getOrDefault(letter, null);

            // Jeśli istnieje kolor, ustaw tło przycisku
            if (color != null) {
                switch (color) {
                    case GREEN:
                        letterButton.setBackground(Color.GREEN);
                        break;
                    case YELLOW:
                        letterButton.setBackground(Color.ORANGE);
                        break;
                    case RED:
                        letterButton.setBackground(Color.RED);
                        break;
                    default:
                        letterButton.setBackground(Color.LIGHT_GRAY);  // Domyślny kolor, jeśli litera nie została jeszcze użyta
                        break;
                }
            } else {
                letterButton.setBackground(Color.LIGHT_GRAY);  // Domyślny kolor
            }

            // Ustaw opcje dla przycisku (chcemy, by tło było widoczne)
            letterButton.setOpaque(true);
            letterButton.setBorderPainted(false);  // Usuń ramkę przycisku dla czystszego wyglądu
            alphabetPanel.add(letterButton);  // Dodaj przycisk do panelu
        }

        // Odśwież panel, by zaktualizowane kolory były widoczne
        alphabetPanel.revalidate();
        alphabetPanel.repaint();
    }


    private static class LimitedDocument extends PlainDocument {
        private final int limit;

        public LimitedDocument(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null || (getLength() + str.length()) > limit) {
                return;
            }
            super.insertString(offset, str, attr);
        }
    }

    private class LetterFieldKeyListener extends KeyAdapter {
        private final int index;

        public LetterFieldKeyListener(int index) {
            this.index = index;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (index < 4 && !Character.isISOControl(e.getKeyChar())) {
                letterFields[index + 1].requestFocus();
            }

            // Obsługa naciśnięcia Enter
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                submitButton.doClick();  // Wywołanie przycisku Submit po naciśnięciu Enter
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && index > 0 && letterFields[index].getText().isEmpty()) {
                letterFields[index - 1].requestFocus();
            }
        }
    }
}
