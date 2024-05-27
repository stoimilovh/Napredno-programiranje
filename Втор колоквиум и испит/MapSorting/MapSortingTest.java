//Да се напише генеричка метода entriesSortedByValues за сортирање на елементи
//        (парови од клуч и вредност) на една мапа според вредноста во опаѓачки редослед.
//        Доколку постојат две или повеќе исти вредности, да се задржи редоследот дефиниран
//        во мапата. Сортираните елементи на мапата да бидат да бидат вратени како SortedSet<Map.Entry<, >>.

import java.util.*;
import java.util.stream.Collectors;

public class MapSortingTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        List<String> l = readMapPairs(scanner);
        if(n==1){
            Map<String, Integer> map = new HashMap<>();
            fillStringIntegerMap(l, map);
            SortedSet<Map.Entry<String, Integer>> s = entriesSortedByValues(map);
            System.out.println(s);
        } else {
            Map<Integer, String> map = new HashMap<>();
            fillIntegerStringMap(l, map);
            SortedSet<Map.Entry<Integer, String>> s = entriesSortedByValues(map);
            System.out.println(s);
        }

    }

    private static <K extends Comparable<K>, V extends Comparable<V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        System.out.println(map);
        Comparator<Map.Entry<K, V>>comparator=(e1, e2) -> {
            int res=e2.getValue().compareTo(e1.getValue());
            if(res!=0){
                return res;
            }
            else {
                return 1;
            }
        };
        return map.entrySet().stream().collect(Collectors.toCollection(()-> new TreeSet<>(comparator)));
    }

    private static List<String> readMapPairs(Scanner scanner) {
        String line = scanner.nextLine();
        String[] entries = line.split("\\s+");
        return Arrays.asList(entries);
    }

    static void fillStringIntegerMap(List<String> l, Map<String,Integer> map) {
        l.stream()
                .forEach(s -> map.put(s.substring(0, s.indexOf(':')), Integer.parseInt(s.substring(s.indexOf(':') + 1))));
    }

    static void fillIntegerStringMap(List<String> l, Map<Integer, String> map) {
        l.stream()
                .forEach(s -> map.put(Integer.parseInt(s.substring(0, s.indexOf(':'))), s.substring(s.indexOf(':') + 1)));
    }

}