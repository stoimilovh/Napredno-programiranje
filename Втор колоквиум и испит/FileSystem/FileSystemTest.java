//Да се имплементира класа FileSystem за едноставен податочен систем. За вашиот податочен систем треба да имплементирате сопствена класа за датотека File со податоци за име (String), големина (Integer) и време на креирање (LocalDateTime) Класата треба да ги овозможува следните функционалности:
//
//public void addFile(char folder, String name, int size, LocalDateTime createdAt) - метод за
//        додавање нова датотека File во фолдер со даденото име (името на фолдерот е еден знак,
//        може да биде . или голема буква)
//public List<File> findAllHiddenFilesWithSizeLessThen(int size) - враќа листа на сите скриени
//        датотеки (тоа се датотеки чие што име започнува со знакот за точка .) со големина
//        помала од size.
//public int totalSizeOfFilesFromFolders(List<Character> folders) - враќа вкупна големина на
//        сите датотеки кои се наоѓаат во фолдерите кои се зададени во листата folders
//public Map<Integer, Set<File>> byYear() - враќа мапа Map во која за датотеките се групирани
//        според годината на креирање.
//public Map<String, Long> sizeByMonthAndDay() - враќа мапа Map во која за секој месец и ден
//        (независно од годината) се пресметува вкупната големина на сите датотеки креирани
//        во тој месец и тој ден. Месецот се добива со повик на методот getMonth(), а денот getDayOfMonth().
//        Датотеките во секој фолдер се подредени според датумот на креирање во растечки редослед,
//        потоа според името лексикографски и на крај според големината. Да се имплементира ваков
//        компаратор во самата класа File. Исто така да се имплементира и toString репрезентација
//        во следниот формат:
//
//        %-10[name] %5[size]B %[createdAt]

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

// no for-loops/foreach :)
class File implements Comparable<File>{
    private String name;
    private int size;
    private LocalDateTime createdAt;

    public File(String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getYear(){
        return createdAt.getYear();
    }

    public String getMonthDay(){
        return createdAt.getMonth()+"-"+createdAt.getDayOfMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return size == file.size &&
                name.equals(file.name) &&
                createdAt.equals(file.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, createdAt);
    }

    @Override
    public int compareTo(File o) {
        Comparator<File> comp = Comparator.comparing(File::getCreatedAt).thenComparing(File::getName).thenComparing(File::getSize);
        return comp.compare(this,o);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s",name,size,createdAt);
    }
}

class FileSystem{
    private HashMap<Character,TreeSet<File>> folders;

    public FileSystem() {
        folders = new HashMap<Character, TreeSet<File>>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime dateTime) {
        folders.compute(folder,(k,v)->{
            TreeSet<File> files = v;
            if(files==null) files = new TreeSet<File>();
            files.add(new File(name,size,dateTime));
            return files;
        });
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        return folders.values().stream()
                .flatMap(x -> x.stream()).filter(file -> file.getName().charAt(0)=='.')
                .collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> collect) {
        return collect.stream()
                .map(c -> folders.get(c))
                .mapToInt(x -> x.stream().mapToInt(file -> file.getSize()).sum()).sum();
    }

    public Map<Integer, Set<File>> byYear() {
        return folders.values().stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toMap(
                        File::getYear,
                        v -> {
                            HashSet<File> files = new HashSet<File>();
                            files.add(v);
                            return files;
                        },
                        (o,n)->{
                            o.addAll(n);
                            return o;
                        }
                ));
    }

    public Map<String, Long> sizeByMonthAndDay() {
        return folders.values().stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toMap(
                        File::getMonthDay,
                        x -> (long) x.getSize(),
                        (ov,nv) -> ov+nv
                ));
    }
}