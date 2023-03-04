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
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            int count = 0;

            // Calculate the statistics for columns containing numeric values
            for (String value : column) {
                if (!value.equalsIgnoreCase("N/A") && !value.isEmpty()) {
                    try {
                        double number = Double.parseDouble(value.replace(",", ""));
                        sum += number;
                        if (number < min) {
                            min = number;
                        }
                        if (number > max) {
                            max = number;
                        }
                        count++;
                    } catch (NumberFormatException e) {
                        isNumeric = false;
                        break;
                    }
                }
            }

            // Sort the columns containing string values
            if (!isNumeric) {
                for (String value : column) {
                    try {
                        Double.parseDouble(value.replace(",", ""));
                        isString = false;
                        break;
                    } catch (NumberFormatException e) {
                    }
                }
                if (isString) {
                    Collections.sort(column, String.CASE_INSENSITIVE_ORDER);
                    if (column.size() > 10) {
                        column = column.subList(0, 10);
                    }
                }
            }

            // Add the statistics to the stats list
            if (isNumeric) {
                double mean = count > 0 ? sum / count : 0;
                statsList.add(new ColumnStats(i, min, max, sum, mean, true, null));
            } else {
                statsList.add(new ColumnStats(i, column, false));
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
