import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    public static <T> List<List<T>> transpose(List<? extends List<? extends T>> data) {
        int width = data.get(0).size();
        int height = data.size();

        List<List<T>> result = new ArrayList<>(width);
        for (int i = 0; i < width; i++) {
            List<T> row = new ArrayList<>(height);
            for (int j = 0; j < height; j++) {
                row.add(null);
            }
            result.add(row);
        }

        for (int i = 0; i < height; i++) {
            List<? extends T> dataRow = data.get(i);
            for (int j = 0; j < width; j++) {
                result.get(j).set(i, dataRow.get(j));
            }
        }

        return result;
    }
}
