package co.axelrod.webnsfw.util;

import co.axelrod.webnsfw.model.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 27.01.2018.
 */
public class Sorter {
    public static void sortResults(String unsortedResultsFileName, String sortedResultsFileName) {
        System.out.println("Starting result sorting");

        try {
            List<Result> results = new ArrayList<>();

            InputStream is = new FileInputStream(unsortedResultsFileName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            // Skipping first result
            String line = buf.readLine();
            line = buf.readLine();

            if (line.split(" ").length > 2) {
                String[] splittedLine = line.split(" ");
                for (int i = 0; i < splittedLine.length / 2; i++) {
                    Float score = Float.valueOf(splittedLine[i]);
                    String file = splittedLine[i + 1];
                    Integer likes = getLikes(file);
                    Float finalScore = score * likes;
                    results.add(new Result(finalScore, file));
                    i++;
                }

            } else {
                while (line != null) {
                    Float score = Float.valueOf(line.split(" ")[0]);
                    String file = line.split(" ")[1];

                    Integer likes = getLikes(file);

                    //Float finalScore = score * likes;
                    Float finalScore = score;

                    results.add(new Result(finalScore, file));
                    line = buf.readLine();
                }
            }

            results.sort(new Comparator<Result>() {
                public int compare(Result p1, Result p2) {
                    return (int) Float.compare(p1.getScore(), p2.getScore()) * (-1);
                }
            });

            try (PrintStream out = new PrintStream(new FileOutputStream(sortedResultsFileName))) {
                int i = 0;
                for (Result result : results) {
                    out.println(result.getFile());
                    if (++i == 100) {
                        break;
                    }
                }
            }

            System.out.println("Sorting result saved in file: " + sortedResultsFileName);
        } catch (Exception ex) {
            System.out.println("Unable to parse results file and sort");
        }
    }

    private static Integer getLikes(String fileName) {
        //photo/138517103/331906843_34534.jpg
        String likesCount = fileName.split("/")[2].split("_")[1];
        likesCount = likesCount.substring(0, likesCount.length() - 4);

        return new Integer(likesCount);
    }
}
