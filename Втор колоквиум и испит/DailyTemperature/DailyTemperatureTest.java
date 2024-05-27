//Да се имплементира класа DailyTemperatures во која се вчитуваат температури на воздухот
//        (цели броеви) за различни денови од годината (број од 1 до 366). Температурите за
//        еден ден се во еден ред во следниот формат (пример): 137 23C 15C 28C. Првиот број
//        претставува денот во годината, а потоа следуваат непознат број на мерења на температури
//        за тој ден во скала во Целзиусови степени (C) или Фаренхајтови степени (F).
//
//        Во оваа класа да се имплементираат методите:
//
//        DailyTemperatures() - default конструктор
//        void readTemperatures(InputStream inputStream) - метод за вчитување на податоците од влезен
//        тек
//        void writeDailyStats(OutputStream outputStream, char scale) - метод за печатање на дневна
//        статистика (вкупно мерења, минимална температура, максимална температура, просечна температура)
//        за секој ден, подредени во растечки редослед според денот. Вториот аргумент scale одредува во
//        која скала се печатат температурите C - Целзиусова, F - Фаренхајтова. Форматот за печатање на
//        статистиката за одреден ден е следниот:
//        [ден]: Count: [вк. мерења - 3 места] Min: [мин. температура] Max: [макс. температура]
//        Avg: [просек ]
//
//        Минималната, максималната и просечната температура се печатат со 6 места, од кои 2 децимални,
//        а по бројот се запишува во која скала е температурата (C/F).
//
//        Формула за конверзија од Целзиусуви во Фаренхајтови: $\frac{T * 9}{5} + 32$
//
//        Формула за конверзија од Фаренхајтови во Целзиусуви: $\frac{(T - 32) * 5}{9}$
//
//        Забелешка: да се постигне иста точност како во резултатите од решението,
//        за пресметување на просекот и конверзијата во различна скала температурите се
//        чуваат со тип Double.

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class Temperature{
    int day;
    List<Double>temp;
    String value;

    public Temperature(int day, List<Double> temp, String value) {
        this.day = day;
        this.temp = temp;
        this.value = value;
    }
    double getMin(){
        return temp.stream().mapToDouble(i -> i).min().getAsDouble();
    }
    double getMax(){
        return temp.stream().mapToDouble(i -> i).max().getAsDouble();
    }
    double getAvg(){
        return temp.stream().mapToDouble(i -> i).average().getAsDouble();
    }
    double CtoF(double t){
        return t*9/5+32;
    }
    double FtoC(double t){
        return (t-32)*5/9;
    }
    public String toString(String scale) {
        if(value.equals(scale)){
            return String.format("%3d: Count: %3d Min: %6.02f%s Max: %6.02f%s Avg: %6.02f%s",
                    day, temp.size(), getMin(), value, getMax(), value, getAvg(), value);
        }
        else {
            if (scale.equals("C")) {
                return String.format("%3d: Count: %3d Min: %6.02f%s Max: %6.02f%s Avg: %6.02f%s",
                        day, temp.size(), FtoC(getMin()), scale, FtoC(getMax()), scale, FtoC(getAvg()), scale);

            } else {
                return String.format("%3d: Count: %3d Min: %6.02f%s Max: %6.02f%s Avg: %6.02f%s",
                        day, temp.size(), CtoF(getMin()), scale, CtoF(getMax()), scale, CtoF(getAvg()), scale);

            }
        }
    }
}

class DailyTemperatures{
    Map<Integer, Temperature>temperatureMap;

    public DailyTemperatures() {
        this.temperatureMap = new TreeMap<>();
    }
    void readTemperatures(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        while (sc.hasNextLine()){
            String s=sc.nextLine();
            String []parts=s.split(" ");
            List<Double>temp=new ArrayList<>();
            for (int i=1; i<parts.length; i++){
                temp.add(Double.parseDouble(parts[i].substring(0, parts[i].length()-1)));
            }
            String value;
            if(s.contains("C")){
                value="C";
            }
            else {
                value="F";
            }
            Temperature t=new Temperature(Integer.parseInt(parts[0]), temp, value);
            temperatureMap.put(Integer.parseInt(parts[0]), t);
        }
    }
    void writeDailyStats(OutputStream outputStream, String scale){
        PrintWriter pw=new PrintWriter(outputStream);
        temperatureMap.values().stream().forEach(i -> pw.println(i.toString(scale)));
        pw.flush();
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, "C");
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, "F");
    }
}