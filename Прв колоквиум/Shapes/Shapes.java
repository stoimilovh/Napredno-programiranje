//        Да се дефинира класа ShapesApplication во која се чуваат податоци за повеќе прозорци на кои се
//        исцртуваат геометриски слики во форма на квадрат.
//
//        За класата да се дефинира:
//        ShapesApplication() - конструктор
//        int readCanvases (InputStream inputStream) - метод којшто од влезен поток на податоци ќе
//        прочита информации за повеќе прозорци на кои се исцртуваат квадрати. Во секој ред од потокот
//        е дадена информација за еден прозорец во формат: canvas_id size_1 size_2 size_3 …. size_n,
//        каде што canvas_id е ИД-то на прозорецот, а после него следуваат големините на страните на
//        квадратите што се исцртуваат во прозорецот. Методот треба да врати цел број што означува
//        колку квадрати за сите прозорци се успешно прочитани.
//        void printLargestCanvasTo (OutputStream outputStream) - метод којшто на излезен поток ќе
//        го испечати прозорецот чии квадрати имаат најголем периметар. Печатењето да се изврши во
//        форматот canvas_id squares_count total_squares_perimeter.

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

class Square{
    String id;
    int niza[];
    int n;
    public Square(String id, int niza[], int n){
        this.id=id;
        this.niza=new int[niza.length];
        for(int i=0; i<niza.length; i++){
            this.niza[i]=niza[i];
        }
        this.n=n;
    }
    public String getId() {
        return id;
    }
    public int getNumber() {
        int zbir=0;
        for(int i=0; i<niza.length; i++){
            zbir+=4*niza[i];
        }
        return zbir;
    }
    public int getN() {
        return n;
    }
}
class ShapesApplication{
    Square sq[];
    int k;
    public ShapesApplication(){
        this.sq=new Square[100];
        k=0;
    }
    int readCanvases(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        int i, j=0, number;
        while (sc.hasNext()){
            String id=sc.next();
            int numbers[]=new int[100];
            i=0;
            while (sc.hasNextInt()){
                number=sc.nextInt();
                if(number>1000){
                    k+=i;
                    sq[j]=new Square(id, numbers, i);
                    j++;
                    id=Integer.toString(number);
                    i=0;
                }
                else {
                    numbers[i]=number;
                    i++;
                }
            }
            k+=i;
            sq[j]=new Square(id, numbers, i);
            j++;
        }
        return k;
    }
    void printLargestCanvasTo (OutputStream outputStream){
        int j=0, max=0;
        for(int i=0; i<sq.length; i++){
            if(sq[i]!=null&&sq[i].getNumber()>max){
                max=sq[i].getNumber();
                j=i;
            }
        }
        PrintStream out=new PrintStream(outputStream);
        out.println(sq[j].getId()+" "+sq[j].getN()+" "+sq[j].getNumber());
    }
}

public class Shapes {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}