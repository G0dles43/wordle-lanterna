package at.tws;

public interface WordleViewInterface {
    void displayMessage(String message);
    void displayHint(String word, WordleModel.Color[] colors);
}
