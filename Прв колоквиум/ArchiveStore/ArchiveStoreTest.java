//Да се имплементира класа ArchiveStore во која се чува листа на архиви (елементи за архивирање).
//
//        Секој елемент за архивирање Archive има:
//
//        id - цел број
//        dateArchived - датум на архивирање.
//        Постојат два видови на елементи за архивирање, LockedArchive за кој дополнително се чува датум
//        до кој не смее да се отвори dateToOpen и SpecialArchive за кој се чуваат максимален број на
//        дозволени отварања maxOpen. За елементите за архивирање треба да се обезбедат следните методи:
//
//        LockedArchive(int id, LocalDate dateToOpen) - конструктор за заклучена архива
//        SpecialArchive(int id, int maxOpen) - конструктор за специјална архива
//        За класата ArchiveStore да се обезбедат следните методи:
//
//        ArchiveStore() - default конструктор
//        void archiveItem(Archive item, LocalDate date) - метод за архивирање елемент item на одреден датум date
//        void openItem(int id, LocalDate date) - метод за отварање елемент од архивата со зададен id и
//        одреден датум date. Ако не постои елемент со даденото id треба да се фрли исклучок од тип NonExistingItemException со
//        порака Item with id [id] doesn't exist.
//        String getLog() - враќа стринг со сите пораки запишани при архивирањето и отварањето архиви во посебен ред.
//        За секоја акција на архивирање во текст треба да се додаде следната порака Item [id] archived at [date],
//        додека за секоја акција на отварање архива треба да се додаде Item [id] opened at [date]. При отварање ако се работи
//        за LockedArhive и датумот на отварање е пред датумот кога може да се отвори, да се додаде порака Item [id] cannot be
//        opened before [date]. Ако се работи за SpecialArhive и се обидиеме да ја отвориме повеќе пати од дозволениот број
//        (maxOpen) да се додаде порака Item [id] cannot be opened more than [maxOpen] times.

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception{
    int id;

    public NonExistingItemException(int id) {
        this.id = id;
    }
    public String getMessage(){
        return String.format("Item with id %d doesn't exist", id);
    }
}

class Archive{
    int id;
    LocalDate date;

    public Archive(int id, LocalDate date) {
        this.id = id;
        this.date = date;
    }
    public Archive(int id){
        this.id=id;
    }
}

class LockedArchive extends Archive{
    LocalDate dateToOpen;
    LockedArchive(int id, LocalDate dateToOpen){
        super(id);
        this.dateToOpen=dateToOpen;
    }
}

class SpecialArchive extends Archive{
    int maxOpen;
    int openCount;
    public SpecialArchive(int id, int maxOpen){
        super(id);
        this.maxOpen=maxOpen;
        this.openCount=0;
    }
}

class ArchiveStore{
    List<Archive> archives;
    List<String> messages;
    public ArchiveStore(){
        this.archives=new ArrayList<>();
        this.messages=new ArrayList<>();
    }
    void archiveItem(Archive item, LocalDate date){
        archives.add(item);
        messages.add(String.format("Item %d archived at %s", item.id, date));
    }
    void openItem(int id, LocalDate date)throws NonExistingItemException{
        int flag=1;
        try{
            for (Archive archive:archives) {
                if(archive.id==id){
                    flag=0;
                    if(archive instanceof LockedArchive){
                        LockedArchive la=(LockedArchive)archive;
                        if(date.isBefore(la.dateToOpen)){
                            messages.add(String.format("Item %d cannot be opened before %s", id, la.dateToOpen));
                        }
                        else{
                            messages.add(String.format("Item %d opened at %s", id, date));
                        }
                    }
                    else if(archive instanceof SpecialArchive){
                        SpecialArchive sa=(SpecialArchive) archive;
                        if(sa.openCount< sa.maxOpen){
                            sa.openCount++;
                            messages.add(String.format("Item %d opened at %s", id, date));
                        }
                        else{
                            messages.add(String.format("Item %d cannot be opened more than %d times", id, sa.maxOpen));
                        }
                    }

                }
            }
            if(flag==1){
                throw new NonExistingItemException(id);
            }
        }
        catch (NonExistingItemException nn){
            System.out.println(nn.getMessage());
        }
    }
    String getLog(){
        StringBuilder sb=new StringBuilder();
        for (String mess:messages) {
            sb.append(mess);
            sb.append("\n");
        }
        return sb.toString();
    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}