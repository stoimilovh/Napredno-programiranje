//Да се имплемнтира генеричка класа MinMax од два споредливи објекти (минимум/максимум). За класата да се
//        имплементираат:
//
//        MinMax()-default конструктор
//        void update(T element) - метод за ажурирање на тековните минимум/максимум.
//        T max() - го враќа најголемиот елемент
//        T min() - го враќа најмалиот елемент
//        да се преоптовари методот toString() кој враќа стринг составен од минималниот и максималниот
//        елемент и бројот на елементи обработени во методот update кои се различни од тековниот
//        минимум/максимум, разделени со празно место.
//        Во класата не смеат да се чуваат елементите кои се обработуваат во методот update,
//        освен тековниот минимум/максимум.

import java.util.Scanner;

class MinMax<T extends Comparable<T>>{
    private T min;
    private T max;
    private int brojachMin, brojachMax, total=0;
    public MinMax(){
        this.min=null;
        this.max=null;
    }
    void update(T element){
        if(min==null || max==null){
            min=element;
            max=element;
        }
        if(element.compareTo(min)<0){
            min=element;
            brojachMin=1;
        }
        else if(element.compareTo(min)==0){
            brojachMin++;
        }
        if(element.compareTo(max)>0){
            max=element;
            brojachMax=1;
        }
        else if(element.compareTo(max)==0){
            brojachMax++;
        }
        total++;
    }
    T max(){
        return max;
    }
    T min(){
        return min;
    }

    @Override
    public String toString() {
        return min+" "+max+" "+(total-brojachMax-brojachMin)+"\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}