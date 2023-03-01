import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsCalculator {
    private List<List<String>> data;

    public StatsCalculator(List<List<String>> data) {
        this.data = data;
    }

    public List<ColumnStats> calculateStats() {
        List<ColumnStats> statsList = new ArrayList<>();

        // Transpose the data
        List<List<String>> transposedData = CSVUtils.transpose(data);

        // Calculate the statistics for each column
        for (int i = 0; i < transposedData.size(); i++) {
            List<String> column = transposedData.get(i);

            boolean isNumeric = true;
            boolean isString = true;
            double sum = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            // Calculate the statistics for columns containing numeric values
            for (int j = 1; j < column.size(); j++) { // start iterating from the second row
                String value = column.get(j);
                try {
                    double number = Double.parseDouble(value);
                    sum += number;
                    if (number < min) {
                        min = number;
                    }
                    if (number > max) {
                        max = number;
                    }
                } catch (NumberFormatException e) {
                    isNumeric = false;
                    break;
                }
            }

            // Sort the columns containing string values
            if (!isNumeric) {
                for (int j = 1; j < column.size(); j++) { // start iterating from the second row
                    String value = column.get(j);
                    try {
                        Double.parseDouble(value);
                        isString = false;
                        break;
                    } catch (NumberFormatException e) {
                    }
                }
                if (isString) {
                    Collections.sort(column.subList(1, column.size()), String.CASE_INSENSITIVE_ORDER);
                    if (column.size() > 11) {
                        column = column.subList(0, 11);
                    }
                }
            }

            // Add the statistics to the stats list
            if (isNumeric) {
                double mean = sum / (column.size() - 1); // subtract 1 to exclude the header row
                statsList.add(new ColumnStats(i, min, max, sum, mean, true, null));
            } else {
                statsList.add(new ColumnStats(i, column.subList(1, column.size()), false)); // start from the second row
            }
        }

        return statsList;
    }

    public static class ColumnStats {
        private int columnIndex;
        private Double minValue;
        private Double maxValue;
        private Double sumValue;
        private Double meanValue;
        private boolean isNumeric;
        private List<String> sortedValues;

        public ColumnStats(int columnIndex, Double minValue, Double maxValue, Double sumValue, Double meanValue,
                boolean isNumeric, List<String> sortedValues) {
            this.columnIndex = columnIndex;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.sumValue = sumValue;
            this.meanValue = meanValue;
            this.isNumeric = true;
            this.sortedValues = null;
        }

        public ColumnStats(int columnIndex, List<String> sortedValues, boolean isNumeric) {
            this.columnIndex = columnIndex;
            this.minValue = null;
            this.maxValue = null;
            this.sumValue = null;
            this.meanValue = null;
            this.isNumeric = false;
            this.sortedValues = sortedValues;
        }

        public boolean isNumeric() {
            return isNumeric;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public Double getMinValue() {
            return minValue;
        }

        public Double getMaxValue() {
            return maxValue;
        }

        public Double getSumValue() {
            return sumValue;
        }

        public Double getMeanValue() {
            return meanValue;
        }

        public List<String> getSortedValues() {
            return sortedValues;
        }
    }
}
