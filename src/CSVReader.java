import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private String fileName;
    private String delimiter;

    public CSVReader(String fileName, String delimiter) {
        this.fileName = fileName;
        this.delimiter = delimiter;
    }

    public List<String[]> read() throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                data.add(values);
            }
        }
        return data;
    }
}
