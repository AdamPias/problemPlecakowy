import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class main {
    static int bestValue = 0;
    static int bestWeight = 0;
    static List<Item> bestItems = new ArrayList<>();
    static long checkedCombinations = 0;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("plecak.txt");
        Fileloader loader = new Fileloader(file);

        Random random = new Random();
        int datasetIndex = random.nextInt(15);
        DataSet chosenDataset = loader.dataSets.get(datasetIndex);

        System.out.println("Wylosowano zestaw danych nr: " + (datasetIndex + 1));
        System.out.println("Pojemnosc plecaka: " + loader.capacity);
        System.out.println("---");

        List<Item> items = new ArrayList<>();
        for (int i = 0; i < loader.length; i++) {
            items.add(new Item(i + 1, chosenDataset.getSizes()[i], chosenDataset.getVals()[i]));
        }

        long startTimeBF = System.nanoTime();

        solveBruteForce(items, loader.capacity, 0, new ArrayList<>(), 0, 0);

        long endTimeBF = System.nanoTime();
        double timeBFInSeconds = (endTimeBF - startTimeBF) / 1_000_000_000.0;

        System.out.println("=== ALGORYTM BRUTALNEJ SIŁY ===");
        System.out.println("Skład plecaka [nr, rozmiar, wartość]: " + bestItems);
        System.out.println("Całkowita wartość: " + bestValue);
        System.out.println("Całkowita zajęta pojemność: " + bestWeight);
        System.out.println("Sprawdzonych kombinacji: " + checkedCombinations);
        System.out.printf("Czas wykonania: %.5f sekund\n", timeBFInSeconds);
        System.out.println("---");
//----------------------------------------
        System.out.println("=== ALGORYTM HEURYSTYCZNY ===");
        long startTimeH = System.nanoTime();

        solveHeuristic(items, loader.capacity);

        long endTimeH = System.nanoTime();
        double timeHInSeconds = (endTimeH - startTimeH) / 1_000_000_000.0;


        System.out.printf("Czas wykonania: %.5f sekund\n", timeHInSeconds);
    }

    public static void solveBruteForce(List<Item> items, int maxCapacity, int index,
                                       List<Item> currentItems, int currentWeight, int currentValue) {

        // koniec
        if (index == items.size()) {
            checkedCombinations++;
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestWeight = currentWeight;
                bestItems = new ArrayList<>(currentItems);
            }
            return;
        }

        solveBruteForce(items, maxCapacity, index + 1, currentItems, currentWeight, currentValue);

        Item currentItem = items.get(index);
        if (currentWeight + currentItem.size <= maxCapacity) {

            currentItems.add(currentItem);

            solveBruteForce(items, maxCapacity, index + 1, currentItems, currentWeight + currentItem.size, currentValue + currentItem.value);

            currentItems.remove(currentItems.size() - 1);
        }
    }

    public static void solveHeuristic(List<Item> items, int maxCapacity) {
        List<Item> sortedItems = new ArrayList<>(items);

        sortedItems.sort((a, b) -> {
            double ratioA = (double) a.value / a.size;
            double ratioB = (double) b.value / b.size;
            return Double.compare(ratioB, ratioA);
        });

        int heuristicWeight = 0;
        int heuristicValue = 0;
        List<Item> heuristicItems = new ArrayList<>();

        for (Item item : sortedItems) {
            if (heuristicWeight + item.size <= maxCapacity) {
                heuristicItems.add(item);
                heuristicWeight += item.size;
                heuristicValue += item.value;
            }
        }

        System.out.println("Skład plecaka [nr, rozmiar, wartość]: " + heuristicItems);
        System.out.println("Całkowita wartość: " + heuristicValue);
        System.out.println("Całkowita zajęta pojemność: " + heuristicWeight);
    }
}