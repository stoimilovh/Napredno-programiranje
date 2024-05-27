//Да се имплементира класа за аудиција Audition со следните методи:
//
//        void addParticpant(String city, String code, String name, int age) додава нов кандидат со
//        код code, име и возраст за аудиција во даден град city. Во ист град не се дозволува додавање
//        на кандидат со ист код како некој претходно додаден кандидат (додавањето се игнорира,
//        а комплексноста на овој метод треба да биде $O(1)$)
//        void listByCity(String city) ги печати сите кандидати од даден град подредени според името,
//        а ако е исто според возраста (комплексноста на овој метод не треба да надминува
//        $O(n*log_2(n))$, каде $n$ е бројот на кандидати во дадениот град).

import java.util.*;
import java.util.stream.Collectors;

class Code{
    String code;
    String name;
    int age;

    public Code(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class City{
    String name;
    Map<String, Code>codes;

    public City(String name) {
        this.name=name;
        this.codes=new HashMap<>();
    }
    void addCode(String code, String name, int age){
        if(!codes.containsKey(code)){
            Code c=new Code(code, name, age);
            codes.put(code, c);
        }
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        List<Code>c=codes.values().stream().sorted(Comparator.comparing(Code::getName).thenComparingInt(Code::getAge)).collect(Collectors.toList());
        for (int i=0; i<c.size(); i++) {
            sb.append(c.get(i).toString());
            if(i+1!=c.size()){
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

class Audition{
    Map<String, City>cityMap;

    public Audition() {
        this.cityMap=new HashMap<>();
    }
    void addParticpant(String city, String code, String name, int age){
        if(!cityMap.containsKey(city)){
            City c=new City(city);
            c.addCode(code, name, age);
            this.cityMap.put(city, c);
        }
        else{
            City c=this.cityMap.get(city);
            c.addCode(code, name, age);
            this.cityMap.put(city, c);
        }
    }
    void listByCity(String city){
        City c=cityMap.get(city);
        System.out.println(c.toString());
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}