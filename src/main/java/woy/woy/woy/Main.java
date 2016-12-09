package woy.woy.woy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ClassSearcher searcher = new ClassSearcher();

        String[] classNames = new String[]{
                "Class1.java",
                "HSdhdHHs.java",
                "iorvk.java",
                "lsdfllcoos.java",
                "PSPfof.java",
                "Class1pppwoe.java",
        };

//        long[] modDates = new long[]{
//                2L,
//                1L,
//                1L,
//                1L,
//                1L,
//                1L
//        };

        BufferedReader namesReader = new BufferedReader(new FileReader(Main.class.getResource("/classNames.txt").getFile()));
        BufferedReader datesReader = new BufferedReader(new FileReader(Main.class.getResource("/modDates.txt").getFile()));

        List<String> list = namesReader.lines().collect(Collectors.toList());
        List<Long> list1 = datesReader.lines().map(Long::parseLong).collect(Collectors.toList());

        classNames = list.toArray(new String[list.size()]);
        final int[] i = {0};

        final long[] modDates = new long[list1.size()];
        list1.forEach(l -> modDates[i[0]++] = l);

        searcher.refresh(classNames, modDates);

        long start = System.nanoTime();
        String[] result = searcher.guess("Class");
        long end = System.nanoTime();

        System.out.println(Arrays.toString(result));
        System.out.println((end - start) / 1000000);

    }
}
