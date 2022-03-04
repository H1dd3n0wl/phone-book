package by.andrey_zhukov;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static String[] entities;
    private static List<String> newEntities = new ArrayList<>();
    private static Set<String> ent = new HashSet<>();
    private static long creatingSet;
    private static String[] toFind;
    private static long counter;

    public static int compareStrings(String s1, String s2) {
        int l1 = s1.length();
        int l2 = s2.length();
        int len = Math.min(l1, l2);
        for (int i = 0; i < len; ++i) {
            int ch1 = s1.charAt(i);
            int ch2 = s2.charAt(i);
            if (ch1 != ch2) {
                return ch1 - ch2;
            }
        }
        if (l1 != l2) {
            return l1 - l2;
        } else {
            return 0;
        }
    }

    public static AlgorithmTime linearSearchAlgorithm() {
        AlgorithmTime linearSearchTimer = new AlgorithmTime();
        for (String item : toFind) {
            for (String ent : entities) {
                if (Objects.equals(ent.split("\\d+\\s")[1], item)) {
                    counter++;
                    break;
                }
            }
        }
        return linearSearchTimer;
    }

    public static AlgorithmTime bubbleSortAlgorithm(long linerSearchTime) {
        AlgorithmTime bubbleSortTimer = new AlgorithmTime();
        for (int i = 0; i < entities.length; ++i) {
            for (int j = i + 1; j < entities.length; ++j) {
                String s1 = entities[i].split("\\d+\\s")[1];
                String s2 = entities[j].split("\\d+\\s")[1];
                if (compareStrings(s1, s2) > 0) {
                    String t = entities[i];
                    entities[i] = entities[j];
                    entities[j] = t;
                }
                if (bubbleSortTimer.isTimeToStop(linerSearchTime)) {
                    return bubbleSortTimer;
                }
            }
        }
        return bubbleSortTimer;
    }

    public static AlgorithmTime jumpSearchAlgorithm() {
        AlgorithmTime jumpSearchTimer = new AlgorithmTime();
        int step = (int) Math.floor(Math.sqrt(entities.length));
        int index;
        for (String item : toFind) {
            index = step - 1;
            while (index < entities.length &&
                    compareStrings(item, entities[index].split("\\d+\\s")[1]) > 0) {
                if (index + step > entities.length) {
                    index = entities.length - 1;
                    break;
                }
                index += step;
            }
            int border = Math.max(0, index - step);
            while (!Objects.equals(item, entities[index].split("\\d+\\s")[1]) &&
                    index > border) {
                index--;
            }
            if (Objects.equals(item, entities[index].split("\\d+\\s")[1])) {
                counter++;
            }
        }
        return jumpSearchTimer;
    }

    public static AlgorithmTime quickSortAlgorithm() {
        AlgorithmTime quickSortTimer = new AlgorithmTime();
        newEntities.sort((o1, o2) -> {
            String p1 = o1.split("\\d+\\s")[1];
            String p2 = o2.split("\\d+\\s")[1];
            return compareStrings(p1, p2);
        });
        return quickSortTimer;
    }

    public static AlgorithmTime binarySearchAlgorithm() {
        AlgorithmTime binarySearchTimer = new AlgorithmTime();
        for (String item : toFind) {
            int l = 0;
            int r = newEntities.size() - 1;
            while (l <= r) {
                int m = (l + r) / 2;
                if (Objects.equals(newEntities.get(m).split("\\d+\\s")[1], item)) {
                    counter++;
                    break;
                } else if (compareStrings(newEntities.get(m).split("\\d+\\s")[1], item) > 0) {
                    r = m - 1;
                } else {
                    l = m + 1;
                }
            }
        }
        return binarySearchTimer;
    }

    public static AlgorithmTime hashTableAlgorithm() {
        AlgorithmTime hashTableTimer = new AlgorithmTime();
        for (String item : toFind) {
            if (ent.contains(item)) {
                counter++;
            }
        }
        return hashTableTimer;
    }

    public static void initArrays() {
        File directory =
                new File("directory.txt");
        File find = new File("find.txt");
        try {
            entities = Files.lines(Path.of(String.valueOf(directory)))
                    .toArray(String[]::new);
            newEntities = Arrays.asList(entities);
            AlgorithmTime hashTable = new AlgorithmTime();
            ent = newEntities.stream().map(e -> e.split("\\d+\\s")[1]).collect(Collectors.toSet());
            creatingSet = hashTable.getCurrentTime();
            toFind = Files.lines(Path.of(String.valueOf(find)))
                    .toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void process() {
        System.out.println("Start searching (linear search)...");
        counter = 0;
        AlgorithmTime linearSearch = linearSearchAlgorithm();
        long linearSearchTime = linearSearch.getCurrentTime();
        System.out.println("Found " + counter + " / " + toFind.length +
                " entries. Time taken: " + linearSearch);

        System.out.println("\nStart searching (bubble sort + jump search)...");
        counter = 0;
        AlgorithmTime bubbleSort = bubbleSortAlgorithm(linearSearchTime);
        long sortingTime = bubbleSort.getCurrentTime();
        AlgorithmTime curSearch = bubbleSort.isStopped() ?
                linearSearchAlgorithm() : jumpSearchAlgorithm();
        long searchingTime = curSearch.getCurrentTime();
        long generalTime = sortingTime + searchingTime;
        System.out.println("Found " + counter + " / " + toFind.length +
                " entries. Time taken: " + AlgorithmTime.parseTime(generalTime));
        System.out.println("Sorting time: " + AlgorithmTime.parseTime(sortingTime));
        if (bubbleSort.isStopped()) {
            System.out.println(" - STOPPED, moved to linear search");
        }
        System.out.println("Searching time: " + AlgorithmTime.parseTime(searchingTime));

        System.out.println("\nStart searching (quick sort + binary search)...");
        counter = 0;
        AlgorithmTime quickSort = quickSortAlgorithm();
        sortingTime = quickSort.getCurrentTime();
        AlgorithmTime binarySearch = binarySearchAlgorithm();
        searchingTime = binarySearch.getCurrentTime();
        generalTime = sortingTime + searchingTime;
        System.out.println("Found " + counter + " / " + toFind.length +
                " entries. Time taken: " + AlgorithmTime.parseTime(generalTime));
        System.out.println("Sorting time: " + AlgorithmTime.parseTime(sortingTime));
        System.out.println("Searching time: " + AlgorithmTime.parseTime(searchingTime));

        System.out.println("Start searching (hash table)...");
        counter = 0;
        AlgorithmTime hashTable = hashTableAlgorithm();
        searchingTime = hashTable.getCurrentTime();
        generalTime = searchingTime + creatingSet;
        System.out.println("Found " + counter + " / " + toFind.length +
                " entries. Time taken: " + AlgorithmTime.parseTime(generalTime));
        System.out.println("Creating time: " + AlgorithmTime.parseTime(creatingSet));
        System.out.println("Searching time: " + AlgorithmTime.parseTime(searchingTime));
    }

    public static void main(String[] args) {
        initArrays();
        process();
    }
}

