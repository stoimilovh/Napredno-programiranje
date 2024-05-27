//Да се напише класата Student во која што ќе се чуваат информации за:
//
//        индекс на студент ФИНКИ (стринг составен од шест бројки)
//        листа на поени добиени на лабораториски вежби по некој предмет ФИНКИ.
//        По предметот се изведуваат максимум 10 лабораториски вежби.
//        За класата да се напише конструктор Student(String index, List<Integer> points).
//
//        Да се напише класа LabExercises во која што се чува колекција од студенти.
//        За класата да се напишат следните методи:
//
//public void addStudent (Student student)- метод за додавање на нов студент во колекцијата
//public void printByAveragePoints (boolean ascending, int n) - метод којшто ќе ги печати првите n
//        студентите сортирани според сумарните поени, а доколку се исти сумарните поени, според индексот,
//        во растечки редослед доколку ascending е true, a во спротивно во опаѓачки.
//        сумарните поени се пресметуваат како збирот на поените поделен со 10.
//public List<Student> failedStudents () - метод којшто враќа листа од студенти кои не добиле потпис
//        (имаат повеќе од 2 отсуства), сортирани прво според индексот, а потоа според сумарните поени.
//public Map<Integer,Double> getStatisticsByYear() - метод којшто враќа мапа од просекот од сумарните
//        поени на студентите според година на студирање. Да се игнорираат студентите кои не добиле потпис.

import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    boolean hasSignature() {
        return points.size() >= 8;
    }

    int failed() {
        return (int) points.stream().mapToInt(g -> g).filter(s -> s == 0).count();
    }

    double average() {
        return points.stream().mapToDouble(g -> g).sum() / 10;
    }

    public String getIndex() {
        return index;
    }

    int getYear() {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    @Override
    public String toString() {
        if (hasSignature()) {
            return String.format("%s YES %.2f", index, average());
        } else {
            return String.format("%s NO %.2f", index, average());
        }
    }
}


class LabExercises{
    Set<Student> studentSet;

    public LabExercises() {
        this.studentSet = new HashSet<>();
    }
    public void addStudent (Student student){
        studentSet.add(student);
    }
    Comparator<Student> getComparator(boolean b){
        if(b){
            return Comparator.comparingDouble(Student::average).thenComparing(Student::getIndex);
        }
        else {
            return Comparator.comparingDouble(Student::average).thenComparing(Student::getIndex).reversed();
        }
    }
    public void printByAveragePoints (boolean ascending, int n){
        Comparator<Student>comparator=getComparator(ascending);
        List<Student>bestStudents= studentSet.stream().sorted(comparator).limit(n).collect(Collectors.toList());
        bestStudents.stream().forEach(s -> System.out.println(s.toString()));
    }
    public List<Student> failedStudents (){
        return studentSet.stream().filter(s -> !s.hasSignature()).sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::average)).collect(Collectors.toList());
    }
    public Map<Integer,Double> getStatisticsByYear(){
        return studentSet.stream().filter(Student::hasSignature).collect(Collectors.groupingBy(Student::getYear, Collectors.averagingDouble(Student::average)));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}