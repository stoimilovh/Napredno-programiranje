//Да се имплементира класа MojDDV која што од влезен тек ќе чита информации за скенирани
//        фискални сметки од страна на еден корисник на истоимената апликација.
//        Податоците за фискалните сметки се во следниот формат:
//
//        ID item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
//
//        На пример: 12334 1789 А 1238 B 1222 V 111 V
//
//        Постојат три типа на данок на додадена вредност и тоа:
//
//        А (18% од вредноста)
//        B (5% од вредноста)
//        V (0% од вредноста)
//        Повратокот на ДДВ изнесува 15% од данокот на додадената вредност за артикалот.
//
//        Да се имплементираат методите:
//
//        void readRecords (InputStream inputStream)- метод којшто ги чита од влезен тек податоците за
//        фискалните сметки. Доколку е скенирана фискална сметка со износ поголем од 30000 денари потребно
//        е да се фрли исклучок од тип AmountNotAllowedException. Дефинирајте каде ќе се фрла исклучокот,
//        и каде ќе биде фатен, на начин што оваа функција, ќе може да ги прочита сите фискални коишто
//        се скенирани. Исклучокот треба да испечати порака “Receipt with amount [сума на сите артикли]
//        is not allowed to be scanned”.
//        void printTaxReturns (OutputStream outputStream) - метод којшто на излезен тек ги печати сите
//        скенирани фискални сметки во формат “ID SUM_OF_AMOUNTS TAX_RETURN”, каде што SUM_OF_AMOUNTS e
//        збирот на сите артикли во фискалната сметка, а TAX_RETURN е пресметаниот повраток на ДДВ за
//        таа фискална сметка.

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}

class MojDDV {
    private List<Smetka> smetkiList;
    public static double povratok = 0.15;

    public void readRecords(InputStream in) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        smetkiList = bf.lines().map(line -> {
            try {
                return new Smetka(line);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return new Smetka();
            }
        }).collect(Collectors.toList());
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
        smetkiList.stream().filter(x -> x.getId()!=-1).forEach(x -> pw.println(x.toString()));
        pw.close();
    }
}

class Smetka {
    private int id;
    private List<Produkt> produktList;

    public Smetka() {
        id = -1;
    }

    public int getId() {
        return id;
    }

    public Smetka(String line) throws AmountNotAllowedException {
        id = Integer.parseInt(line.split("\\s+")[0]);
        produktList = new ArrayList<Produkt>();
        Arrays.stream(line.split("\\s+")).skip(1).forEach(x -> {
            try {
                int price = Integer.parseInt(x);
                produktList.add(new Produkt(price));
            }catch(NumberFormatException nf){
                Type t = Type.A;
                if(x.equals("B")) t = Type.B;
                if(x.equals("V")) t = Type.V;
                produktList.get(produktList.size()-1).setType(t);
            }
        });
        if(sum()>30000) throw new AmountNotAllowedException(sum());
    }

    public int sum() {
        return produktList.stream().mapToInt(x -> x.getPrice()).sum();
    }

    @Override
    public String toString() {
        return String.format("%d %d %.2f",id,sum(),taxReturn());
    }

    private double taxReturn() {
        return produktList.stream().mapToDouble(x -> x.getDanok()*x.getPrice()*MojDDV.povratok).sum();
    }
}

enum Type{A,B,V}

class Produkt {
    private int price;
    private Type type;
    private double danok;

    public void setType(Type type) {
        this.type = type;
        if(type == Type.A) danok = 0.18;
        if(type == Type.B) danok = 0.05;
        if(type == Type.V) danok = 0;
    }

    public Produkt(int price) {
        this.price = price;
    }

    public double getDanok() {
        return danok;
    }

    public int getPrice() {
        return price;
    }

    public Type getType() {
        return type;
    }
}

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",sum));
    }
}