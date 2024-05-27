//Да се дефинира генеричка класа за правило (Rule) во која ќе се чуваат имплементации на
//        интерфејсите Predicate и Function. Генеричката класа треба да има два генерички
//        параметри - еден за влезниот тип (типот на објектите кои се спроведуваат низ правилото)
//        и еден за излезниот тип (типот на објектите кои се резултат од правилото).
//
//        Во класата Rule да се дефинира метод apply кој прима еден аргумент input од влезниот тип,
//        а враќа објект од генеричката класата Optional со генерички параметар ист како излезниот
//        тип на класата Rule. Методот apply треба да врати Optional објект пополнет со резултатот
//        добиен од Function имплементацијата применет на аргументот input само доколку е исполнет
//        предикатот од правилото Rule. Доколку предикатот не е исполнет, методот apply враќа празен
//        Optional.
//
//        Дополнително, да се дефинира класа RuleProcessor со еден генерички статички метод process
//        кој ќе прими два аргументи:
//
//        Листа од влезни податоци (објекти од влезниот тип)
//        Листа од правила (објекти од класа Rule)
//        Методот потребно е врз секој елемент од листата на влезни податоци да го примени секое правило
//        од листата на правила и на екран да го испечати резултатот од примената на правилото
//        (доколку постои), а во спротивно да испечати порака Condition not met.
//
//        Во главната класа на местата означени со TODO да се дефинираат потребните објекти од класата
//        Rule. Да се користат ламбда изрази за дефинирање на објекти од тип Predicate и Function.
//
//
//
//        Напомена: Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети.
//        Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети
//        со најмногу 80% од поените.

import java.util.*;
import java.util.stream.Collectors;

interface Predicate<T>{
    boolean test(T t);
}

interface Function<T, R>{
    R apply(T t);
}

class Rule<T, R>{
    private final Predicate<T> predicate;
    private final Function<T, R> function;

    public Rule(Predicate<T> predicate, Function<T, R> function) {
        this.predicate = predicate;
        this.function = function;
    }
    public Optional<R> apply(T input){
        if(predicate.test(input)){
            return Optional.ofNullable(function.apply(input));
        }
        else{
            return Optional.empty();
        }
    }
}

class RuleProcessor{
    public static <T, R> void process(List<T> inputs, List<Rule<T, R>> rules){
        for (T input:inputs) {
            System.out.println("Input: "+input);
            boolean flag=false;
            for(Rule<T, R> rule:rules){
                Optional<R> result=rule.apply(input);
                if(result.isPresent()){
                    System.out.println("Result: "+result.get());
                    flag=true;
                    //break;
                }
                else {
                    System.out.println("Condition not met");
                }
            }
        }
    }
}

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */
            Predicate<String> containsNPPredicate = s -> s.contains("NP");
            Function<String, Integer> indexOfNPFunction = s -> s.indexOf("NP");
            Rule<String, Integer> rule1 = new Rule<>(containsNPPredicate, indexOfNPFunction);
            rules.add(rule1);

            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */
            Predicate<String> startsWithNPPredicate = s -> s.startsWith("NP");
            Function<String, Integer> lengthOfStringFunction = String::length;
            Rule<String, Integer> rule2 = new Rule<>(startsWithNPPredicate, lengthOfStringFunction);
            rules.add(rule2);


            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
            Predicate<Student> hasAtLeast3GradesPredicate = student -> student.grades.size() >= 3;
            Function<Student, Double> maxGradeFunction = student -> (double) Collections.max(student.grades);
            Rule<Student, Double> rule1 = new Rule<>(hasAtLeast3GradesPredicate, maxGradeFunction);
            rules.add(rule1);

            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0
            Predicate<Student> startsWith20Predicate = student -> student.id.startsWith("20");
            Function<Student, Double> averageGradeFunction = student -> {
                if (student.grades.isEmpty()) {
                    return 5.0;
                } else {
                    return student.grades.stream().mapToDouble(Double::valueOf).average().orElse(0.0);
                }
            };
            Rule<Student, Double> rule2 = new Rule<>(startsWith20Predicate, averageGradeFunction);
            rules.add(rule2);


            List<Student> students = new ArrayList<>();
            while (sc.hasNext()){
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}
