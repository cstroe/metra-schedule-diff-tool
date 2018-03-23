import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.String.format;

public class TrainEntry {
    private static Scanner input = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) throws IOException {
        while(true) {
            System.out.print("Enter a train number: ");
            Optional<Integer> trainNumberOpt = getTrainNumber();
            if (trainNumberOpt.isPresent()) {
                Integer trainNumber = trainNumberOpt.get();
                if (trainNumber % 2 == 0) {
                    List<StationTime> times = getInboundTimes(trainNumber);
                    saveTimes(trainNumber, times);
                } else {
                    getOutboundTimes(trainNumber);
                }
            } else {
                break;
            }
        }
    }

    private static Optional<Integer> getTrainNumber() {
        String trainNumberRaw = input.next();
        try {
            return Optional.of(Integer.parseInt(trainNumberRaw));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static List<StationTime> getInboundTimes(Integer trainNumber) throws IOException {
        System.out.println("Reading times for inbound train number " + trainNumber);

        Reader in = new FileReader("schedules/bnsf/bnsf-stations-inbound.csv");
        Iterable<CSVRecord> records = CSVFormat.RFC4180
                .withFirstRecordAsHeader()
                .parse(in);
        List<Station> stations = new ArrayList<>();
        for (CSVRecord record : records) {
            stations.add(Station.of(record.get(0), record.get(1)));
        }

        List<StationTime> times = new ArrayList<>();
        StationIterator iterator = new StationIterator(stations);
        StationTime previousStation = null;
        loop: while(iterator.hasNext()) {
            Station currentStation = iterator.next();
            Pair<Input, StationTime> input = getInput(trainNumber, currentStation, previousStation);

            switch(input.getKey()) {
                case COMMAND_SKIP:
                    System.out.println("Skip.");
                    continue loop;
                case COMMAND_GOTO_END:
                    System.out.println("Goto end.");
                    iterator.skipToLast();
                    continue loop;
                case VALUE:
                    StationTime currentStationTime = input.getValue();
                    System.out.print(trainNumber);
                    System.out.print(" -> ");
                    System.out.println(currentStationTime);
                    times.add(currentStationTime);
                    previousStation = currentStationTime;
                    break;
            }
        }

        return times;
    }

    private static Pair<Input, StationTime> getInput(int trainNumber, Station station, StationTime previousStation) {
        System.out.print(trainNumber);
        System.out.print(" -> ");
        System.out.print(station.name);
        System.out.print(": ");

        String inputRaw = input.next();

        if("-".equals(inputRaw)) {
            return new Pair<>(Input.COMMAND_SKIP, null);
        }

        if(".".equals(inputRaw)) {
            return new Pair<>(Input.COMMAND_GOTO_END, null);
        }

        TimeParserResult result;
        if(previousStation != null) {
            result = new TimeParser().parse(previousStation.time, inputRaw);
        } else {
            result = new TimeParser().parse(inputRaw);
        }
        if(result.hasError()) {
            return getInput(trainNumber, station, previousStation);
        } else {
            return new Pair<>(Input.VALUE, StationTime.of(station, result.getValue()));
        }
    }

    private static void getOutboundTimes(Integer trainNumber) {
        System.out.println("Reading times for outbound train number " + trainNumber);
    }

    private static void saveTimes(int trainNumber, List<StationTime> times) throws IOException {
        String fileName = format("schedules/bnsf/2016-10-09/%d.csv", trainNumber);
        System.out.println(format("Saving file: %s", fileName));
        FileWriter writer = new FileWriter(fileName);

        CSVPrinter printer = CSVFormat.DEFAULT
                .withHeader("station_id","station_name", "time")
                .withQuoteMode(QuoteMode.NON_NUMERIC)
                .print(writer);
        for(StationTime stationTime : times) {
            printer.printRecord(stationTime.station.id, stationTime.station.name, stationTime.time.toString());
        }
        printer.close();
    }
}
