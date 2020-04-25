package org.uma.jmetal.bzu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFileUtils {

    private static final String COMMA_DELIMITER = ",";

    public static   int [][] readTestCaseFaults(String directory, int numberOfTestCases) {
        String fileName = directory+"/past_faults.csv";
        List<String> lines = readFile(fileName);

        int data [][] = new int[numberOfTestCases][];

        for (int i = 0; i < lines.size(); i++) {
            List<String> csvData=  getRecordFromLine(lines.get(i));
            data[i] = new int[csvData.size()];
            for (int j = 0; j <csvData.size(); j++) {
                data[i][j] = Integer.parseInt(csvData.get(j));
            }
        }
        return data;
    }

    public static    double [] readTestCaseExecutionTime(String directory, int numberOfTestCases) {
        String fileName = directory+"/cost.csv";
        double data [] = new double[numberOfTestCases];
        List<String> lines = readFile(fileName);

        for (int i = 0; i < lines.size(); i++) {
            data[i] = Double.parseDouble(lines.get(i));
        }
        return data;
    }

    public static   List readFile(String fileName){
        List<String> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName));) {
            while (scanner.hasNextLine()) {
                String line =scanner.nextLine();
                records.add(line);
                System.out.println(line);
            }
            return records;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static   List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
