//Да се напише класа за книга Book во која се чува:
//
//        наслов
//        категорија
//        цена.
//        Да се имплементира конструктор со следните аргументи Book(String title, String category, float price).
//
//        Потоа да се напише класа BookCollection во која се чува колекција од книги.
//        Во оваа класа треба да се имплментираат следните методи:
//
//public void addBook(Book book) - додавање книга во колекцијата
//public void printByCategory(String category) - ги печати сите книги од проследената категорија
//        (се споредува стрингот без разлика на мали и големи букви), сортирани според насловот на
//        книгата (ако насловот е ист, се сортираат според цената).
//public List<Book> getCheapestN(int n) - враќа листа на најевтините N книги (ако има помалку од
//        N книги во колекцијата, ги враќа сите).

import java.util.*;
import java.util.stream.Collectors;

class Book{
    String title;
    String category;
    float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }
    public String getName() {
        return title;
    }
    public float getPrice() {
        return price;
    }
    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}

class BookCollection{
    Map<String, List<Book>>categoryMap;

    public BookCollection() {
        this.categoryMap=new HashMap<>();
    }
    public void addBook(Book book){
        List<Book>list=new ArrayList<>();
        if(categoryMap.containsKey(book.category.toUpperCase())){
            list=categoryMap.get(book.category.toUpperCase());
        }
        list.add(book);
        categoryMap.put(book.category.toUpperCase(), list);
    }
    public void printByCategory(String category){
        List<Book>c=categoryMap.get(category.toUpperCase());
        c.sort(Comparator.comparing(Book::getName).thenComparing(Book::getPrice));
        for (int i=0; i<c.size(); i++){
            System.out.println(c.get(i).toString());
        }
    }
    public List<Book> getCheapestN(int n){
        return categoryMap.values().stream().flatMap(List::stream).sorted(Comparator.comparingDouble(Book::getPrice).thenComparing(Book::getName)).limit(n).collect(Collectors.toList());
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}