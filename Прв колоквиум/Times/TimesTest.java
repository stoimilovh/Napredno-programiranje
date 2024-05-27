//Да се имплементира класа TimeTable која ќе чита од влезен тек (стандарден влез, датотека, ...) податоци за времиња во 24-часовен формат. Сите времиња се разделени со едно празно место, а во самото време часот и минутите може да бидат разделени со : или .. Пример за форматот на податоците:
//
//        11:15 0.45 23:12 15:29 18.46
//
//        Ваша задача е да ги имплементирате методите:
//
//        TimeTable() - default конструктор
//        void readTimes(InputStream inputStream) - метод за читање на податоците
//        void writeTimes(OutputStream outputStream, TimeFormat format) - метод кој ги печати сите времиња
//        сортирани во растечки редослед во зададениот формат (24 часовен или AM/PM).
//        Методот за читање readTimes фрла исклучоци од тип UnsupportedFormatException ако времињата се
//        разделени со нешто друго што не е : или . и InvalidTimeException ако времето (часот или минутите)
//        е надвор од дозволениот опсег (0-23, 0-59). И двата исклучоци во пораката getMessage() треба да
//        го вратат влезниот податок кој го предизвикал исклучокот. Сите времиња до моментот кога ќе се
//        фрли некој од овие два исклучоци треба да си останат вчитани.
//        Правила за конверзија од 24-часовен формат во AM/PM:
//
//        за првиот час од денот (0:00 - 0:59), додадете 12 и направете го "AM"
//        од 1:00 до 11:59, само направето го "AM"
//        од 12:00 до 12:59, само направето го "PM"
//        од 13:00 до 23:59 одземете 12 и направете го "PM"

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

class UnsupportedFormatException extends Exception{
    String str;
    public UnsupportedFormatException(String str){
        this.str=str;
    }
    public String getMessage(){
        return str;
    }
}

class InvalidTimeException extends Exception {
    String str;
    public InvalidTimeException(String str) {
        this.str=str;
    }
    public String getMessage(){
        return str;
    }
}

class TimeTable{
    List<Integer>niza;
    public TimeTable(){
        this.niza=new ArrayList<>();
    }
    void readTimes(InputStream inputStream)throws UnsupportedFormatException, InvalidTimeException{
        Scanner sc=new Scanner(inputStream);
        String str;
        while (sc.hasNext()){
            str=sc.nextLine();
            String []ss=str.split(" ");
            if(ss.length==1){
                String []timeparts=str.split(":|\\.");
                if(timeparts.length==2){
                    int hours=Integer.parseInt(timeparts[0]);
                    int minutes=Integer.parseInt(timeparts[1]);
                    if(hours>23||minutes>59||hours<0||minutes<0){
                        throw new InvalidTimeException(str);
                    }
                    int seconds=(60*60*hours)+(minutes*60);
                    niza.add(seconds);
                }
                else {
                    throw new UnsupportedFormatException(str);
                }
            }
            else{
                for(int i=0; i<ss.length; i++){
                    String []timeparts=ss[i].split(":|\\.");
                    if(timeparts.length==2){
                        int hours=Integer.parseInt(timeparts[0]);
                        int minutes=Integer.parseInt(timeparts[1]);
                        if(hours>23||minutes>59||hours<0||minutes<0){
                            throw new InvalidTimeException(str);
                        }
                        int seconds=(60*60*hours)+(minutes*60);
                        niza.add(seconds);
                    }
                    else {
                        throw new UnsupportedFormatException(ss[i]);
                    }
                }
            }
        }
    }
    void writeTimes(OutputStream outputStream, TimeFormat format){
        PrintStream out=new PrintStream(outputStream);
        Collections.sort(niza);
        for(int i=0; i<niza.size(); i++){
            int hours=(niza.get(i)/60)/60;
            int minutes=(niza.get(i)/60)%60;
            if(format==TimeFormat.FORMAT_24){
                if(hours<10){
                    out.printf(" %01d:%02d\n", hours, minutes);
                }
                else{
                    out.printf("%02d:%02d\n", hours, minutes);
                }
            }
            else if(format==TimeFormat.FORMAT_AMPM) {
                String formatt;
                if(hours<12){
                    formatt="AM";
                }
                else {
                    formatt="PM";
                }
                if(hours>12){
                    hours-=12;
                }
                if(hours==0){
                    hours=12;
                }
                if(hours<10){
                    out.printf(" %01d:%02d %s\n", hours, minutes, formatt);
                }
                else {
                    out.printf("%02d:%02d %s\n", hours, minutes, formatt);
                }
            }
        }
        out.flush();
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}