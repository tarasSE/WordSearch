package woy.woy.woy;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class ClassSearcher implements ISearcher {

    @Getter
    private String indexedNames;
    @Getter
    private int filesTotal;
    @Getter
    private String[] classNames;
    @Getter
    private long[] modDates;

    public void refresh(String[] classNames, long[] modificationDates) {
        StringBuilder builder = new StringBuilder();

        if (classNames.length != modificationDates.length)
            throw new RuntimeException("Class names count should be quals to modification dates count");

        for (int i = 0; i < classNames.length; i++) {
            builder
                    .append("№").append(i).append("№")
                    .append(classNames[i])
                    .append("№№").append(modificationDates[i]).append("№№").append("\n");
        }

        this.indexedNames = builder.toString();
        this.filesTotal = classNames.length;
        this.classNames = classNames;
        this.modDates = modificationDates;
    }

    public String[] guess(String start) {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Long> dates = new ArrayList<>();
        Matcher m = buildPattern(start.toCharArray()).matcher(indexedNames);
        long result = -1L;
        long tmp = 0L;

        while (m.find()) {
            nodes.add(
                    new Node(
                            Integer.parseInt(m.group(1)),
                            Long.parseLong(m.group(3))
                    )
            );

            tmp = modDates[Integer.parseInt(m.group(1))];
            if (result == -1L) {
                result = tmp;
            } else {
                result ^= tmp;
            }

            dates.add(tmp);
        }

        System.out.println(result);

//        long result = dates.stream().reduce((n1, n2) -> n1 ^ n2).get();

        if (result == 0L || result == (long) dates.get(0)) {
            System.out.println("I'm here!");
            String[] r = nodes.stream().map(node -> getClassNames()[node.getId()]).collect(toList()).toArray(new String[nodes.size()]);
            Arrays.sort(r, String::compareTo);
            return r;
        }

        return nodes.stream().sorted(Node::compareTo).map(node -> getClassNames()[node.getId()]).collect(toList()).toArray(new String[nodes.size()]);

    }

    private Pattern buildPattern(char[] chars) {  // todo: Escape special symbols like '$'
        StringBuilder builder = new StringBuilder();

        builder.append("№([\\d]+)№(");

        for (char c : chars) {
            if (Character.isAlphabetic(c)) {
                builder.append("[").append(c).append(negateCase(c)).append("]{1}");
                continue;
            }

            builder.append(c);
        }

        builder.append(".*)№№(.*)№№");
        return Pattern.compile(builder.toString());
    }

    private char negateCase(char c) {
        if (Character.isUpperCase(c))
            return Character.toLowerCase(c);

        return Character.toUpperCase(c);
    }

}
