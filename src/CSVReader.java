import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private String filePath;
    private String delimiter;

    public CSVReader(String filePath, String delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    public List<String[]> read() throws IOException {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("\"", "").trim();
                }
                data.add(values);
            }
        }

        return data;
    }
}
