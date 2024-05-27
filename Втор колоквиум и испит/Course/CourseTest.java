//За потребите на предметот Напредно програмирање потребно е да се имплементира систем
//        за евидентирање на напредокот на студентите во текот на семестарот и за генерирање
//        на извештаи за оценки.
//
//        Да се имплементира класа Student во која ќе се чува:
//
//        Индекс
//        Име на студентот
//        Поени на прв колоквиум (цел број со максимална вредност 100)
//        Поени на втор колоквиум (цел број со максимална вредност 100)
//        Поени од лабораториски вежби (цел број со максимална вредност 10)
//        За класата да се имплементира потребниот конструктор.
//
//        Да се напише класа AdvancedProgrammingCourse во која ќе се чува колекција од студенти
//        кои го слушаат и полагаат предметот Напредно програмирање. Во ова класа да се имплементираат
//        следните методи:
//
//public void addStudent (Student s) - додавање на студент на предметот.
//public void updateStudent (String idNumber, String activity, int points) - метод за ажурирање на
//        поените на студентот со индекс idNumber во активноста activity со поените points. Методот
//        да има комплекност О(1)! Можни вредности за activity се midterm1,midterm2 и labs. Со помош
//        на исклучоци да се игнорираат додавања на поени кои се невалидни
//        (не е потребно да се печати порака).
//public List<Student> getFirstNStudents (int n) - ги враќа првите N најдобри положени студенти на
//        предметот сортирани во опаѓачки редослед според вкупниот број на сумарни поени. Сумарните
//        поени се пресметуваат по формулата: midterm1 * 0.45 + midterm2 * 0.45 + labs.
//public Map<Integer,Integer> getGradeDistribuition() - враќа мапа од оценките (5,6,7,8,9,10) со бројот
//        на студенти кои ја добиле соодветната оценка.
//public void printStatistics() - печати основни статистики за вкупните поени (min,max,average,count)
//        за сумарните поени на сите положени студенти.

import java.util.*;
import java.util.stream.Collectors;

class InvalidNumber extends Exception{
    public InvalidNumber() {
    }
}

class Student{
    String index;
    String name;
    int first, second, labs;

    public Student(String index, String name){
        this.index = index;
        this.name = name;
    }
    public Student(String index, String name, int first, int second, int labs) {
        this.index = index;
        this.name = name;
        this.first = first;
        this.second = second;
        this.labs = labs;
    }
    public void setFirst(int first) {
        this.first = first;
    }
    public void setSecond(int second) {
        this.second = second;
    }
    public void setLabs(int labs) {
        this.labs = labs;
    }
    double getPoints(){
        return first*0.45+second*0.45+labs;
    }
    int getGrade(){
        int num= (int) ((this.getPoints()/10)+1);
        if(num>5&&num<=10){
            return num;
        }
        return 5;
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d", index, name, first, second, labs, this.getPoints(), this.getGrade());
    }
}

class AdvancedProgrammingCourse{
    Map<String, Student>students;

    public AdvancedProgrammingCourse() {
        this.students=new HashMap<>();
    }
    public void addStudent (Student s){
        students.put(s.index, s);
    }
    public void updateStudent (String idNumber, String activity, int points) throws InvalidNumber{
        if((activity.equals("midterm1")||activity.equals("midterm2"))&&points>100){
            throw new InvalidNumber();
        }
        if(activity.equals("labs")&&points>10){
            throw new InvalidNumber();
        }
        if(points<0){
            throw new InvalidNumber();
        }
        Student s=students.get(idNumber);
        if(activity.equals("midterm1")){
            s.first=points;
        }
        if(activity.equals("midterm2")){
            s.second=points;
        }
        if(activity.equals("labs")){
            s.labs=points;
        }
    }
    public List<Student> getFirstNStudents (int n){
        return students.values().stream().sorted(Comparator.comparingDouble(Student::getPoints).reversed()).limit(n).collect(Collectors.toList());
    }
    public Map<Integer,Integer> getGradeDistribution(){
        Map<Integer, Integer>mapa=new HashMap<>();
        for (int i=5; i<=10; i++){
            int brojach=0;
            for (Student s:students.values()){
                if(s.getGrade()==i){
                    brojach++;
                }
            }
            mapa.put(i, brojach);
        }
        return mapa;
    }
    public void printStatistics(){
        int counter=0;
        float min=9999, max=0, average=0;
        for(Student s:students.values()){
            if(s.getGrade()>5){
                counter++;
                if(s.getPoints()<min){
                    min= (float) s.getPoints();
                }
                if(s.getPoints()>max){
                    max= (float) s.getPoints();
                }
                average+=s.getPoints();
            }
        }
        average/=counter;
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f", counter, min, average, max));
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                try {
                    advancedProgrammingCourse.updateStudent(idNumber, activity, points);
                } catch (InvalidNumber e) {

                }
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
