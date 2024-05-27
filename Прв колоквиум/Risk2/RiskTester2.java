//Да се дефинира класа Risk со единствен метод void processAttacksData (InputStream is).
//
//        Методот од влезен поток ги чита информациите за извршените напади на еден играч врз
//        другите играчи во стратешката игра Ризик.
//
//        За секој поединечен напад информациите ќе се дадени во посебен ред и ќе бидат во следниот
//        формат: X1 X2 X3;Y1 Y2 Y3, каде што X1, X2 и X3 се броеви добиени со фрлање на 3 коцки
//        (број од 1-6) на напаѓачот, а Y1, Y2 и Y3 се броеви добиени со фрлање на 3 коцки (број од 1-6)
//        за одбрана. Потребно е да се испечати бројот на преостанати војници на напаѓачот и нападнатиот,
//        по завршувањето на нападот.
//
//        Без разлика на редоследот на фрлените коцки, бројот на преостанати војници се смета на следниот
//        начин. Се почнува од најголемиот број кај напаѓачот и нападнатиот, се прави споредба и преживува
//        војникот на оној кој има поголем број. Оваа постапка се прави и понатаму, додека не се изминат
//        сите фрлени броеви.
//
//        Пример за влезот: 5 3 4; 2 4 1 ќе се испечати 3 0, бидејќи најголемата вредност на напаѓачот
//        (5) е поголема од најголемата вредност на нападнатиот (4) -> значи преживеал +1 војник на
//        напаѓачот, втората најголема вредност на напаѓачот (4) е поголема од втората најголема вредност
//        на нападнатиот (2) -> значи преживеал +1 војник на напаѓачот, третата најголема вредност (3) на
//        напаѓачот е поголема од третата најголемата вредност на нападнатиот (1) -> значи преживеал +1
//        војник на напаѓачот. Така, преживеале 3 војници на напаѓачот, а 0 на нападнатиот.

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

class Risk{
    public void processAttacksData (InputStream is) {
        int a, b, c, d, e, f;
        Scanner sc = new Scanner(is);
        while (sc.hasNext()) {
            int counter1 = 0;
            int counter2 = 0;
            String hs = sc.nextLine();
            String[] niza = hs.split(" |\\;");
            a = Integer.parseInt(niza[0]);
            b = Integer.parseInt(niza[1]);
            c = Integer.parseInt(niza[2]);
            d = Integer.parseInt(niza[3]);
            e = Integer.parseInt(niza[4]);
            f = Integer.parseInt(niza[5]);
            int[] set1 = {a, b, c};
            int[] set2 = {d, e, f};
            Arrays.sort(set1);
            Arrays.sort(set2);
            a = set1[0];
            b = set1[1];
            c = set1[2];
            d = set2[0];
            e = set2[1];
            f = set2[2];
            if(a>d){
                counter1++;
            }
            else{
                counter2++;
            }
            if(b>e){
                counter1++;
            }
            else{
                counter2++;
            }
            if(c>f){
                counter1++;
            }
            else{
                counter2++;
            }
            System.out.println(counter1+" "+counter2);
        }
    }
}

public class RiskTester2 {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}