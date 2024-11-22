package at.tws;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordleModel {
    private StringBuilder alphabet = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    private String wordToGuess;
    private int attempts = 0;
    private final int maxAttempts = 5;
    private Set<Character> usedLetters = new HashSet<>(); // Zbiór użytych liter
    private WordleWordProvider wordProvider;

    public enum Color {
        GREEN,
        YELLOW,
        RED
    }

    public WordleModel() {
        this.wordProvider = new WordleWordProvider();
        this.wordToGuess = wordProvider.fetchRandomWord();
    }

    public boolean guessWord(String word) {
        if (word.equals(this.wordToGuess)) {
            return true;
        } else {
            this.attempts++;
            usedLetters.addAll(word.chars().mapToObj(c -> (char) c).toList());  // Dodanie użytych liter
            return false;
        }
    }

    public Color[] hint(String word) {
        Color[] colors = new Color[word.length()]; // Tablica kolorów o długości słowa
        StringBuilder pomWordToGuess = new StringBuilder(wordToGuess);

        for (int index = 0; index < word.length(); index++) {
            char letter = word.charAt(index);
            if (letter == pomWordToGuess.charAt(index)) {
                // Litera w dobrej pozycji - zielony
                colors[index] = Color.GREEN;
                pomWordToGuess.setCharAt(index, '~'); // Znak zastępujący
            } else if (pomWordToGuess.indexOf(Character.toString(letter)) != -1) {
                // Litera w złej pozycji - żółty
                colors[index] = Color.YELLOW;
                int foundIndex = pomWordToGuess.indexOf(Character.toString(letter));
                pomWordToGuess.setCharAt(foundIndex, '~'); // Znak zastępujący
            } else {
                // Litera, której nie ma w słowie - czerwony
                colors[index] = Color.RED;
            }
        }
        return colors;
    }

    // Definicja prostego enum dla kolorów
    public Map<Character, Color> getAlphabetColors() {
        Map<Character, Color> alphabetColors = new HashMap<>();

        for (char letter : alphabet.toString().toCharArray()) {
            if (usedLetters.contains(letter)) {
                if (wordToGuess.contains(Character.toString(letter))) {
                    boolean correctPosition = false;
                    for (int i = 0; i < wordToGuess.length(); i++) {
                        if (wordToGuess.charAt(i) == letter) {
                            correctPosition = true;
                            break;
                        }
                    }
                    alphabetColors.put(letter, correctPosition ? Color.GREEN : Color.YELLOW);
                } else {
                    alphabetColors.put(letter, Color.RED);
                }
            } else {
                alphabetColors.put(letter, null); // Nie użyto
            }
        }

        return alphabetColors;
    }


    public Set<Character> getUsedLetters() {
        return usedLetters;  // Zwraca zbiór użytych liter
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public StringBuilder getAlphabet() {
        return alphabet;
    }
}
