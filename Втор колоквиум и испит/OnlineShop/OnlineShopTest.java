//Да се имплементира апликација за чување на продукти во една online продавница.
//        За таа цел, дефинирајте класа OnlineShop во која што ќе ги чувате сите
//        продукти во продавниците и ќе нуди функционалности за листање на продуктите и
//        нивно купување. За класата да се имплементираат:
//
//        Default конструктор OnlineShop()
//        Метод void addProduct(String category, String id, String name, LocalDateTime createdAt,
//        double price) - метод за додавање на производ во онлајн продавницата. Секој производ е
//        дефиниран со категорија, ИД, име, датум кога се додава во продавницата и негова цена.
//        метод double buyProduct(String id, int quantity) - што ќе имплементира купување на quantity
//        количина на производот со ИД id. Методот да врати колку пари се потрошени за оваа трансакција.
//        Да се фрли исклучок од тип ProductNotFoundException доколку не постои производот. Методот да
//        има комплексност О(1).
//        метод List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType,
//        int pageSize) којшто ќе ги излиста сите производи од категоријата category сортирани според
//        компараторот comparatorType групирани во страници со големина pageSize (пагинација).
//        Category може да биде и null па во тој случај се листаат сите продукти во онлајн продавницата.
//        COMPARATOR_TYPE е еnum којшто ви е даден во почетниот код. За печатење на продуктите користете
//        ја вградената toString нотација во IDE-то (запазете го редоследот и имињата на променливите).

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}

class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int quantity;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantity = 0;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantity +
                '}';
    }
}

class OnlineShop {
    Map<String, Product> productMap;

    OnlineShop() {
        this.productMap = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product p = new Product(category, id, name, createdAt, price);
        productMap.put(id, p);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if (!productMap.containsKey(id)) {
            throw new ProductNotFoundException("Product with id " + id + " does not exist in the online shop!");
        }
        productMap.get(id).quantity += quantity;
        return productMap.get(id).price * quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        if(category!=null){
            List<Product> products = productMap.values().stream().filter(p -> p.category.equals(category)).collect(Collectors.toList());
            Comparator<Product> comparator = getComparator(comparatorType);
            products.sort(comparator);
            for (int i = 0; i < products.size(); i += pageSize) {
                int end = Math.min(i + pageSize, products.size());
                result.add(new ArrayList<>(products.subList(i, end)));
            }
        }
        else{
            List<Product> products = productMap.values().stream().collect(Collectors.toList());
            Comparator<Product> comparator = getComparator(comparatorType);
            products.sort(comparator);
            for (int i = 0; i < products.size(); i += pageSize) {
                int end = Math.min(i + pageSize, products.size());
                result.add(new ArrayList<>(products.subList(i, end)));
            }
        }
        return result;
    }

    Comparator<Product> getComparator(COMPARATOR_TYPE com) {
        if (com == COMPARATOR_TYPE.NEWEST_FIRST) {
            return Comparator.comparing(Product::getCreatedAt).reversed();
        } else if (com == COMPARATOR_TYPE.OLDEST_FIRST) {
            return Comparator.comparing(Product::getCreatedAt);
        } else if (com == COMPARATOR_TYPE.LOWEST_PRICE_FIRST) {
            return Comparator.comparing(Product::getPrice);
        } else if (com == COMPARATOR_TYPE.HIGHEST_PRICE_FIRST) {
            return Comparator.comparing(Product::getPrice).reversed();
        } else if (com == COMPARATOR_TYPE.MOST_SOLD_FIRST) {
            return Comparator.comparing(Product::getQuantity).reversed();
        } else {
            return Comparator.comparing(Product::getQuantity);
        }
    }
}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

