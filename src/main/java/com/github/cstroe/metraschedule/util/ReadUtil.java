package com.github.cstroe.metraschedule.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        return lines;
    }
}
