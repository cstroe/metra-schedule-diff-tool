import com.github.cstroe.metraschedule.cli.Input;
import com.github.cstroe.metraschedule.domain.Station;
import com.github.cstroe.metraschedule.domain.StationTime;
import com.github.cstroe.metraschedule.domain.Train;
import com.github.cstroe.metraschedule.domain.TrainDirection;
import com.github.cstroe.metraschedule.parser.TimeParser;
import com.github.cstroe.metraschedule.parser.TimeParserResult;
import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.*;

import static java.lang.String.format;

public class TrainEntry {
    private static Scanner input = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) throws IOException {
        String line = select("Please select your Metra line", getSubDirectories("schedules"));
        System.out.println("Selected Metra line: " + line);
        String schedule = select("Please select a schedule to edit", getSubDirectories("schedules" + File.separator + line));
        System.out.println("Editing the following schedule: " + schedule);
        while(true) {
            System.out.println(format("Line: %s, Schedule: %s", line, schedule));
            System.out.print("Enter a train number: ");
            Optional<Integer> trainNumberOpt = readInteger();
            if (trainNumberOpt.isPresent()) {
                Integer trainNumber = trainNumberOpt.get();

                Train train;
                if (trainNumber % 2 == 0) {
                    train = new Train(trainNumber, TrainDirection.INBOUND);
                } else {
                    train = new Train(trainNumber, TrainDirection.OUTBOUND);
                }

                List<StationTime> times = getTrainTimes(line, train);
                saveTimes(line, schedule, trainNumber, times);
            } else {
                break;
            }
        }
    }

    private static String select(String prompt, List<String> options) {
        for(int i = 0; i < options.size(); i++) {
            String currentOption = options.get(i);
            System.out.println(i + ": " + currentOption);
        }
        while(true) {
            System.out.print(prompt + ": ");
            Optional<Integer> valueOpt = readInteger();
            if(valueOpt.isPresent()) {
                Integer value = valueOpt.get();
                if(value >= 0 && value < options.size()) {
                    return options.get(value);
                }
            }
        }
    }

    private static List<String> getSubDirectories(String parentDir) {
        File schedulesDir = new File(parentDir);
        if(!schedulesDir.isDirectory()) {
            throw new IllegalArgumentException("Cannot find the schedules directory.");
        }

        String[] subDirectories = schedulesDir.list((dir, name) -> {
            try {
                File currentFile = new File(dir.getCanonicalPath() + File.separator + name);
                return currentFile.isDirectory();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });

        return Arrays.asList(subDirectories);
    }

    private static Optional<Integer> readInteger() {
        String trainNumberRaw = input.next();
        try {
            return Optional.of(Integer.parseInt(trainNumberRaw));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static List<Station> readStations(String line, TrainDirection direction) throws IOException {
        Reader in = new FileReader(format("schedules/%s/bnsf-stations-%s.csv", line, direction.toString()));
        Iterable<CSVRecord> records = CSVFormat.RFC4180
                .withFirstRecordAsHeader()
                .parse(in);
        List<Station> stations = new ArrayList<>();
        for (CSVRecord record : records) {
            stations.add(Station.of(record.get(0), record.get(1)));
        }
        return stations;
    }

    private static List<StationTime> getTrainTimes(String line, Train train) throws IOException {
        System.out.println("Reading times for train " + train);

        List<Station> stations = readStations(line, train.direction);
        LinkedList<StationTime> times = new LinkedList<>();
        StationIterator iterator = new StationIterator(stations);
        StationTime previousStation = null;
        loop: while(iterator.hasNext()) {
            Station currentStation = iterator.next();
            Pair<Input, Object> input = getInput(train.number, currentStation, previousStation);

            switch(input.getKey()) {
                case COMMAND_SKIP:
                    System.out.println("Skip.");
                    continue loop;
                case COMMAND_GOTO_END:
                    System.out.println("Goto end.");
                    iterator.skipToLast();
                    continue loop;
                case COMMAND_UNDO:
                    StationTime time = times.removeLast();
                    System.out.println("Undo: " + time);
                    iterator.goBackTo(time.station.id);
                    continue loop;
                case COMMAND_JUMP:
                    String newStation = (String) input.getValue();
                    Station jumpedStation = iterator.jumpTo(newStation);
                    System.out.println("Jump to: " + jumpedStation);
                    continue loop;
                case VALUE:
                    StationTime currentStationTime = (StationTime) input.getValue();
                    System.out.print(train.number);
                    System.out.print(" -> ");
                    System.out.println(currentStationTime);
                    times.add(currentStationTime);
                    previousStation = currentStationTime;
                    break;
            }
        }

        return times;
    }

    private static Pair<Input, Object> getInput(int trainNumber, Station station, StationTime previousStation) {
        System.out.print(trainNumber);
        System.out.print(" -> ");
        System.out.print(station.name);
        System.out.print(": ");

        String inputRaw = input.next();

        if(inputRaw == null) {
            return getInput(trainNumber, station, previousStation);
        }

        if("-".equals(inputRaw)) {
            return new Pair<>(Input.COMMAND_SKIP, null);
        }

        if(".".equals(inputRaw)) {
            return new Pair<>(Input.COMMAND_GOTO_END, null);
        }

        if("u".equals(inputRaw)) {
            return new Pair<>(Input.COMMAND_UNDO, null);
        }

        if(inputRaw.startsWith("=")) {
            String newStation = inputRaw.substring(1);
            return new Pair<>(Input.COMMAND_JUMP, newStation);
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

    private static void saveTimes(String line, String schedule, int trainNumber, List<StationTime> times) throws IOException {
        String fileName = format("schedules/%s/%s/%d.csv", line, schedule, trainNumber);
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
