import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args)
    {
        Metro parisMetro = new Metro("Paris_RER_Metro_v2.csv");
        System.out.println(parisMetro.getStationCount());
        parisMetro.findDirection();
 //       test(parisMetro);
//        parisMetro.findDirection("Avron","Rome", 0);
//        parisMetro.findDirection("Avron","Rome", 1);
//        parisMetro.findDirection("Bercy","Pigalle", 0);
//        parisMetro.findDirection("Bercy","Pigalle", 1);
    }

    public static void test(Metro parisMetro)
    {
        long startTime = System.nanoTime();
        try (Scanner scanner = new Scanner(new File("Test100.csv"))){
            scanner.nextLine();

            String origin, destination;
            int preference;

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");

                origin = line[0];
                destination = line[1];
                preference = Integer.parseInt(line[2]);
                System.out.println("Origin: " + origin + ", Destination: " + destination + ", Preference: " + preference);
                parisMetro.findDirection(origin, destination, preference);
                System.out.println("--------------");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        double averageTime = (endTime - startTime) / 100;
        System.out.println("Average Time : " + averageTime/ 1_000_000 + " ms");

    }

}