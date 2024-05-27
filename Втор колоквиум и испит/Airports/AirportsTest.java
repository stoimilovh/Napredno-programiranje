//Да се имплементира класа Airports со следните методи:
//
//public void addAirport(String name, String country, String code, int passengers) - метод за
//        додавање нов аеродром (име, држава, код и број на патници кои ги превезува годишно)
//public void addFlights(String from, String to, int time, int duration) - метод за додавање
//        летови (код на аеродром за полетување, код на аеродром за слетување, време на тргнување
//        во минути поминати од 0:00 часот, времетраење на летот во минути). Од аеродром А до
//        аеродром Б може да има повеќе летови.
//public void showFlightsFromAirport(String code) - метод кои ги прикажува сите летови од аеродромот
//        со код code. Прво се печати името на аеродромот (формат во пример излезот), потоа се печатат
//        сите летови (формат во пример излезот) подредени најпрво лексикографски според кодот на
//        аеродромот дестинација, а потоа летовите кон тој аеродром според времето на полетување
//        (целосно точна имплементација се смета без повикување на sort методи).
//public void showDirectFlightsFromTo(String from, String to) - метод кој ги прикажува сите директни
//        летови од аеродромот со код from до аеродромот со код to.
//public void showDirectFlightsTo(String to) - метод кои ги прикажува сите директни летови до
//        аеродромот со код to.
//        Сите летови треба да бидат сортирани според времето на полетување
//        (целосно точна имплементација се смета без повикување на sort методи).

import java.util.*;
import java.util.stream.Collectors;

class Flight implements Comparable<Flight>{
    String from;
    String to;
    int hour;
    int minutes;
    int hoursTime;
    int minutesTime;
    int hoursDuration;
    int minutesDuration;
    int flag;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.hoursTime = time/60;
        this.minutesTime = time%60;
        this.hour=duration/60;
        this.minutes=duration%60;
        this.hoursDuration = hoursTime+hour;
        this.minutesDuration = minutesTime+minutes;
        if(minutesDuration>=60){
            hoursDuration+=minutesDuration/60;
            minutesDuration=minutesDuration%60;
        }
        if(hoursDuration>23){
            flag=1;
            hoursDuration=hoursDuration-24;
        }
        else {
            flag=0;
        }
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public int getHoursTime() {
        return hoursTime;
    }
    public int getMinutesTime() {
        return minutesTime;
    }
    public int getMinutesDuration() {
        return minutesDuration;
    }
    @Override
    public String toString() {
        if(flag==1){
            return String.format("%s-%s %02d:%02d-%02d:%02d +1d %dh%02dm", from, to, hoursTime, minutesTime, hoursDuration, minutesDuration, hour, minutes);
        }
        return String.format("%s-%s %02d:%02d-%02d:%02d %dh%02dm", from, to, hoursTime, minutesTime, hoursDuration, minutesDuration, hour, minutes);
    }
    @Override
    public int compareTo(Flight o) {
        return this.to.compareTo(o.to);
    }
}

class Airport implements Comparable<Airport>{
    String name;
    String country;
    String code;
    int passengers;
    List<Flight>flightFrom;
    List<Flight>comingFlight;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flightFrom = new ArrayList<>();
        this.comingFlight = new ArrayList<>();
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%s (%s)\n%s\n%d\n", name, code, country, passengers));
        flightFrom.sort(Comparator.comparing(Flight::getTo).thenComparing(Flight::getHoursTime));
        for (int i=0; i<flightFrom.size(); i++){
            sb.append(i+1+". "+flightFrom.get(i).toString());
            if(i+1!=flightFrom.size()){
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    public List<Flight> getFlights(String s){
        List<Flight>newlist=flightFrom.stream().filter(f -> f.to.equals(s)).collect(Collectors.toList());
        return newlist;
    }
    @Override
    public int compareTo(Airport o) {
        return this.code.compareTo(o.code);
    }
}

class Airports{
    Map<String, Airport>airportMap;

    public Airports() {
        this.airportMap=new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        Airport a=new Airport(name, country, code, passengers);
        airportMap.put(code, a);
    }
    public void addFlights(String from, String to, int time, int duration){
        Flight f=new Flight(from, to, time, duration);
        if(airportMap.containsKey(from)){
            airportMap.get(from).flightFrom.add(f);
        }
        if(airportMap.containsKey(to)){
            airportMap.get(to).comingFlight.add(f);
        }
    }
    public void showFlightsFromAirport(String code){
        if(airportMap.containsKey(code)){
            System.out.println(airportMap.get(code).toString());
        }
    }
    public void showDirectFlightsFromTo(String from, String to){
        if(airportMap.containsKey(from)&&airportMap.get(from).getFlights(to).size()!=0){
            List<Flight>flightList=airportMap.get(from).getFlights(to).stream().collect(Collectors.toList());
            flightList.stream().forEach(f -> System.out.println(f.toString()));
        }
        else {
            System.out.println(String.format("No flights from %s to %s", from, to));
        }
    }
    public void showDirectFlightsTo(String to){
        List<Flight>flightList=airportMap.values().stream().filter(a -> a.getFlights(to).size()>0)
                .flatMap(a -> a.getFlights(to).stream()).collect(Collectors.toList());
        flightList.sort(Comparator.comparing(Flight::getTo).thenComparing(Flight::getHoursTime).thenComparing(Flight::getMinutesTime).thenComparing(Flight::getMinutesDuration));
        flightList.forEach(flight -> System.out.println(flight.toString()));
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}
