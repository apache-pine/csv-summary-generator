import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SummaryGenerator {
    private String filePath;

    public SummaryGenerator(String filePath) {
        this.filePath = filePath;
    }

    public void generateSummary(List<StatsCalculator.ColumnStats> statsList, String[] columnHeadings)
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("# Summary Report\n\n");

            // Numeric statistics
            writer.write("## Numeric Statistics\n\n");
            writer.write("| Column | Heading | Min | Max | Sum | Mean |\n");
            writer.write("| --- | --- | --- | --- | --- | --- |\n");
            for (StatsCalculator.ColumnStats stats : statsList) {
                if (stats.isNumeric()) {
                    String min = stats.getMinValue() == null ? "N/A" : Double.toString(stats.getMinValue());
                    String max = stats.getMaxValue() == null ? "N/A" : Double.toString(stats.getMaxValue());
                    String sum = stats.getSumValue() == null ? "N/A" : Double.toString(stats.getSumValue());
                    String mean = stats.getMeanValue() == null ? "N/A" : Double.toString(stats.getMeanValue());
                    writer.write(String.format("| %d | %s | %s | %s | %s | %s |\n",
                            stats.getColumnIndex(), columnHeadings[stats.getColumnIndex()], min, max, sum, mean));
                }
            }
            writer.write("\n");

            // String statistics
            writer.write("## String Statistics\n\n");
            for (StatsCalculator.ColumnStats stats : statsList) {
                if (!stats.isNumeric()) {
                    writer.write(String.format("### Column %d - %s\n\n", stats.getColumnIndex(),
                            columnHeadings[stats.getColumnIndex()]));
                    writer.write("| Top Values |\n");
                    writer.write("| --- |\n");
                    for (String value : stats.getSortedValues()) {
                        writer.write(String.format("| %s |\n", value));
                    }
                    writer.write("\n");
                }
            }
        }
    }
}
