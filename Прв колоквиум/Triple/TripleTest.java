//Да се имплемнтира генеричка класа Triple (тројка) од нумерички вредности (три броја).
//        За класата да се имплементираат:
//
//        конструктор со 3 аргументи,
//        double max() - го враќа најголемиот од трите броја
//        double average() - кој враќа просек на трите броја
//        void sort() - кој ги сортира елементите во растечки редослед
//        да се преоптовари методот toString() кој враќа форматиран стринг со две децимални
//        места за секој елемент и празно место помеѓу нив.

import java.util.Scanner;
class Triple<E extends Comparable<E>>{
    double a, b, c;
    public Triple(double a, double b, double c){
        this.a=a;
        this.b=b;
        this.c=c;
    }
    public double max(){
        this.sort();
        return c;
    }
    public double avarage(){
        return (a+b+c)/3.00;
    }
    void sort(){
        double tmp;
        if(a>b){
            tmp=b;
            b=a;
            a=tmp;
        }
        if(b>c){
            tmp=c;
            c=b;
            b=tmp;
        }
        if(a>c){
            tmp=c;
            c=a;
            a=tmp;
        }
        if(a>b){
            tmp=a;
            a=b;
            b=tmp;
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", a, b, c);
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}


