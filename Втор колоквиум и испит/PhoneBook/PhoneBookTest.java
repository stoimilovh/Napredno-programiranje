//Да се имплементира класа за именик PhoneBook со следните методи:
//
//        void addContact(String name, String number) - додава нов контакт во именикот.
//        Ако се обидеме да додадеме контакт со веќе постоечки број, треба да се фрли исклучок
//        од класа DuplicateNumberException со порака Duplicate number: [number]. Комплексноста
//        на овој метод не треба да надминува $O(log N)$ за $N$ контакти.
//        void contactsByNumber(String number) - ги печати сите контакти кои во бројот го содржат
//        бројот пренесен како аргумент во методот (минимална должина на бројот [number] е 3).
//        Комплексноста на овој метод не треба да надминува $O(log N)$ за $N$ контакти.
//        void contactsByName(String name) - ги печати сите контакти кои даденото име. Комплексноста
//        на овој метод треба да биде $O(1)$.
//        Во двата методи контактите се печатат сортирани лексикографски според името, а оние со исто
//        име потоа според бројот.

import java.util.*;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}

class NotFound extends Exception{
    public NotFound() {
        super("NOT FOUND");
    }
}

class Contact {
    String name;
    String number;
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }
    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }
}

class PhoneBook {
    Set<String> allNumbers;
    Map<String, Set<Contact>> byNumberParts;
    Map<String, Set<Contact>> byName;
    static Comparator<Contact> comparator = Comparator.comparing(Contact::getName)
            .thenComparing(Contact::getNumber);
    public PhoneBook() {
        byNumberParts = new TreeMap<>();
        byName = new HashMap<>();
        allNumbers = new HashSet<>();
    }
    public void addContact(String name, String number) throws DuplicateNumberException {
        if (allNumbers.contains(number)){
            throw new DuplicateNumberException(number);
        }
        Contact contact = new Contact(name, number);
        Set<Contact> contactsByName = byName.computeIfAbsent(name, key -> new TreeSet<>(comparator));
        contactsByName.add(contact);
        List<String> keys = getKeys(number, 3);
        for (String key : keys) {
            Set<Contact> contactsByNumber = byNumberParts.computeIfAbsent(key, k -> new TreeSet<>(comparator));
            contactsByNumber.add(contact);
        }
    }
    private List<String> getKeys(String key, int minLen) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= key.length() - minLen; ++i) {
            for (int len = minLen; len <= (key.length() - i); ++len) {
                String k = key.substring(i, i + len);
                result.add(k);
            }
        }
        return result;
    }
    // O(log N)
    public void contactsByNumber(String number) throws NotFound {
        try{
            if (byNumberParts.containsKey(number)) {
                byNumberParts.get(number).forEach(System.out::println);
            }
            else {
                throw new NotFound();
            }
        }
        catch (NotFound e){
            System.out.println(e.getMessage());
        }
    }
    // O(1)
    public void contactsByName(String name) throws NotFound {
        try{
            if (byName.containsKey(name)) {
                byName.get(name).forEach(System.out::println);
            }
            else {
                throw new NotFound();
            }
        }
        catch (NotFound e){
            System.out.println(e.getMessage());
        }
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            try {
                if (parts[0].equals("NUM")) {
                    phoneBook.contactsByNumber(parts[1]);
                } else {
                    phoneBook.contactsByName(parts[1]);
                }
            }
            catch (NotFound f){
                System.out.println(f.getMessage());
            }
        }
    }

}