import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        test();
    }
    public static void test()
    {
        testIndexingTime(HashingMode.SUM, ProbingMode.DOUBLE, 0.5);

//        MenagamentSystem ms = new MenagamentSystem("supermarket_dataset_5.csv", HashingMode.SUM, ProbingMode.LINEAR, 0.8);
//        testSearchingTime(ms);
//        testRemove(ms);
//        testDisplay(ms);

    }
    public static void testIndexingTime(HashingMode hashingMode, ProbingMode probingMode, double loadFactor)
    {
        long startReadingTime = System.nanoTime();
        MenagamentSystem ms = new MenagamentSystem("supermarket_dataset_5.csv", hashingMode, probingMode, loadFactor);
        long endReadingTime = System.nanoTime();
        long readingTime = endReadingTime - startReadingTime;
        System.out.println("Dosya okuma süresi: " + readingTime / 1_000_000 + " ms");
        System.out.println("Collision count : " + ms.getCollCount());
    }

    public static void testSearchingTime(MenagamentSystem ms)
    {
        long minTime = Integer.MAX_VALUE;
        long maxTime = Integer.MIN_VALUE;
        long startTime = System.nanoTime();

        try (Scanner scanner = new Scanner(new File("customer_1K.txt"))) {
            while (scanner.hasNextLine()) {
                String id = scanner.nextLine().trim();
                long startSearchingTime = System.nanoTime();
                ms.get(id);
                long endSearchingTime = System.nanoTime();
                long searhingTime = endSearchingTime - startSearchingTime;

                if(searhingTime < minTime)
                    minTime = searhingTime;
                if(searhingTime > maxTime)
                    maxTime = searhingTime;

            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        long averageSearchingTime = (endTime - startTime) / 1_000;

        System.out.printf("Average Searching Time: %d micro s, Max Searching Time: %d micro s, Min Searching Time: %d micro s",
                averageSearchingTime / 1_000, maxTime / 1_000, minTime / 1_000);
    }
    public static void testRemove(MenagamentSystem ms)
    {
        ms.display();
        Scanner kb = new Scanner(System.in);
        System.out.print("if you want to quick, press -1: ");
        while(Integer.parseInt(kb.nextLine()) != -1) {
            System.out.print("Enter a user id : ");
            String id = kb.nextLine();
            Customer customer = ms.remove(id.trim());
            if (Objects.nonNull(customer))
                System.out.println(customer.toString());
            System.out.print("if you want to quit, press -1: ");
        }
        ms.display();
    }
    public static void testDisplay(MenagamentSystem ms)
    {
        ms.display();
    }
}