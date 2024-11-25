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
    private Set<Character> usedLetters = new HashSet<>();
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
        Color[] colors = new Color[word.length()];
        StringBuilder wordToGuessCopy = new StringBuilder(wordToGuess);

        // Pierwsza pętla: Sprawdzenie poprawnych liter w poprawnych miejscach (zielony)
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (letter == wordToGuess.charAt(i)) {
                colors[i] = Color.GREEN;
                wordToGuessCopy.setCharAt(i, '~');
            }
        }

        // Druga pętla: Sprawdzenie liter, które są w słowie, ale w złym miejscu (żółty)
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (colors[i] == null) { // Jeśli litera jeszcze nie została oznaczona jako zielona
                if (wordToGuessCopy.indexOf(String.valueOf(letter)) != -1) {
                    colors[i] = Color.YELLOW;
                    wordToGuessCopy.setCharAt(wordToGuessCopy.indexOf(String.valueOf(letter)), '~');
                } else {
                    colors[i] = Color.RED; // Jeśli litera nie ma miejsca w słowie, kolorujemy na czerwono
                }
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

    public void resetGame() {
        this.wordToGuess = wordProvider.fetchRandomWord();
        this.attempts = 0;
        this.usedLetters.clear();
    }

    public WordleWordProvider getWordProvider() {
        return wordProvider;
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
    public boolean isWordValid(String word) {
        return wordProvider.isWordValid(word); // Delegowanie do WordleWordProvider
    }


    public StringBuilder getAlphabet() {
        return alphabet;
    }
}
