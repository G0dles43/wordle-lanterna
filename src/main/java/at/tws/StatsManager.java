package at.tws;

import java.io.*;
import java.util.*;

public class StatsManager {
    private static final String FILE_NAME = "game_stats.txt";

    public static void saveStats(int gamesPlayed, int gamesWon, int gamesLost) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(gamesPlayed + "\n");
            writer.write(gamesWon + "\n");
            writer.write(gamesLost + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] loadStats() {
        int[] stats = new int[3];
        File file = new File(FILE_NAME);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                stats[0] = Integer.parseInt(reader.readLine());
                stats[1] = Integer.parseInt(reader.readLine());
                stats[2] = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stats;
    }
}
