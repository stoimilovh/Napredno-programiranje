//Да се имплементира класа F1Race која ќе чита од влезен тек
//        (стандарден влез, датотека, ...) податоци за времињата од последните 3 круга на неколку
//        пилоти на Ф1 трка. Податоците се во следниот формат:
//
//        Driver_name lap1 lap2 lap3, притоа lap е во формат mm:ss:nnn каде mm се минути ss се секунди
//        nnn се милисекунди (илјадити делови од секундата). Пример:
//
//        Vetel 1:55:523 1:54:987 1:56:134.
//
//        Ваша задача е да ги имплементирате методите:
//
//        F1Race() - default конструктор
//        void readResults(InputStream inputStream) - метод за читање на податоците
//        void printSorted(OutputStream outputStream) - метод кој ги печати сите пилоти сортирани
//        според нивното најдобро време (најкраткото време од нивните 3 последни круга) во формат
//        Driver_name best_lap со 10 места за името на возачот (порамнето од лево) и 10 места за
//        времето на најдобриот круг порамнето од десно. Притоа времето е во истиот формат со времињата
//        кои се читаат.

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

class Formula{
    String name;
    List<Double> broevi;

    public Formula(String name, List<Double> broevi) {
        this.name = name;
        this.broevi = broevi;
        Collections.sort(broevi);
    }
    public String bestTime(){
        double time=broevi.get(0);
        int minutes, seconds;
        int total=(int)Math.floor(time);
        minutes=total/60;
        seconds=total%60;
        double ms=((time-Math.floor(time))*1000);
        return String.format("%d:%02d:%03.0f", minutes, seconds, ms);
    }
    double getBroj(){
        return broevi.get(0);
    }
}
class F1Race {
    // vashiot kod ovde
    List<Formula>formuli;

    public F1Race() {
        this.formuli = new ArrayList<>();
    }
    void readResults(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        while (sc.hasNext()){
            String red=sc.nextLine();
            String []podelba=red.split(" ");
            String name=podelba[0];
            String []vreme1=podelba[1].split(":");
            String []vreme2=podelba[2].split(":");
            String []vreme3=podelba[3].split(":");
            double a=(Integer.parseInt(vreme1[0])*60)+
                    Integer.parseInt(vreme1[1])+
                    (Double.parseDouble(vreme1[2])/1000.0);
            double b=(Integer.parseInt(vreme2[0])*60)+
                    Integer.parseInt(vreme2[1])+
                    (Double.parseDouble(vreme2[2])/1000.0);
            double c=(Integer.parseInt(vreme3[0])*60)+
                    Integer.parseInt(vreme3[1])+
                    (Double.parseDouble(vreme3[2])/1000.0);
            List<Double>br=new ArrayList<>();
            br.add(a);
            br.add(b);
            br.add(c);
            Formula fr=new Formula(name, br);
            formuli.add(fr);
        }
    }
    void sortFormula(){
        formuli.sort(Comparator.comparingDouble(formula->formula.getBroj()));
    }
    void printSorted(OutputStream outputStream){
        PrintStream out=new PrintStream(outputStream);
        sortFormula();
        int i=1;
        for (Formula formula:formuli) {
            String vreme=formula.bestTime();
            out.println(String.format("%d. %-11s %s", i, formula.name, vreme));
            i++;
        }
    }
}

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
