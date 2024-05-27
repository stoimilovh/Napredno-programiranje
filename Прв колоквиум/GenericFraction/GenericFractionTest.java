//Треба да се развие генеричка класа за работа со дропки.
//        Класата GenericFraction има два генерички параметри T и U кои мора да бидат од некоја
//        класа која наследува од класата Number. GenericFraction има две променливи:
//
//        numerator - броител
//        denominator - именител.
//        Треба да се имплементираат следните методи:
//
//        GenericFraction(T numerator, U denominator) - конструктор кој ги иницијализира броителот
//        и именителот на дропката. Ако се обидиме да иницијализираме дропка со 0 вредност за именителот
//        треба да се фрли исклучок од тип ZeroDenominatorException
//        GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) -
//        собирање на две дропки
//        double toDouble() - враќа вредност на дропката како реален број
//        toString():String - ја печати дропката во следниот формат [numerator] / [denominator],
//        скратена (нормализирана) и секој со две децимални места.

import java.util.Scanner;

class ZeroDenominatorException extends Exception{
    @Override
    public String getMessage() {
        return "Denominator cannot be zero";
    }
}

class GenericFraction<T extends Number, U extends Number>{
    T t;
    U u;
    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if(denominator.equals(0)){
            throw new ZeroDenominatorException();
        }
        t=numerator;
        u=denominator;
    }
    public double toDouble(){
        return t.doubleValue()/u.doubleValue();
    }
    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException{
        double imenitel=0;
        if(u.doubleValue()==gf.u.doubleValue()){
            imenitel=u.doubleValue()/2;
        }
        else if(gf.u.doubleValue()%u.doubleValue()==0){
            imenitel=gf.u.doubleValue();
        }
        else{
            imenitel=u.doubleValue()*gf.u.doubleValue();
        }
        double broitel=((imenitel/u.doubleValue())*t.doubleValue())+((imenitel/gf.u.doubleValue())*gf.t.doubleValue());
        if(imenitel==0){
            throw new ZeroDenominatorException();
        }
        for(int i=(int) broitel; i>0; i--){
            if(broitel%i==0&&imenitel%i==0){
                broitel=broitel/i;
                imenitel=imenitel/i;
            }
        }
        return new GenericFraction<>(broitel, imenitel);
    }
    @Override
    public String toString() {
        return String.format("%.2f / %.2f", t.doubleValue(), u.doubleValue());
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}
