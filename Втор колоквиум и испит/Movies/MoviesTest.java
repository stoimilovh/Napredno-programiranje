//Да се имплементира класа MoviesList во која се чува листа од филмови
//        (класа Movie за секој филм се дадени неговиот наслов и листа од рејтинзи
//        (цели броеви од 1 до 10) и ги има следните методи:
//
//public void addMovie(String title, int[] ratings) - метод за додавање нов филм во листата
//        (наслов и низа од рејтинзи)
//public List<Movie> top10ByAvgRating() - метод кој враќа листа од 10-те филмови со најдобар
//        просечен рејтинг, подредени во опаѓачки редослед според рејтингот (ако два филмови
//        имаат ист просечен рејтинг, се подредуваат лексикографски според името)
//public List<Movie> top10ByRatingCoef() - метод кој враќа листа од 10-те филмови со најдобар
//        рејтинг коефициент (се пресметува како просечен ретјтинг на филмот x вкупно број на
//        рејтинзи на филмот / максимален број на рејтинзи (од сите филмови во листата)
//        За класата Movie да се препокрие toString() методот да враќа соодветна репрезентација
//        (погледнете го пример излезот).

import java.util.*;
import java.util.stream.Collectors;

class Movie{
    String title;
    List<Integer>ratings;

    public Movie(String title, List<Integer> ratings) {
        this.title = title;
        this.ratings = ratings;
    }
    public String getTitle() {
        return title;
    }
    double average(){
        double sum=ratings.stream().mapToDouble(Integer::doubleValue).sum();
        return sum/ratings.size();
    }
    double topMovies(int num){
        return this.average()*ratings.size()/num;
    }
    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, this.average(), ratings.size());
    }
}

class MoviesList{
    List<Movie> movies;
    int max;

    public MoviesList() {
        this.movies=new ArrayList<>();
        this.max=0;
    }
    public void addMovie(String title, int[] ratings){
        List<Integer>rat=new ArrayList<>();
        max+= ratings.length;
        for(int i=0; i<ratings.length; i++){
            rat.add(ratings[i]);
        }
        Movie m=new Movie(title, rat);
        movies.add(m);
    }
    public List<Movie> top10ByAvgRating(){
        List<Movie> top10=movies.stream().sorted(Comparator.comparingDouble(Movie::average).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
        return top10;
    }
    public List<Movie> top10ByRatingCoef(){
        List<Movie> top10=movies.stream().sorted(Comparator.<Movie, Double>comparing(m-> m.topMovies(max)).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
        return top10;
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}