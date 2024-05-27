//За потребите на модулот за онлајн плаќања на системот iknow потребно е да напишете класа
//        OnlinePayments со следните методи:
//
//default конструктор
//        void readItems (InputStream is) - метод за вчитување на сите ставки кои се платени
//        преку модулот. Секоја ставка е во нов ред и е во следниот формат STUDENT_IDX ITEM_NAME PRICE.
//        void printStudentReport (String index, OutputStream os) - метод за печатење на извештај за
//        студентот со индекс id. Во извештајот треба да се испечати нето износот на сите платени ставки,
//        наплатената банкарска провизија, вкупниот износ кој е наплатен од студентите, како и нумерирана
//        листа од сите ставки кои се платени од студентите сортирани во опаѓачки редослед според цената.
//        Провизијата се пресметува врз вкупниот износ на ставките кои студентот ги плаќа и изнесува 1.14%
//        (но најмалку 3 денари, а најмногу 300). Децималните износи се заокрузуваат со Math.round.

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class StudentNotFound extends Exception{
    public StudentNotFound(String s) {
        super(String.format("Student %s not found!", s));
    }
}

class Payer{
    String index;
    List<String> name;
    List<Integer> price;

    public Payer(String index, String name, int price) {
        this.index = index;
        this.name=new ArrayList<>();
        this.price=new ArrayList<>();
        this.name.add(name);
        this.price.add(price);
    }
    public void add(String name, int price){
        this.name.add(name);
        this.price.add(price);
    }
    public int net(){
        int net=0;
        for (int i=0; i<price.size(); i++){
            net+= price.get(i);
        }
        return net;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        double fee = this.net() * 0.0114;
        fee = Math.round(fee);
        if(fee<3){
            fee=3;
        }
        if(fee>300){
            fee=300;
        }
        int total = (int) (this.net() + fee);
        sb.append(String.format("Student: %s Net: %d Fee: %d Total: %d", index, this.net(), (int) fee, total));
        sb.append(String.format("\nItems:\n"));
        Map<Integer, List<String>>sorted=new TreeMap<>(Collections.reverseOrder());
        for (int i = 0; i < price.size(); i++) {
            int currentPrice = price.get(i);
            String currentItem = name.get(i);
            if (sorted.containsKey(currentPrice)) {
                sorted.get(currentPrice).add(currentItem);
            } else {
                List<String> items = new ArrayList<>();
                items.add(currentItem);
                sorted.put(currentPrice, items);
            }
        }
        int i=0;
        int totalItems = sorted.values().stream().mapToInt(List::size).sum();
        for (Map.Entry<Integer, List<String>> entry : sorted.entrySet()) {
            for (String item : entry.getValue()) {
                sb.append(String.format("%d. %s %d", i+1, item, entry.getKey()));
                i++;
                if(i!=totalItems){
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}

class OnlinePayments{
    HashMap<String, Payer>map;

    public OnlinePayments() {
        this.map = new HashMap<>();
    }

    void readItems (InputStream is){
        Scanner sc=new Scanner(is);
        while (sc.hasNext()){
            String line= sc.nextLine();
            String []parts=line.split(";");
            if(map.containsKey(parts[0])){
                Payer p=map.get(parts[0]);
                p.add(parts[1], Integer.parseInt(parts[2]));
            }
            else {
                Payer p=new Payer(parts[0], parts[1], Integer.parseInt(parts[2]));
                map.put(parts[0], p);
            }
        }
    }
    void printStudentReport (String index, OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        try{
            if(map.containsKey(index)){
                Payer p=map.get(index);
                pw.println(p.toString());
            }
            else {
                throw new StudentNotFound(index);
            }
        }
        catch (StudentNotFound s){
            System.out.println(s.getMessage());
        }
        pw.flush();
    }
}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}