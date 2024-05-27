//Да се дефинира класа Risk со единствен метод int processAttacksData (InputStream is).
//
//        Методот од влезен поток ги чита информациите за извршените напади на еден играч врз другите
//        играчи во стратешката игра Ризик. За секој поединечен напад информациите ќе се дадени во
//        посебен ред и ќе бидат во следниот формат:
//
//        X1 X2 X3;Y1 Y2 Y3, каде што X1, X2 и X3 се броеви добиени со фрлање на 3 коцки (број од 1-6)
//        на напаѓачот, а Y1, Y2 и Y3 се броеви добиени со фрлање на 3 коцки (број од 1-6) за одбрана.
//        Како резултат методот треба да го врати бројот на целосно успешнo извршени напади.
//
//        Еден напад се смета дека е целосно успешен доколку сите коцки фрлени од напаѓачот имаат број
//        поголем од бројот на фрлените коцки на нападнатиот (најголемиот број на фрлените коцки на
//        напаѓачот е поголем од најголемиот број на фрлените коцки на нападнатиот и соодветно за сите
//        останати обиди (редоследот на фрлените коцки не игра улога).
//
//        Пример влезот: 5 3 4; 2 4 1 се смета за целосно успешен напад бидејќи најголемата вредност на
//        напаѓачот (5) е поголема од најголемата вредност на нападнатиот (4), втората најголема вредност
//        на напаѓачот (4) е поголема од втората најголема вредност на нападнатиот (2), како и третата
//        најголема вредност (3) на напаѓачот е поголема од третата најголемата вредност на нападнатиот (1).



import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

class Risk{
    public int processAttacksData (InputStream is){
        int counter=0;
        int a, b, c, d, e, f;
        Scanner sc=new Scanner(is);
        while (sc.hasNext()){
            String hs=sc.nextLine();
            String []niza=hs.split(" |\\;");
            a=Integer.parseInt(niza[0]);
            b=Integer.parseInt(niza[1]);
            c=Integer.parseInt(niza[2]);
            d=Integer.parseInt(niza[3]);
            e=Integer.parseInt(niza[4]);
            f=Integer.parseInt(niza[5]);
            int []set1={a, b, c};
            int []set2={d, e, f};
            Arrays.sort(set1);
            Arrays.sort(set2);
            a=set1[0];
            b=set1[1];
            c=set1[2];
            d=set2[0];
            e=set2[1];
            f=set2[2];
            if(a>d&&b>e&&c>f){
                counter++;
            }
        }
        return counter;
    }
}

public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}