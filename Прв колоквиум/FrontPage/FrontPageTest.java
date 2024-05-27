//Весникот FINKI Onion се одлучил да развие мобилна апликација за своите вести.
//        Сите вести NewsItem се состојат од наслов, датум на објавување
//        (објект од класата java.util.Date) и категорија. Категоријата на вести е објект од
//        класата Category во која се чува само името на категоријата. Две категории се еднакви,
//        ако се еднакви нивните имиња.
//
//        Во весникот засега постојат два видови вести кои се објавуваат и тоа:
//
//        Текстуални вести (TextNewsItem) за кој се чува дополнително текстот на веста,
//        Мултимедијални вести (MediaNewsItem) за кој се чува url кон локацијата на мултимедијалната
//        содржина (String) и бројот на погледи.
//        Сите вести се додаваат во класа FrontPage во која се чува листа од вести и поле од сите
//        категории на вести кои постојат. За оваа класа треба да се имплементираат следните методи:
//
//        конструктор: FrontPage(Category[] categories);
//        void addNewsItem(NewsItem newsItem) - додава нова вест во листата со вести,
//        List<NewsItem> listByCategory(Category category) - прима еден аргумент рефернца кон објект
//        од Category и враќа листа со сите вести од таа категорија.
//        List<NewsItem> listByCategoryName(String category) - прима еден аргумент String името на
//        категоријата и враќа листа со сите вести од категоријата со тоа име. Ако не постои категорија
//        со вакво име во полето со категории, да се фрли исклучок од тип CategoryNotFoundException во
//        кој се пренесува името на категоријата која не е најдена.
//        препокривање на методот toString() кој враќа String составен од сите кратки содржини на
//        вестите (повик на методот getTeaser()).
//        Во класите за вести треба да се имплементира методот за враќање на кратка содржина
//        getTeaser() на следниот начин:
//
//        TextNewsItem:getTeaser() - враќа String составен од насловот на веста, пред колку
//        минути е објавена веста (цел број минути) и максимум 80 знаци од содржината на веста,
//        сите одделени со нов ред.
//        MediaNewsItem:getTeaser() - враќа String составен од насловот на веста, пред колку минути
//        е објавена веста (цел број минути), url-то на веста и бројот на погледи, сите одделени со
//        нов ред.

import java.util.*;

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message) {
        super(message);
    }
}

class Category implements Comparable<Category>{
    String name;

    public Category(String category) {
        this.name=category;
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }
}

abstract class NewsItem{
    String title;
    Date date;
    Category c;

    public NewsItem(String title, Date date, Category c) {
        this.title = title;
        this.date = date;
        this.c = c;
    }
    abstract String getTeaser();
    public int getTimeeee(){
        Calendar cal=Calendar.getInstance();
        long mss=cal.getTimeInMillis();
        long time=date.getTime();
        long difference=mss-time;
        int min= (int) (difference/(1000*60));
        return min;
    }
}

class TextNewsItem extends NewsItem{
    String text;

    public TextNewsItem(String title, Date date, Category c, String text) {
        super(title, date, c);
        this.text = text;
    }
    String getTeaser(){
        String teaser;
        if(text.length()>80){
            teaser=text.substring(0, 80);
        }
        else{
            teaser=text;
        }
        int min=getTimeeee();
        return String.format("%s\n%d\n%s\n", title, min, teaser);
    }
}

class MediaNewsItem extends NewsItem{
    String url;
    int viewCount;

    public MediaNewsItem(String title, Date date, Category c, String url, int viewCount) {
        super(title, date, c);
        this.url = url;
        this.viewCount = viewCount;
    }
    String getTeaser(){
        int min=getTimeeee();
        return String.format("%s\n%d\n%s\n%d\n", title, min, url, viewCount);
    }
}

class FrontPage{
    List<NewsItem> newsItems;
    Category []categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        this.newsItems=new ArrayList<>();
    }
    void addNewsItem(NewsItem newsItem){
        newsItems.add(newsItem);
    }
    List<NewsItem> listByCategory(Category category){
        List<NewsItem> newList=new ArrayList<>();
        for (NewsItem newsItem:newsItems) {
            if(newsItem.c==category){
                newList.add(newsItem);
            }
        }
        return newList;
    }
    List<NewsItem> listByCategoryName(String category)throws CategoryNotFoundException{
        Category ctg=new Category(category);
        List<NewsItem> newList=new ArrayList<>();
        boolean flag=false;
        for(int i=0; i< categories.length; i++){
            if(categories[i].name.equals(category)){
                flag=true;
            }
        }
        if(!flag){
            throw new CategoryNotFoundException(String.format("Category %s was not found", category));
        }
        for (NewsItem newsItem:newsItems) {
            if(newsItem.c.name.equals(category)){
                newList.add(newsItem);
            }
        }

        return newList;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (NewsItem ni:newsItems) {
            sb.append(ni.getTeaser());
        }
        return sb.toString();
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}