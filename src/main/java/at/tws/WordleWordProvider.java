package at.tws;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleWordProvider {
    private static final String API_URL = "https://api.datamuse.com/words?sp=?????";

    public String fetchRandomWord() {
        try {
            // Pobieranie danych z API
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

            // Parsowanie odpowiedzi JSON i losowanie słowa
            JSONArray jsonArray = new JSONArray(content.toString());
            Random random = new Random();
            String randomWord = jsonArray.getJSONObject(random.nextInt(jsonArray.length())).getString("word");
            return randomWord.toUpperCase();

        } catch (Exception e) {
            e.printStackTrace();

            // Fallback - lista domyślnych słów
            String[] fallbackWords = {"APPLE", "PEACH", "GRAPE", "PLANK", "PRANK", "HAPPY", "HEART", "TRACK"};
            return fallbackWords[new Random().nextInt(fallbackWords.length)];
        }
    }
    // Sprawdzanie poprawności słowa
    public boolean isWordValid(String userWord) {
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

            // Parsowanie JSON do listy słów
            String[] words = content.toString().split("\"word\":\"|\"");
            List<String> wordList = new ArrayList<>();
            for (int i = 1; i < words.length; i += 2) { // Co drugie słowo
                wordList.add(words[i]);
            }

            // Sprawdź, czy słowo użytkownika znajduje się na liście
            return wordList.contains(userWord.toLowerCase());

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Zakładamy, że słowo jest niepoprawne w przypadku błędu
        }
    }
}
