import com.github.cstroe.metraschedule.domain.Station;
import com.github.cstroe.metraschedule.domain.Time;
import com.github.cstroe.metraschedule.domain.TrainDirection;
import com.github.cstroe.metraschedule.parser.TimeDifference;
import com.github.cstroe.metraschedule.parser.TimeParser;
import com.github.cstroe.metraschedule.util.ReadUtil;
import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ScheduleDiff {
    private static Scanner input = new Scanner(System.in).useDelimiter("\n");
    private static final TimeParser parser = new TimeParser();

    public static void main(String[] args) throws IOException {
        final String line = "bnsf";
        final String currentSchedule = "2016-10-09";
        final String proposedSchedule = "2018-03-proposed-schedule";

        for(TrainDirection direction : TrainDirection.values()) {
            System.out.println(format("Computing diff between '%s' on '%s' on the %s '%s' line.", currentSchedule, proposedSchedule, direction.name().toLowerCase(), line));
            computeDiff(line, currentSchedule, proposedSchedule, direction);
        }
    }

    private static void computeDiff(String line, String currentSchedule, String proposedSchedule, TrainDirection direction) throws IOException {
        List<String> trainNumbers = ReadUtil.readFile(format("schedules/%s/%s-trains.txt", line, direction.name().toLowerCase()));

        List<Station> stations = readStations(line, direction);

        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");
        builder.append(format("<h1>%s line: %s schedule compared with the %s</h1>", line, currentSchedule, proposedSchedule));
        builder.append(format("<h1>%s train schedule comparison</h1>", direction.name()));
        builder.append(ReadUtil.readWholeFile("src/main/resources/tooltip.html"));
        builder.append("<table border=1 cellspacing=0>\n");
        builder.append("<tr><td>&nbsp;</td>");
        for(String train : trainNumbers) {
            builder.append("<td>");
            builder.append(train);
            builder.append("</td>");
        }
        builder.append("<td>Stops</td>");
        builder.append("<td>&nbsp;</td>");
        builder.append("</tr>");

        int currentTotalStops = 0;
        int proposedTotalStops = 0;
        for(Station station : stations) {
            builder.append("<tr><td>");
            builder.append(station.name.replaceAll("\\s", "&nbsp;"));
            builder.append("</td>");

            int currentStationStops = 0;
            int proposedStationStops = 0;
            for(String train : trainNumbers) {
                Map<Station, Time> currentTrainStops = readSchedule(currentSchedule, train);
                Map<Station, Time> proposedTrainStops = readSchedule(proposedSchedule, train);
                Time currentTime = currentTrainStops.get(station);
                Time proposedTime = proposedTrainStops.get(station);

                if(currentTime == null && proposedTime != null) {
                    builder.append("<td style=\"background-color: lime;\">");
                    builder.append(proposedTime.toStringTimeOnly());
                    builder.append("</td>");
                    proposedStationStops++;
                }

                if(currentTime != null && proposedTime == null) {
                    builder.append("<td style=\"background-color: red;\">");
                    builder.append(currentTime.toStringTimeOnly());
                    builder.append("</td>");
                    currentStationStops++;
                }

                if(currentTime == null && proposedTime == null) {
                    builder.append("<td style=\"background-color: gray;\">&nbsp;</td>");
                }

                if(currentTime != null && proposedTime != null) {
                    if(currentTime.equals(proposedTime)) {
                        builder.append("<td>");
                        builder.append(currentTime.toStringTimeOnly());
                        builder.append("</td>");
                    } else {
                        builder.append("<td style=\"background-color: yellow;\">");
                        builder.append("<div class=\"tooltip\">");
                        builder.append(TimeDifference.diff(currentTime, proposedTime));
                        builder.append("<span class=\"tooltiptext\">");
                        builder.append(currentTime.toString().replaceAll("\\s", "&nbsp;"));
                        builder.append("&nbsp&rarr;&nbsp;");
                        builder.append(proposedTime.toString().replaceAll("\\s", "&nbsp;"));
                        builder.append("</span></div><br/>");
                        builder.append(proposedTime.toStringTimeOnly());
                        builder.append("</td>");
                    }
                    currentStationStops++;
                    proposedStationStops++;
                }

            }
            builder.append("<td style=\"text-align: center;\">");
            builder.append(proposedStationStops - currentStationStops);
            builder.append("</td>");

            builder.append("<td>");
            builder.append(station.name.replaceAll("\\s", "&nbsp;"));
            builder.append("</td>");


            builder.append("</tr>");
            currentTotalStops += currentStationStops;
            proposedTotalStops += proposedStationStops;
        }

        builder.append("<tr><td colspan=\"48\">&nbsp;</td><td style=\"text-align: center;\">");
        builder.append(proposedTotalStops - currentTotalStops);
        builder.append("</td></tr>");
        builder.append("</table><br/>");
        builder.append(ReadUtil.readWholeFile("src/main/resources/legend.html"));
        builder.append("</body></html>\n");
        saveHtmlReport(builder, direction);
    }

    private static List<Station> readStations(String line, TrainDirection direction) throws IOException {
        return CSVFormat.DEFAULT
                .withHeader("id","station", "zone")
                .withSkipHeaderRecord()
                .withQuoteMode(QuoteMode.NON_NUMERIC)
                .withTrim()
                .withQuote('"')
                .parse(new FileReader(format("schedules/%s/bnsf-stations-%s.csv", line, direction)))
                .getRecords().stream()
                .map(e -> Station.of(e.get("id"), e.get("station")))
                .collect(Collectors.toList());
    }

    private static Map<Station, Time> readSchedule(String schedule, String train) throws IOException {
        return CSVFormat.DEFAULT
                .withHeader("station_id","station_name", "time")
                .withSkipHeaderRecord()
                .withQuoteMode(QuoteMode.NON_NUMERIC)
                .withTrim()
                .withQuote('"')
                .parse(new FileReader(format("schedules/bnsf/%s/%s.csv", schedule, train)))
                .getRecords().stream()
                .map(e -> {
                    Station station = Station.of(e.get("station_id"), e.get("station_name"));
                    String timeRaw = e.get("time");
                    Time time = parser.parse(timeRaw).getValue();
                    return new Pair<>(station, time);
                }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static void saveHtmlReport(StringBuilder builder, TrainDirection direction) throws IOException {
        final String file = format("target/%s-diff.html", direction.name().toLowerCase());
        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);

        bw.write(builder.toString());
        bw.flush();
        bw.close();
        writer.close();
        System.out.println(format("Saved %s", file));
    }
}
