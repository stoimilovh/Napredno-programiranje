//Да се имплементира класа Names со следните методи:
//
//public void addName(String name) - додавање на име
//public void printN(int n) - ги печати сите имиња кои се појавуваат n или повеќе пати,
//        подредени лексикографски според името, на крајот на зборот во загради се печати
//        бројот на појавувања, а по него на крај бројот на уникатни букви во зборот
//        (не се прави разлика на големи и мали)
//public String findName(int len, int x) - го враќа името кое со наоѓа на позиција x
//        (почнува од 0) во листата од уникатни имиња подредени лексикографски,
//        по бришење на сите имиња со големина поголема или еднаква на len.
//        Позицијата x може да биде поголема од бројот на останати имиња, во тој случај се
//        продожува со броење од почетокот на листата. Пример за листа со 3 имиња A, B, C,
//        ако x = 7, се добива B. A0, B1, C2, A3, B4, C5, A6, B7.

import java.util.*;

class Names{
    List<String>names;
    Map<String, Integer>map;

    public Names() {
        this.names = new ArrayList<>();
        this.map = new HashMap<>();
    }
    public void addName(String name) {
        if (map.containsKey(name)) {
            int num= map.get(name);
            num++;
            map.put(name, num);
        }
        else {
            map.put(name, 1);
            names.add(name);
        }
    }
    public void printN(int n){
        names.sort(Comparator.naturalOrder());
        for(int i=0; i<names.size(); i++){
            if(map.get(names.get(i))>=n){
                System.out.printf("%s (%d) %d\n", names.get(i), map.get(names.get(i)), uniqueChars(names.get(i)));
            }
        }
    }
    public String findName(int len, int x){
        List<String>namesList=new ArrayList<>();
        for (int i=0; i<names.size(); i++){
            if(names.get(i).length()<len){
                namesList.add(names.get(i));
            }
        }
        int index=x%namesList.size();
        if(x<namesList.size()){
            return namesList.get(index);
        }
        return namesList.get(index);
    }
    int uniqueChars(String name){
        Set<Character>unique=new HashSet<>();
        for (int i=0; i<name.length(); i++){
            unique.add(Character.toLowerCase(name.charAt(i)));
        }
        return unique.size();
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}