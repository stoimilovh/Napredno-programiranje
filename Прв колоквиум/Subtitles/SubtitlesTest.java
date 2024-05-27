//Да се имплементира класа Subtitles која ќе чита од влезен тек (стандарден влез, датотека, ...)
//        превод во стандарден srt формат. Секој еден елемент од преводот се состои од реден број,
//        време на почеток на прикажување, време на крај на прикажување и текст и е во следниот формат (пример):
//
//        2
//        00:00:48,321 --> 00:00:50,837
//        Let's see a real bet.
//        Делот со текстот може да има повеќе редови. Сите елементи се разделени со еден нов ред.
//
//        Ваша задача е да ги имплементирате методите:
//
//        Subtitles() - default конструктор
//        int loadSubtitles(InputStream inputStream) - метод за читање на преводот (враќа резултат колку елементи се прочитани)
//        void print() - го печати вчитаниот превод во истиот формат како и при читањето.
//        void shift(int ms) - ги поместува времињата на сите елементи од преводот за бројот на милисекунди кои се
//        проследува како аргумент (може да биде негативен, со што се поместуваат времињата наназад).

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Title{
    int num;
    double timeA;
    double timeB;
    List<String> subtitle;

    public Title(int num, double timeA, double timeB, List<String> subtitle) {
        this.num = num;
        this.timeA = timeA;
        this.timeB = timeB;
        this.subtitle = subtitle;
    }
    public String printNumber(double time){
        int hours, minutes, seconds;
        int total=(int)Math.floor(time);
        hours=(total/3600);
        total=total%3600;
        minutes=total/60;
        seconds=total%60;
        double ms=((time-Math.floor(time))*1000);
        return String.format("%02d:%02d:%02d,%03.0f", hours, minutes, seconds, ms);
    }
    public double getTimeA() {
        return timeA;
    }
    public void setTimeA(double timeA) {
        this.timeA = timeA;
    }
    public double getTimeB() {
        return timeB;
    }
    public void setTimeB(double timeB) {
        this.timeB = timeB;
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        String t1=printNumber(timeA);
        String t2=printNumber(timeB);
        sb.append(String.format("%d\n%s --> %s\n", num, t1, t2));
        for (String title:subtitle) {
            sb.append(title).append("\n");
        }
        return sb.toString();
    }
}
class Subtitles{
    List<Title> titles;

    public Subtitles() {
        this.titles = new ArrayList<>();
    }
    public int loadSubtitles(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        while (sc.hasNext()){
            int num=Integer.parseInt(sc.nextLine());
            List<String>prevod=new ArrayList<>();
            String time=sc.nextLine();
            String title=sc.nextLine();
            while (!title.isEmpty()){
                prevod.add(title);
                if(sc.hasNext()){
                    title=sc.nextLine();
                }
                else{
                    break;
                }
            }
            String []timee=time.split(" --> ");
            String []startTime=timee[0].split(":");
            String []endTime=timee[1].split(":");
            String []msS=startTime[2].split(",");
            String []msE=endTime[2].split(",");
            int msStart=Integer.parseInt(msS[1]);
            int msEnd=Integer.parseInt(msE[1]);
            double timeA=(Integer.parseInt(startTime[0])*3600)+
                    (Integer.parseInt(startTime[1])*60)+
                    Integer.parseInt(msS[0])+
                    (msStart/1000.0);
            double timeB=(Integer.parseInt(endTime[0])*60*60)+
                    (Integer.parseInt(endTime[1])*60)+
                    Integer.parseInt(msE[0])+
                    (msEnd/1000.0);
            Title title1=new Title(num, timeA, timeB, prevod);
            titles.add(title1);
        }
        return titles.size();
    }
    public void print(){
        for (Title title:titles) {
            System.out.println(title.toString());
        }
    }
    public void shift(int ms){
        for (Title title:titles) {
            double a, b, mss, ta, tb;
            mss=ms/1000.0;
            a=title.getTimeA();
            b=title.getTimeB();
            ta=a+mss;
            tb=b+mss;
            title.setTimeA(ta);
            title.setTimeB(tb);
        }
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}