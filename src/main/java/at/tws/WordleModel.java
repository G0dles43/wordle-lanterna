package at.tws;

import java.util.*;

public class WordleModel {
    private StringBuilder alphabet = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    private String wordToGuess;
    private int attempts = 0;
    private final int maxAttempts = 5;
    private Set<Character> usedLetters = new HashSet<>();
    private WordleWordProvider wordProvider;

    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;

    public enum Color {
        GREEN,
        YELLOW,
        RED
    }

    public WordleModel() {
        this.wordProvider = new WordleWordProvider();
        int[] stats = StatsManager.loadStats();
        this.gamesPlayed = stats[0];
        this.gamesWon = stats[1];
        this.gamesLost = stats[2];
        resetGame();
    }

    public boolean guessWord(String word) {
        if (word.equals(this.wordToGuess)) {
            gamesWon++;
            gamesPlayed++;
            StatsManager.saveStats(gamesPlayed, gamesWon, gamesLost);
            return true;
        } else {
            this.attempts++;
            usedLetters.addAll(word.chars().mapToObj(c -> (char) c).toList());

            if (this.attempts >= maxAttempts) {
                gamesLost++;
                gamesPlayed++;
                StatsManager.saveStats(gamesPlayed, gamesWon, gamesLost);
            }

            return false;
        }
    }

    public Color[] hint(String word) {
        Color[] colors = new Color[word.length()];
        StringBuilder wordToGuessCopy = new StringBuilder(wordToGuess);

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (letter == wordToGuess.charAt(i)) {
                colors[i] = Color.GREEN;
                wordToGuessCopy.setCharAt(i, '~');
            }
        }

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (colors[i] == null) {
                if (wordToGuessCopy.indexOf(String.valueOf(letter)) != -1) {
                    colors[i] = Color.YELLOW;
                    wordToGuessCopy.setCharAt(wordToGuessCopy.indexOf(String.valueOf(letter)), '~');
                } else {
                    colors[i] = Color.RED;
                }
            }
        }

        return colors;
    }

    public Map<Character, Color> getAlphabetColors() {
        Map<Character, Color> alphabetColors = new HashMap<>();

        // Loop through the alphabet and assign colors based on guesses
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
                alphabetColors.put(letter, null); // Letter not used yet
            }
        }
        return alphabetColors;
    }


    public void resetGame() {
        this.wordToGuess = wordProvider.fetchRandomWord();
        this.attempts = 0;
        this.usedLetters.clear();
    }

    public void resetStatistics() {
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
    }

    public WordleWordProvider getWordProvider() {
        return wordProvider;
    }

    public Set<Character> getUsedLetters() {
        return usedLetters;
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
        return wordProvider.isWordValid(word);
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public StringBuilder getAlphabet() {
        return alphabet;
    }
}
