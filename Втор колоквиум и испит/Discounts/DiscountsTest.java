//Да се имплементира класа Discounts за обработка на информации за цени и цени на попуст
//        на одредени производи во неколку продавници (објекти од класа Store).
//        Потребно е да се имплементираат следните методи:
//
//public int readStores(InputStream inputStream) - метод за вчитување на податоците за продавниците и
//        цените на производите. Податоците за секоја продавница се во посебен ред во формат
//        [ime] [cena_na_popust1:cena1] [cena_na_popust2:cena2] ... (погледнете пример).
//        Методот враќа колку продавници се вчитани.
//public List<Store> byAverageDiscount() - метод кој враќа листа од 3-те продавници со најголем
//        просечен попуст (просечна вредност на попустот за секој производ од таа продавница).
//        Попустот (намалувањето на цената) е изразен во цел број (проценти) и треба да се пресмета
//        од намалената цена и оригиналната цена. Ако две продавници имаат ист попуст, се подредуваат
//        според името лексикографски.
//public List<Store> byTotalDiscount() - метод кој враќа листа од 3-те продавници со намал вкупен
//        попуст (сума на апсолутен попуст од сите производи). Апсолутен попуст е разликата од цената
//        и цената на попуст. Ако две продавници имаат ист попуст, се подредуваат според името
//        лексикографски.
//        Дополнително за класата Store да се имплементира стринг репрезентација, односно методот:
//
//public String toString() кој ќе враќа репрезентација во следниот формат:
//
//        [Store_name]
//        Average discount: [заокружена вредност со едно децимално место]%
//        Total discount: [вкупен апсолутен попуст]
//        [процент во две места]% [цена на попуст]/[цена]
//        ...
//        при што продуктите се подредени според процентот на попуст (ако е ист, според апсолутниот попуст)
//        во опаѓачки редослед.

import java.io.InputStream;
import java.util.*;

class Discount{
    int discount;
    String s;

    public Discount(int discount, String s) {
        this.discount = discount;
        this.s = s;
    }
    int getDiscount(){
        return discount;
    }
    int getNumber(){
        String []parts=s.split("/");
        int n=Integer.parseInt(parts[1]);
        return n;
    }
}

class Store{
    String name;
    List<String>discount;
    Map<Integer, String>map;

    public Store(String name, List<String> discount) {
        this.name = name;
        this.discount = discount;
        this.map=new HashMap<>();
    }
    double AverageDiscount(){
        int sum=0;
        double average=0;
        for (int i=0; i<discount.size(); i++){
            String s=discount.get(i);
            String []discounts=s.split(":");
            int a=Integer.parseInt(discounts[0]);
            int b=Integer.parseInt(discounts[1]);
            double c=((double) a/b)*100;
            double d=(100-c);
            sum+=d;
        }
        average=(double) sum/discount.size();
        return average;
    }
    int TotalDiscount(){
        int sum=0;
        for (int i=0; i<discount.size(); i++){
            String s=discount.get(i);
            String []discounts=s.split(":");
            int a=Integer.parseInt(discounts[0]);
            int b=Integer.parseInt(discounts[1]);
            double c=Math.abs(a-b);
            sum+=c;
        }
        return sum;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(name+"\n");
        sb.append(String.format("Average discount: %.1f%%\n", this.AverageDiscount()));
        sb.append(String.format("Total discount: %d\n", this.TotalDiscount()));
        List<Discount>dis=new ArrayList<>();
        for (int i=0; i<discount.size(); i++){
            String s=discount.get(i);
            String []discounts=s.split(":");
            int a=Integer.parseInt(discounts[0]);
            int b=Integer.parseInt(discounts[1]);
            double c=((double) a/b)*100;
            int d= (int) (100-c);
            Discount dd=new Discount(d, (String.format("%2d%% %d/%d", d, a, b)));
            dis.add(dd);
        }
        dis.sort(Comparator.comparingInt(Discount::getDiscount).thenComparing(Discount::getNumber).reversed());
        for (int i=0; i<dis.size(); i++){
            sb.append(dis.get(i).s);
            if(i+1!=dis.size()){
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

class Discounts{
    Map<String, Store>storeMap;

    public Discounts() {
        this.storeMap=new HashMap<>();
    }
    public int readStores(InputStream inputStream){
        int counter=0;
        Scanner s=new Scanner(inputStream);
        while (s.hasNextLine()){
            counter++;
            String p=s.nextLine();
            String []parts=p.split("\\s+");
            if(parts.length>=1){
                String name=parts[0];
                List<String>dis=new ArrayList<>();
                for (int i=1; i<parts.length; i++){
                    dis.add(parts[i]);
                }
                Store store=new Store(name, dis);
                storeMap.put(name, store);
            }
        }
        return counter;
    }
    public List<Store> byAverageDiscount(){
        List<Store>ordered=new ArrayList<>(storeMap.values());
        ordered.sort(Comparator.comparingDouble(Store::AverageDiscount).reversed().thenComparing(Store::getName));
        List<Store>s=new ArrayList<>();
        for (int i=0; i<3; i++){
            s.add(ordered.get(i));
        }
        return s;
    }
    public List<Store> byTotalDiscount(){
        List<Store>ordered=new ArrayList<>(storeMap.values());
        ordered.sort(Comparator.comparingDouble(Store::TotalDiscount).thenComparing(Store::getName));
        List<Store>s=new ArrayList<>();
        for (int i=0; i<3; i++){
            s.add(ordered.get(i));
        }
        return s;
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}