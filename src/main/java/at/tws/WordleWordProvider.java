package at.tws;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class WordleWordProvider {
    private static final String API_URL = "https://api.datamuse.com/words?sp=?????";

    public String fetchRandomWord() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            // Parsujemy JSON i losujemy jedno słowo
            String[] words = content.toString().split("\"word\":\"|\"");
            Random random = new Random();
            String randomWord = words[(random.nextInt(words.length / 2) * 2) + 1];
            return randomWord.toUpperCase();

        } catch (Exception e) {
            e.printStackTrace();
            String[] fallbackWords = {"APPLE", "PEACH", "GRAPE", "PLANK", "PRANK", "HAPPY", "HEART", "TRACK"};
            return fallbackWords[new Random().nextInt(fallbackWords.length)];
        }
    }
}
