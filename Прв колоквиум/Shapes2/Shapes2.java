//        Да се дефинира класа ShapesApplication чување на податоци за повеќе прозорци на кои и се
//                сцртуваат геометриски слики во различна форма (квадрати и кругови)..
//
//        За класата да се дефинира:
//
//        ShapesApplication(double maxArea) - конструктор, каде maxArea е најголемата дозволена плоштина на
//                секоја форма поединечно, која може да биде исцртана на прозорците.
//        void readCanvases (InputStream inputStream) - метод којшто од влезен поток на податоци ќе прочита
//                информации за повеќе прозорци на кои се исцртуваат различните геометриски слики. Во секој
//                ред се наоѓа информација за еден прозорец во формат: canvas_id type_1 size_1 type_2 size_2
//                type_3 size_3 …. type_n size_n каде што canvas_id е ИД-то на прозорецот, a после него
//                следуваат информации за секоја форма во прозорецот. Секоја форма е означена со карактер
//                што го означува типот на геометриската слика (S = square, C = circle) и со големината на
//                страната на квадратот, односно радиусот на кругот.
//        При додавањето на геометриските слики на прозорецот треба да се спречи креирање и додавање на
//                прозорец во кој има форма што има плоштина поголема од максимално дозволената. Како
//                механизам за спречување треба да се користи исклучок од тип IrregularCanvasException
//                (фрлањето на исклучокот не треба да го попречи вчитувањето на останатите прозорци и
//                геометриски слики. Да се испечати порака Canvas [canvas_id] has a shape with area larger
//                than [max_area].
//        void printCanvases (OutputStream os) - метод којшто на излезен поток ќе ги испечати информациите
//                а сите прозорци во апликацијата. Прозорците да се сортирани во опаѓачки редослед според
//                сумата на плоштините на геометриските слики во нив. Секој прозорец да е испечатен во следниот
//                формат: ID total_shapes total_circles total_squares min_area max_area average_area.
//        За вредноста на PI користете ja константата Math.PI. За постигнување на точност со тест примерите
//                користете double за сите децимални променливи.


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

class IrregularCanvasException extends Exception{
    String id;
    double maxArea;
    public IrregularCanvasException(String id, double maxArea){
        this.id=id;
        this.maxArea=maxArea;
    }

    public String getMessage() {
        return String.format("Canvas %s has a shape with area larger than %.2f", id,maxArea);
    }
}

class GeometricFigures{
    String id;
    List<Object> niza;
    double max, min, prosek, sum;
    public GeometricFigures(String id, ArrayList<Object> niza, double max, double min, double prosek, double sum){
        this.id=id;
        this.niza=niza;
        this.max=max;
        this.min=min;
        this.prosek=prosek;
        this.sum=sum;
    }
    public String getName() {
        return id;
    }
    public List<Object> getNiza() {
        return niza;
    }
    public int circle(){
        int counter=0;
        for (Object nizi:niza) {
            if (nizi.equals("C")){
                counter++;
            }
        }
        return counter;
    }
    public int sq(){
        int counter=0;
        for (Object nizi:niza) {
            if (nizi.equals("S")){
                counter++;
            }
        }
        return counter;
    }
}
class ShapesApplication{
    private List<GeometricFigures> gfig;
    private double maxArea;
    public ShapesApplication(double maxArea){
        this.maxArea=maxArea;
        gfig=new ArrayList<>();
    }
    public void sortFigures(){
        gfig.sort(Comparator.comparingDouble(figure -> figure.sum));
        Collections.reverse(gfig);
    }
    public void readCanvases (InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        while (sc.hasNext()){
            boolean flag=true;
            String []str=sc.nextLine().split(" ");
            String id=str[0];
            List<Object>niza=new ArrayList<>();
            int counter=0;
            double max=0, min=9999, prosek=0, sum=0;
            double numm=0;
            for(int i=1; i<str.length; i+=2){
                String ch=str[i];
                int num=Integer.parseInt(str[i+1]);
                try{
                    if((ch.equals("C")&&volumenC(num)>maxArea)||(ch.equals("S")&&volumenS(num)>maxArea)){
                        throw new IrregularCanvasException(id, maxArea);
                    }
                    else{
                        if (ch.equals("C")){
                            numm=volumenC(num);
                            prosek+=numm;
                            counter++;
                        }
                        else if (ch.equals("S")){
                            numm=volumenS(num);
                            prosek+=numm;
                            counter++;
                        }
                        if(numm>max){
                            max=numm;
                        }
                        if(numm<min){
                            min=numm;
                        }
                        niza.add(ch);
                        niza.add(num);
                    }
                }
                catch (IrregularCanvasException e){
                    System.out.println(e.getMessage());
                    flag=false;
                    break;
                }
            }
            if(flag){
                sum=prosek;
                prosek=prosek/counter;
                GeometricFigures gf=new GeometricFigures(id,(ArrayList<Object>) niza, max, min, prosek, sum);
                gfig.add(gf);
            }
        }
    }
    void printCanvases (OutputStream os){
        PrintStream out=new PrintStream(os);
        sortFigures();
        for (GeometricFigures gf:gfig) {
            int num=gf.circle()+gf.sq();
            out.println(String.format("%s %d %d %d %.2f %.2f %.2f", gf.getName(), num, gf.circle(), gf.sq(), gf.min, gf.max, gf.prosek));
        }
    }
    double volumenC(int num){
        return Math.PI*num*num;
    }
    double volumenS(int num){
        return num*num;
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}