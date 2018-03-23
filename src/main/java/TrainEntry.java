import java.util.Scanner;

public class TrainEntry {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in).useDelimiter("\n");
        String string = input.next();
        if ("hello".equals(string)) {
            System.out.println("world!");
        }
    }
}
