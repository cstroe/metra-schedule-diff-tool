package com.github.cstroe.metraschedule.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadUtil {
    public static List<String> readFile(String path) throws FileNotFoundException {
        Scanner input = new Scanner(new File(path));
        input.useDelimiter("\\n");
        List<String> lines = new ArrayList<>();
        while(input.hasNext()) {
            String line = input.next();
            if(line.trim().length() != 0) {
                lines.add(line);
            }
        }
        input.close();
        return lines;
    }

    public static String readWholeFile(String path) throws FileNotFoundException {
        List<String> file = readFile(path);
        return file.stream().collect(Collectors.joining("\n"));
    }
}
