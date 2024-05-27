//Да се имплементира класа StudentRecords која ќе чита од влезен тек (стандарден влез, датотека, ...) податоци за студентски досиеа. Податоците содржат (код - единствен стринг), насока (стринг од 3 букви) и низа со оценки (цели броеви од 6 - 10). Сите податоци се разделени со едно празно место. Пример за форматот на податоците:
//
//        ioqmx7 MT 10 8 10 8 10 7 6 9 9 9 6 8 6 6 9 9 8
//        Ваша задача е да ги имплементирате методите:
//
//        StudentRecords() - default конструктор
//        int readRecords(InputStream inputStream) - метод за читање на податоците кој враќа вкупно
//        прочитани записи
//        void writeTable(OutputStream outputStream) - метод кој ги печати сите записите за сите студенти
//        групирани по насока (најпрво се печати името на насоката), а потоа се печатат сите записи за
//        студентите од таа насока сортирани според просекот во опаѓачки редослед (ако имаат ист просек
//        според кодот лексикографски) во формат kod prosek, каде што просекот е децимален број заокружен
//        на две децимали. Пример jeovz8 8.47. Насоките се сортирани лексикографски. Комплексноста на
//        методот да не надминува $O(N)$ во однос на бројот на записи.
//        void writeDistribution(OutputStream outputStream) - метод за печатење на дистрибуцијата на
//        бројот на оценки по насока, притоа насоките се сортирани по бројот на десетки во растечки
//        редослед (прва е насоката со најмногу оценка десет). Дистрибуцијата на оценки се печати во
//        следниот формат:
//        NASOKA
//        [оценка со 2 места порамнети во десно] | [по еден знак * на секои 10 оценки] ([вкупно оценки])
//        Пример:
//
//        KNI
//        6 | ***********(103)
//        7 | ******************(173)
//        8 | *******************(184)
//        9 | *****************(161)
//        10 | **************(138)
//        Комплексноста на овој метод да не надминува $O(N * M*log_2(M))$ за N записи и M насоки.

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student {
    String code;
    String smer;
    List<Integer> grades;
    double average;
    int num;

    public Student(String code, String smer, List<Integer> grades) {
        this.code = code;
        this.smer = smer;
        this.grades = grades;
        this.average = av(grades);
        this.num = ten();
    }

    int ten() {
        return (int) grades.stream().filter(s -> s == 10).count();
    }

    int numOfGrades(int grade) {
        return (int) grades.stream().filter(s -> s == grade).count();
    }

    public double getAverage() {
        return average;
    }

    public String getName() {
        return code;
    }

    double av(List<Integer> grades) {
        return grades.stream().mapToDouble(g -> g).sum() / grades.size();
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code, average);
    }
}

class StudentRecords {
    Map<String, List<Student>> studentRecords;
    Comparator<Map.Entry<String, List<Student>>> comparator = (e1, e2) ->
            Integer.compare(e2.getValue().stream().mapToInt(s -> s.ten()).sum(), e1.getValue().stream().mapToInt(s -> s.ten()).sum());

    public StudentRecords() {
        this.studentRecords = new TreeMap<>();
    }

    int readRecords(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        int counter = 0;
        while (sc.hasNextLine()) {
            counter++;
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            List<Integer> grades = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                grades.add(Integer.parseInt(parts[i]));
            }
            Student s = new Student(parts[0], parts[1], grades);
            if (studentRecords.containsKey(parts[1])) {
                List<Student> studentList = studentRecords.get(parts[1]);
                studentList.add(s);
                studentRecords.put(parts[1], studentList);
            } else {
                List<Student> studentList = new ArrayList<>();
                studentList.add(s);
                studentRecords.put(parts[1], studentList);
            }
        }
        return counter;
    }

    void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        studentRecords.entrySet().stream().forEach(entry -> {
            pw.println(entry.getKey());
            entry.getValue().stream().sorted(Comparator.comparing(Student::getAverage).reversed().thenComparing(Student::getName)).forEach(s -> pw.println(s));
        });
        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        studentRecords.entrySet().stream().sorted(comparator).forEach(entry -> {
            pw.println(entry.getKey());
            IntStream.range(6, 11).forEach(i -> pw.printf("%2d | %s(%d)\n", i, star(entry.getValue().stream().mapToInt(s -> s.numOfGrades(i)).sum()), entry.getValue().stream().mapToInt(s -> s.numOfGrades(i)).sum()));
        });
        pw.flush();
    }

    String star(int n){
        n= (int) Math.ceil((double) n/10);
        String s="";
        for (int i = 0; i < n; i++) {
            s+="*";
        }
        return s;
    }
}

public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}
