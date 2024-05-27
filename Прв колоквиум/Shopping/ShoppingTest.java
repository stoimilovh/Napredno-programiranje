//Да се дефинира класа ShoppingCart за репрезентација на една потрошувачка кошничка во
//        која може да се наоѓаат ставки од 2 типа (ставка која содржи продукт кој се купува во целост,
//        или ставка која содржи продукт кој се купува на грам).
//
//        За класата ShoppingCart да се имплементираат следните методи:
//
//        конструктор
//        void addItem(String itemData) - метод за додавање на ставка во кошничката. Податоците за
//        ставката се дадени во текстуална форма и може да бидат во следните два формати согласно типот
//        на ставката:
//        WS;productID;productName;productPrice;quantity (quantity е цел број, productPrice се однесува
//        на цена на 1 продукт)
//        PS;productID;productName;productPrice;quantity (quantity е децимален број - во грамови,
//        productPrice се однесува на цена на 1 кг продукт)
//        Со помош на исклучок од тип InvalidOperationException да се спречи додавање на ставка
//        со quantity 0.
//        void printShoppingCart(OutputStream os) - метод за печатење на кошничката на излезен поток.
//        Потребно е да се испечатат сите ставки од кошничката подредени според вкупната цена во опаѓачки
//        редослед. Вкупната цена е производ на цената на продуктот кој е во ставката и квантитетот кој е
//        купен по таа цена.
//        void blackFridayOffer(List<Integer> discountItems, OutputStream os) - метод којшто ќе ја намали
//        цената за 10% на сите продукти чиј што productID се наоѓа во листата discountItems. Потоа, треба
//        да се испечати извештај за вкупната заштеда на секоја ставка каде има продукт на попуст
//        (види тест пример). Да се фрли исклучок од тип InvalidOperationException доколку листата  со
//        продукти на попуст е празна.
//
//
//        Напомена: Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети.
//        Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети
//        со најмногу 80% од поените.

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(message);
    }
}

abstract class Shopping{
    String id;
    String name;
    int price;
    float total;

    public Shopping(String id, String name, int price, float total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.total=total;
    }
    @Override
    public String toString() {
        return String.format("%s - %.2f", id, total);
    }
    public float getTotal() {
        return total;
    }
    public abstract void setTotalll();
    public abstract float getQ();
}

class ShoppingCart{
    List<Shopping> shoppings;

    public ShoppingCart() {
        this.shoppings = new ArrayList<>();
    }
    void addItem(String itemData) throws  InvalidOperationException{
        String []products=itemData.split(";");
        if(products[0].equals("WS")){
            String id=products[1];
            String name=products[2];
            int price=Integer.parseInt(products[3]);
            int quantity=Integer.parseInt(products[4]);
            float total=0;
            if(quantity==0){
                throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.", id));
            }
            total=price*quantity;
            ShoppingOne first=new ShoppingOne(id, name, price, total, quantity);
            shoppings.add(first);
        }
        else if(products[0].equals("PS")){
            String id=products[1];
            String name=products[2];
            int price=Integer.parseInt(products[3]);
            float quantity=Float.parseFloat(products[4]);
            if(quantity==0){
                throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.", id));
            }
            float total= (float) (price*(quantity/1000.0));
            ShoppingTwo first=new ShoppingTwo(id, name, price, total, quantity);
            shoppings.add(first);
        }
    }
    void printShoppingCart(OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        shoppings.stream().sorted(Comparator.comparingDouble(Shopping::getTotal).reversed()).forEach(shopping -> pw.println(shopping));
        pw.flush();
    }
    void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException{
        PrintWriter pw=new PrintWriter(os);
        boolean flag=false;
        if(discountItems.isEmpty()){
            throw new InvalidOperationException("There are no products with discount.");
        }
        for (Shopping shops:shoppings) {
            for(int i=0; i<discountItems.size(); i++){
                int id=Integer.parseInt(shops.id);
                if(id==discountItems.get(i)){
                    float sum= (float) (shops.total-(shops.total-(shops.total*0.1)));
                    pw.println(String.format("%s - %.2f", id, sum));
                    flag=true;
                }
            }
        }
        pw.flush();
    }
}

class ShoppingOne extends Shopping{
    int quantity;

    public ShoppingOne(String id, String name, int price, float total, int quantity) {
        super(id, name, price, total);
        this.quantity = quantity;
        this.total = total;
    }
    public void setTotalll(){
        this.total=price*quantity;
    }
    public float getQ() {
        return quantity;
    }
}

class ShoppingTwo extends Shopping{
    float quantity;

    public ShoppingTwo(String id, String name, int price, float total, float quantity) {
        super(id, name, price, total);
        this.quantity = quantity;
    }
    public void setTotalll(){
        this.total= (float) (price*(quantity/1000.0));
    }
    public float getQ() {
        return quantity;
    }
}


public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}