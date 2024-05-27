//Да се имплементира класа DeliveryApp која ќе моделира една апликација за нарачки и
//        достава на храна од ресторани. Во класата да се имплементираат следните методи:
//
//        Конструктор DeliveryApp (String name)
//        Метод void registerDeliveryPerson (String id, String name, Location currentLocation) - методот
//        за регистрирање на слободен доставувач кој сака да работи за апликацијата.
//        Метод void addRestaurant (String id, String name, Location location) - метод за додавање на
//        ресторан кој сака да овозможи достава на ставките од своето мени
//        Метод void addUser (String id, String name) - метод за регистрирање на корисник кој сака да
//        ја користи апликацијата за нарачка и достава на храна
//        Метод void addAddress (String id, String addressName, Location location) - метод за додавање
//        на адреса на корисникот со ИД id. Еден корисник може да има повеќе адреси (пр. Дома, работа и сл.)
//        метод void orderFood(String userId, String userAddressName, String restaurantId, float cost) -
//        метод за нарачка на храна на корисникот со ID userID на неговата адреса userAddressName од
//        ресторантот со ID restaurantId.
//
//        При процесирање на нарачката потребно е прво да се најде доставувач кој ќе ја достави нарачката
//        до клиентот. Нарачката се доделува на доставувачот кој е најблиску до ресторанот. Во случај да
//        има повеќе доставувачи кои се најблиску до ресторанот - се избира доставувачот со најмалку
//        извршени достави досега.
//        По доделување на нарачката на определен доставувач, се менува неговата моментална локација во
//        локацијата на клиентот кому му се доставува нарачката.
//        Доставувачот заработува од нарачката така што добива 90 денари за секоја нарачка, и дополнителни
//        10 денари на секои10 единици растојание од ресторанот до клиентот (пр. ако растојанието е 35
//        единици = 90+3х10 = 120)
//        метод void printUsers() - метод кој ги печати сите корисници на апликацијата сортирани во
//        опаѓачки редослед според потрошениот износ за нарачка на храна преку апликацијата
//        метод void printRestaurants() - метод кој ги печати сите регистрирани ресторани во апликацијата,
//        сортирани во опаѓачки редослед според просечната цена на нарачките наплатени преку апликацијата
//        метод void printDeliveryPeople() - метод кој ги печати сите регистрирани доставувачи сортирани
//        во опачки редослед според заработениот износ од извршените достави.

import java.util.*;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class DeliveryPerson{
    String id, name;
    Location currentLocation;
    int total;
    float earned;

    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.earned=0;
        this.total=0;
    }
    public String getId() {
        return id;
    }
    public float getEarned() {
        return earned;
    }
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    @Override
    public String toString() {
        float average=0;
        if(total!=0&&earned!=0){
            average=earned/total;
        }
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f", id, name, total, earned, average);
    }
}

class Restaurant{
    String id, name;
    Location currentLocation;
    float average;
    float cost;
    int orders;

    public Restaurant(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.average=0;
        this.cost=0;
        this.orders=0;
    }
    public String getId() {
        return id;
    }
    public float getAverage() {
        return average;
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f", id, name, orders, cost, average);
    }
}

class User{
    String id;
    String name;
    float cost;
    int orders;
    Map<String, Addres>addreses;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.cost=0;
        this.orders=0;
        addreses=new HashMap<>();
    }
    public float getCost() {
        return cost;
    }
    public String getId() {
        return id;
    }
    @Override
    public String toString() {
        float average=0;
        if(orders!=0&&cost!=0){
            average=cost/orders;
        }
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f", id, name, orders, cost, average);
    }
}

class Addres{
    String addressName;
    Location location;

    public Addres(String addressName, Location location) {
        this.addressName = addressName;
        this.location = location;
    }
}

class DeliveryApp{
    String name;
    Map<String, DeliveryPerson>deliveryPeople;
    Map<String, Restaurant>restaurants;
    Map<String, User>users;

    public DeliveryApp(String name) {
        this.name = name;
        deliveryPeople=new HashMap<>();
        restaurants=new HashMap<>();
        users=new HashMap<>();
    }
    void registerDeliveryPerson (String id, String name, Location currentLocation){
        DeliveryPerson dp=new DeliveryPerson(id, name, currentLocation);
        deliveryPeople.put(id, dp);
    }
    void addRestaurant (String id, String name, Location location){
        Restaurant r=new Restaurant(id, name, location);
        restaurants.put(id, r);
    }
    void addUser (String id, String name){
        User u=new User(id, name);
        users.put(id, u);
    }
    void addAddress (String id, String addressName, Location location){
        if(users.containsKey(id)){
            Addres a=new Addres(addressName, location);
            User u=users.get(id);
            u.addreses.put(addressName, a);
        }
    }
    void orderFood(String userId, String userAddressName, String restaurantId, float cost){
        int min=9999;
        int number=9999;
        Addres add=null;
        DeliveryPerson dpp=null;
        if(users.containsKey(userId)){
            User u=users.get(userId);
            u.orders++;
            u.cost+=cost;
            if(u.addreses.containsKey(userAddressName)){
                add=u.addreses.get(userAddressName);
            }
        }
        if(add!=null&&restaurants.containsKey(restaurantId)){
            Restaurant r=restaurants.get(restaurantId);
            r.orders++;
            r.cost+=cost;
            r.average=r.cost/r.orders;
            for (DeliveryPerson dp:deliveryPeople.values()) {
                int distance=dp.currentLocation.distance(r.currentLocation);
                if(distance<min||(distance==min&&dp.total<number)){
                    min=distance;
                    number=dp.total;
                    dpp=dp;
                }
            }
            if(dpp!=null){
                int difference=dpp.currentLocation.distance(r.currentLocation);
                difference/=10;
                difference*=10;
                dpp.setCurrentLocation(add.location);
                dpp.earned+=90+difference;
                dpp.total++;
            }
        }
    }
    void printUsers(){
        users.values().stream().sorted(Comparator.comparingDouble(User::getCost).thenComparing(User::getId).reversed()).forEach(System.out::println);
    }
    void printRestaurants(){
        restaurants.values().stream().sorted(Comparator.comparingDouble(Restaurant::getAverage).thenComparing(Restaurant::getId).reversed()).forEach(System.out::println);
    }
    void printDeliveryPeople(){
        deliveryPeople.values().stream().sorted(Comparator.comparingDouble(DeliveryPerson::getEarned).thenComparing(DeliveryPerson::getId).reversed()).forEach(System.out::println);
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
