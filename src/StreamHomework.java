import java.util.*;
import java.util.stream.*;

public class StreamHomework {
    public static void main(String[] args) {
        // Task1 test
        List<String> names = List.of(
                "Ivan", "Petro", "Mikola", "Oleksandr", "Stepan", "Roman", "Volodumur", "Anton",
                "Dmutro", "Artem", "Evgen", "Boris"
        );
        System.out.println(oddNamesJoiner(names));
        printDelimiter();

        // Task2 test
        for (String name : toUpperCaseInReverseOrder(names)) {
            System.out.println(name);
        }
        printDelimiter();

        // Task3 test
        String[] array = {"1, 2, 0", "4, 5"};
        fromArrayNumberPrinter(array);
        printDelimiter();

        // Task4 test
        long a = 25214903917L;
        long c = 11L;
        long m = 2 << 48;
        streamOfRandomNumber(a, c, m)
                .limit(10)
                .forEach(System.out::println);
        printDelimiter();

        // Task5 test
        Stream<String> names1 = Stream.of("Ivan", "Petro", "Mikola", "Oleksandr", "Stepan");
        Stream<String> names2 = Stream.of("Ganna", "Iruna", "Olesya");
        zip(names1, names2).forEach(System.out::println);
        printDelimiter();
    }

    private static void printDelimiter() {
        System.out.println("===========================================================");
        System.out.println();
    }

    // Task 1
    public static String oddNamesJoiner(List<String> names) {
        return IntStream
                .range(0, names.size())
                .filter(n -> n % 2 != 0)
                .mapToObj(n -> n + ". " + names.get(n))
                .collect(Collectors.joining(", "));
    }

    // Task 2
    public static List<String> toUpperCaseInReverseOrder(List<String> names) {
        return names.stream()
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    // Task 3
    public static void fromArrayNumberPrinter(String[] numArray) {
        String result = Arrays.stream(numArray)
                .flatMap(nums -> Stream.of(nums.split(", ")))
                .mapToInt(Integer::valueOf)
                .sorted()
                .mapToObj(Objects::toString)
                .collect(Collectors.joining(", "));

        System.out.println(result);
    }

    // Task 4
    public static Stream<Long> streamOfRandomNumber(long a, long c, long m) {
        return LongStream
                .iterate(1, (element) -> (a * element + c) % m)
                .skip(1)
                .boxed();
    }

    // Task 5
    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        final Iterator<T> firstIterator = first.iterator();
        final Iterator<T> secondIterator = second.iterator();
        final Iterator<T> commonIterator = new Iterator<T>() {
            private boolean takeFromFirstIterator = true;

            @Override
            public boolean hasNext() {
                return firstIterator.hasNext() && secondIterator.hasNext();
            }

            @Override
            public T next() {
                if (takeFromFirstIterator) {
                    takeFromFirstIterator = false;
                    return firstIterator.next();
                } else {
                    takeFromFirstIterator = true;
                    return secondIterator.next();
                }
            }
        };

        final boolean parallel = first.isParallel() || second.isParallel();
        final Iterable<T> iterable = () -> commonIterator;

        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
}
