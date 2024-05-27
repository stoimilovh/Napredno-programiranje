//Да се имплементира класа LogCollector за чување и анализирање на логовите на сервисите
//        (и на микросервисите кои се дел од истите) на една компанија. За класата да се
//        обезбедат следните методи:
//
//        Потребни конструктори
//        void addLog (String log) - метод за додавање на лог во колекторот на логови.
//        Логот е потребно да се парсира и истиот секогаш е во следниот формат: service_name
//        microservice_name type message timestamp, каде што type може да биде INFO, WARN и ERROR.
//        void printServicesBySeverity() - метод кој ќе ги испечати сите сервиси за кои колекторот
//        има собрано логови сортирани според просечната сериозност (анг. severity) на сите логовите
//        произведени од тој сервис во опаѓачки редослед.
//        Map<Integer, Integer> getSeverityDistribuition (String service, String microservice) -
//        метод кој враќа мапа од нивоата на сериозност детектирани во логовите на микросервисот
//        microservice кој се наоѓа во сервисот service со бројот на логови кои ја имаат соодветната
//        сериозност. Доколку microservice e null, потребно е резултатот да се однесува на сите логови
//        од конкретниот сервис (без разлика на микросервисот).
//        void displayLogs(String service, String microservice, String order)- метод кој ги печати
//        логовите на микросервисот microservice кој се наоѓа во сервисот service сортирани според
//        правилото дадено во order. Правилото може да ги има следните 4 вредности:
//        NEWEST_FIRST (се печатат најновите логови прво)
//        OLDEST_FIRST (се печатат најстарите логови прво)
//        MOST_SEVERE_FIRST (се печатат логови со најголема сериозност прво)
//        LEAST_SEVERE_FIRST (се печатат логови со најмала сериозност прво)
//        Сериозноста на логовите е цел број кој зависи од типот на логот и содржината на пораката и
//        таа се пресметува на следниот начин:
//
//        логовите од тип INFO секогаш имаат сериозност 0
//        логовите од тип WARN секогаш имаат сериозност 1, а доколку во пораката се содржи и текстот
//        might cause error, сериозноста се зголемува за 1
//        логовите од тип ERROR секогаш имаат сериозност 3, а доколку во пораката се содржи и текстот:
//        fatal - сериозноста се зголемува за 2
//        exception - сериозноста се зголемува за 3
//        Решенија кои не користат правилни приципи од ООП за пресметка на сериозноста на различните
//        типови на логови ќе бидат оценети со најмногу 70% од предвидените поени за задачата!

import java.util.*;
import java.util.stream.Collectors;

abstract class Log{
    String name;
    String microserviceName;
    String message;
    long timestamp;

    public Log(String name, String microserviceName, String message, long timestamp) {
        this.name = name;
        this.microserviceName = microserviceName;
        this.message = message;
        this.timestamp = timestamp;
    }
    public String getName() {
        return name;
    }
    public String getMicroserviceName() {
        return microserviceName;
    }
    public String getMessage() {
        return message;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public abstract int severity();

    @Override
    public abstract String toString();
}

class InfoLog extends Log{
    public InfoLog(String name, String microserviceName, String message, long timestamp) {
        super(name, microserviceName, message, timestamp);
    }
    public int severity(){
        return 0;
    }
    @Override
    public String toString() {
        return String.format("%s|%s [INFO] %s T:%d", getName(), getMicroserviceName(), getMessage(), getTimestamp());
    }
}

class WarnLog extends Log{
    public WarnLog(String name, String microserviceName, String message, long timestamp) {
        super(name, microserviceName, message, timestamp);
    }
    public int severity(){
        if(message.contains("might cause error")){
            return 2;
        }
        return 1;
    }
    @Override
    public String toString() {
        return String.format("%s|%s [WARN] %s T:%d", getName(), getMicroserviceName(), getMessage(), getTimestamp());
    }
}

class ErrorLog extends Log{
    public ErrorLog(String name, String microserviceName, String message, long timestamp) {
        super(name, microserviceName, message, timestamp);
    }
    public int severity(){
        if(message.contains("fatal")&&message.contains("exception")){
            return 8;
        }
        if(message.contains("fatal")){
            return 5;
        }
        if(message.contains("exception")){
            return 6;
        }
        return 3;
    }
    @Override
    public String toString() {
        return String.format("%s|%s [ERROR] %s T:%d", getName(), getMicroserviceName(), getMessage(), getTimestamp());
    }
}

class LogFactory{
    static Log createLog(String line){
        String []parts=line.split(" ");
        String message="";
        for(int i=3; i<=parts.length-1; i++){
            message+=parts[i];
            if(i+1!=parts.length){
                message+=" ";
            }
        }
        if(parts[2].equals("ERROR")){
            return new ErrorLog(parts[0], parts[1], message, Long.parseLong(parts[parts.length-1]));
        }
        if(parts[2].equals("WARN")){
            return new WarnLog(parts[0], parts[1], message, Long.parseLong(parts[parts.length-1]));
        }
        else{
            return new InfoLog(parts[0], parts[1], message, Long.parseLong(parts[parts.length-1]));
        }
    }
}

class ComparatorFactory {
    public static Comparator<Log> createComparator(String type) {
        switch (type) {
            case "NEWEST_FIRST":
                return Comparator.comparing(Log::getTimestamp).thenComparing(Log::getTimestamp).reversed();
            case "OLDEST_FIRST":
                return Comparator.comparing(Log::getTimestamp);
            case "MOST_SEVERE_FIRST":
                return Comparator.comparing(Log::severity).thenComparing(Log::getTimestamp).reversed();
            case "LEAST_SEVERE_FIRST":
                return Comparator.comparing(Log::severity);
            default:
                return Comparator.comparing(Log::getName);
        }
    }
}

class LogCollector{
    Map<String, List<Log>>serviceLogMap;
    Map<String, List<Log>>microserviceLogMap;
    Comparator<Map.Entry<String, List<Log>>>comparator=
            Comparator.comparingDouble(e -> e.getValue().stream().mapToDouble(Log::severity).average().getAsDouble());

