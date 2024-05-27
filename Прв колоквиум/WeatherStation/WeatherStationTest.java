//Во една метеролошка станица на секои 5 минути пристигнуваат податоци за временските
//        услови (температура, влажност на воздухот, ветар, видливост, време).
//        Пример за вакви податоци:
//
//        температура: 13 степени
//        влажност: 98%
//        ветар: 11.2 km/h
//        видливост: 14 km
//        време: 28.12.2013 14:37:55 (dd.MM.yyyy HH:mm:ss).
//        Потребно е да се имплементира класа WeatherStation која ќе ги чува податоците за временските услови за последните x денови (при додавање на податоци за ново мерење, сите мерења чие што време е постаро за x денови од новото се бришат ). Исто така ако времето на новото мерење кое се додава се разликува за помалку од 2.5 минути од времето на некое претходно додадено мерење, тоа треба да се игнорира (не се додава).
//
//        Да се имплементираат следните методи на класата WeatherStation:
//
//        WeatherStation(int days) - конструктор со аргумент бројот на денови за кои се чуваат мерења
//public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date)
//        - додавање на податоци за ново мерење
//public int total() - го враќа вкупниот број на мерења кои се чуваат
//public void status(Date from, Date to) - ги печати сите мерења во периодот од from до to подредени
//        според датумот во растечки редослед и на крај ја печати просечната температура во овој период.
//        Ако не постојат мерења во овој период се фрла исклучок од тип RuntimeException (вграден во Јава).

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Weather{
    float temperature, wind, humidity, visibility;
    Date date;

    public Weather(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public String toString() {
        DateFormat gmtFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy");
        gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtDateString=gmtFormat.format(date);
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, gmtDateString);
    }
}

class WeatherStation{
    List<Weather> weathers;
    int days;

    public WeatherStation(int days){
        this.days=days;
        this.weathers=new ArrayList<>();
    }
    public boolean compareDate(Date d1, Date d2){
        long number=Math.abs(d1.getTime()-d2.getTime());
        long ms=150*1000;
        return number>ms;
    }
    public Date getNewDate(int dayss, Date d){
        Calendar cal=Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, -dayss);
        return cal.getTime();
    }
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date dateee){
        boolean flag=true;
        for (Weather weather:weathers) {
            if(!compareDate(weather.date, dateee)){
                flag=false;
                break;
            }
        }
        if(flag){
            Weather w=new Weather(temperature, wind, humidity, visibility, dateee);
            weathers.add(w);
            Date newd=getNewDate(days, dateee);
            Iterator<Weather>iterator= weathers.iterator();
            while (iterator.hasNext()){
                Weather weather=iterator.next();
                if(weather.date.before(newd)){
                    iterator.remove();
                }
            }
        }
    }
    public int total(){
        return weathers.size();
    }
    public void status(Date from, Date to){
        sortByDate();
        boolean flag=false;
        int brojach=0;
        double average=0;
        for (Weather weather:weathers) {
            if(weather.date.compareTo(from)>=0&&weather.date.compareTo(to)<=0){
                System.out.println(weather);
                average+=weather.temperature;
                brojach++;
                flag=true;
            }
        }
        if(!flag){
            throw new RuntimeException();
        }
        else {
            System.out.println(String.format("Average temperature: %.2f", average/brojach));
        }

    }
    double average(){
        double av=0;
        for (Weather weather:weathers) {
            av+=weather.temperature;
        }
        return av/weathers.size();
    }
    void sortByDate(){
        weathers.sort(Comparator.comparing(weather -> weather.date));
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}