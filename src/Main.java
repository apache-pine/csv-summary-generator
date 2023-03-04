import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get input file path from user
        System.out.print("Enter the input file path: ");
        String inputFilePath = scanner.nextLine();

        // Get delimiter from user
        System.out.print("Enter the delimiter: ");
        String delimiter = scanner.nextLine();

        // Get the output file path from user
        System.out.print("Enter the output file path: ");
        String outputFilePath = scanner.nextLine();

        // Create a new CSVReader instance and read the CSV file
        CSVReader csvReader = new CSVReader(inputFilePath, delimiter);
        List<String[]> data = null;
        try {
            data = csvReader.read();
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        }

        // Extract the column headings
        String[] columnHeadings = data.get(0);

        // Remove the first row (column headings) from the data
        data.remove(0);

        // Create a new StatsCalculator instance and calculate statistics for the CSV
        // file
        List<List<String>> dataList = new ArrayList<>();
        for (String[] row : data) {
            List<String> rowData = new ArrayList<>();
            for (String value : row) {
                // Remove any commas from the value if it's a number
                if (value.matches("^\\d+(\\.\\d+)?$")) {
                    value = value.replace(",", "");
                }
                rowData.add(value);
            }
            dataList.add(rowData);
        }

        StatsCalculator statsCalculator = new StatsCalculator(dataList);
        List<StatsCalculator.ColumnStats> statsList = statsCalculator.calculateStats();

        // Create a new SummaryGenerator instance and generate a summary markdown file
        SummaryGenerator summaryGenerator = new SummaryGenerator(outputFilePath);
        try {
            summaryGenerator.generateSummary(statsList, columnHeadings);
        } catch (IOException e) {
            System.out.println("Error writing output file: " + e.getMessage());
            System.exit(1);
        }

        // Close the scanner
        scanner.close();
    }
}