    public LogCollector() {
        this.serviceLogMap=new HashMap<>();
        this.microserviceLogMap=new HashMap<>();
    }
    void addLog (String logg){
        Log log=LogFactory.createLog(logg);
        serviceLogMap.computeIfAbsent(log.getName(), k -> new ArrayList<>()).add(log);
        microserviceLogMap.computeIfAbsent(log.getMicroserviceName(), k -> new ArrayList<>()).add(log);
    }
    void printServicesBySeverity(){
        serviceLogMap.entrySet().stream().sorted(comparator.reversed()).forEach(e -> System.out.println(print(e.getKey())));
    }
    Map<Integer, Integer> getSeverityDistribution (String service, String microservice){
        Map<Integer, Integer>map=new HashMap<>();
        if(microservice==null){
            List<Log>logList=serviceLogMap.get(service);
            for (Log l:logList){
                if(map.containsKey(l.severity())){
                    int num=map.get(l.severity());
                    num++;
                    map.put(l.severity(), num);
                }
                else {
                    map.put(l.severity(), 1);
                }
            }
        }
        else{
            List<Log>logList=microserviceLogMap.get(microservice);
            for (Log l:logList){
                if(l.getName().equals(service)&&map.containsKey(l.severity())){
                    int num=map.get(l.severity());
                    num++;
                    map.put(l.severity(), num);
                }
                else if(l.getName().equals(service)){
                    map.put(l.severity(), 1);
                }
            }
        }
        return map;
    }
    void displayLogs(String service, String microservice, String order){
        if (microservice == null){
            microserviceLogMap.values().stream().flatMap(Collection::stream)
                    .filter(log -> log.getName().equals(service))
                    .sorted(ComparatorFactory.createComparator(order)).
                    forEach(System.out::println);
        }
        else{
            microserviceLogMap.get(microservice).stream().filter(log -> log.getName().equals(service))
                    .sorted(ComparatorFactory.createComparator(order))
                    .forEach(System.out::println);
        }
    }
    public String print(String service) {
        int microSev=serviceLogMap.get(service).stream().map(Log::getMicroserviceName).collect(Collectors.toSet()).size();
        int totalLogs=serviceLogMap.get(service).size();
        double average=serviceLogMap.get(service).stream().mapToDouble(Log::severity).average().getAsDouble();
        double averageSev=(double)serviceLogMap.get(service).size()/serviceLogMap.get(service).stream().map(Log::getMicroserviceName).collect(Collectors.toSet()).size();
        return String.format("Service name: %s Count of microservices: %d Total logs in service: " +
                        "%d Average severity for all logs: %.2f Average number of logs per microservice: %.2f",
                service, microSev, totalLogs, average, averageSev);
    }
}

public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k,v)-> System.out.printf("%d -> %d%n", k,v));
            } else if (line.startsWith("displayLogs")){
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                System.out.println(line);

                collector.displayLogs(service, microservice, order);
            }
        }
    }
}