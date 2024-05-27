//Да се имплементира класа Canvas на која ќе чуваат различни форми. За секоја форма се чува:
//
//        id:String
//        color:Color (enum дадена)
//        Притоа сите форми треба да имплментираат два интерфејси:
//
//        Scalable - дефиниран со еден метод void scale(float scaleFactor) за соодветно
//        зголемување/намалување на формата за дадениот фактор
//        Stackable - дефиниран со еден метод float weight() кој враќа тежината на формата
//        (се пресметува како плоштина на соодветната форма)
//        Во класата Canvas да се имплементираат следните методи:
//
//        void add(String id, Color color, float radius) за додавање круг
//        void add(String id, Color color, float width, float height) за додавање правоаголник
//        При додавањето на нова форма, во листата со форми таа треба да се смести на соодветното
//        место според нејзината тежина. Елементите постојано се подредени според тежината во опаѓачки
//        редослед.
//
//        void scale(String id, float scaleFactor) - метод кој ја скалира формата со даденото id за
//        соодветниот scaleFactor. Притоа ако има потреба, треба да се изврши преместување на
//        соодветните форми, за да се задржи подреденоста на елементите.
//        Не смее да се користи сортирање на листата.
//
//        toString() - враќа стринг составен од сите фигури во нов ред. За секоја фигура се додава:
//
//        C: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно] ако е круг
//
//        R: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно] ако е правоаголник
//        Користење на instanceof ќе се смета за неточно решение


import java.util.*;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}

interface Stackable{
    float weight();
}

class Canvas{
    List<Form> forms;

    public Canvas() {
        this.forms = new ArrayList<>();
    }

    void add(String id, Color color, float radius){
        Krug k=new Krug(id, color, radius);
        addInOrder(k);
    }
    void add(String id, Color color, float width, float height){
        Pravoagolnik p=new Pravoagolnik(id, color, width, height);
        addInOrder(p);
    }
    public void addInOrder(Form form) {
        int i = forms.size();
        while (i > 0 && forms.get(i - 1).weight() < form.weight()) {
            i--;
        }
        forms.add(i, form);
    }
    void scale(String id, float scaleFactor){
        for (Form f:forms) {
            if(f.id.equals(id)){
                f.scale(scaleFactor);
            }
        }
        List<Form>reordered=new ArrayList<>(forms);
        forms.clear();
        for (Form f:reordered) {
            addInOrder(f);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (Form f:forms) {
            sb.append(f).append("\n");
        }
        return sb.toString();
    }
}

abstract class Form implements Scalable, Stackable{
    String id;
    Color color;

    public Form(String id, Color color) {
        this.id = id;
        this.color = color;
    }
    @Override
    abstract public void scale(float scaleFactor);
    @Override
    abstract public float weight();

    @Override
    abstract public String toString();
}

class Krug extends Form{
    float radius;

    public Krug(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }
    @Override
    public void scale(float scaleFactor) {
        radius*=scaleFactor;
    }
    @Override
    public float weight() {
        return (float) (radius*radius*Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f", id, color, weight());
    }
}

class Pravoagolnik extends Form{
    float width, height;

    public Pravoagolnik(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }
    @Override
    public void scale(float scaleFactor) {
        width*=scaleFactor;
        height*=scaleFactor;
    }
    @Override
    public float weight() {
        return width*height;
    }
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f", id, color, weight());
    }
}


public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
