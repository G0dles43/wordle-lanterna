package at.tws;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SwingWordleView extends JFrame {
    private WordleModel model;
    private JTextField inputField;
    private JTextPane gameArea; // Zmieniamy JTextArea na JTextPane
    private JButton submitButton;
    private JButton restartButton;

    public SwingWordleView(WordleModel model) {
        this.model = model;
        initializeGUI();
        inputField.addActionListener(e -> submitButton.doClick());

    }

    private void initializeGUI() {
        setTitle("Wordle - GUI Mode");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameArea = new JTextPane(); // Zmieniono z JTextArea
        gameArea.setEditable(false);
        gameArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(gameArea);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputField = new JTextField();
        submitButton = new JButton("Submit");
        restartButton = new JButton("Restart");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);
        inputPanel.add(restartButton, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }



    public String getInputWord() {
        return inputField.getText();
    }

    public void clearInputField() {
        inputField.setText("");
    }

    public void showTemporaryMessage(String message, int duration) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);
        dialog.setSize(200, 100);
        dialog.setLocationRelativeTo(this);

        // Timer to close the dialog after the specified duration
        new Timer(duration, e -> dialog.dispose()).start();

        dialog.setVisible(true);
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

    public void displayHint(String word, WordleModel.Color[] colors) {
        for (int i = 0; i < word.length(); i++) {
            Color color;

            switch (colors[i]) {
                case GREEN -> color = Color.GREEN;
                case YELLOW -> color = Color.ORANGE;
                default -> color = Color.RED;
            }
            appendToPane(gameArea, String.valueOf(word.charAt(i)), color);
        }
        appendToPane(gameArea, "\n", Color.BLACK);
    }

    public void disableInput() {
        inputField.setEnabled(false);
        submitButton.setEnabled(false);
    }

    public void resetView() {
        gameArea.setText("");
        inputField.setText("");
        inputField.setEnabled(true);
        submitButton.setEnabled(true);
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
}
