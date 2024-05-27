//Да се имплементира класа FootballTable за обработка од податоците за повеќе фудбласки
//        натпревари од една лига и прикажување на табелата на освоени поени според резултатите
//        од натпреварите. Во класата да се имплементираат:
//
//public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) - метод за
//        додавање податоци за одигран натпревар помеѓу тимот со име homeTeam (домашен тим) и тимот
//        со име awayTeam (гостински тим), при што homeGoals претставува бројот на постигнати голови
//        од домашниот тим, а awayGoals бројот на постигнати голови од гостинскиот тим.
//public void printTable() - метод за печатење на табелата според одиграните (внесените) натпревари.
//        Во табелата се прикажуваат редниот број на тимот во табелата, името
//        (со 15 места порамнето во лево), бројот на одиграни натпревари, бројот на победи,
//        бројот на нерешени натпревари, бројот на освоени поени (сите броеви се печатат со 5
//        места порамнети во десно). Бројот на освоени поени се пресметува како број_на_победи x 3 +
//        број_на_нерешени x 1. Тимовите се подредени според бројот на освоени поени во опаѓачки редослед,
//        ако имаат ист број на освоени поени според гол разликата (разлика од постигнатите голови и
//        примените голови) во опаѓачки редослед, а ако имаат иста гол разлика, според името.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */

class Game{
    String name;
    int goals;
    int number;
    int wins;
    int nereseni;
    int lost;
    int points;
    int primeni;


    public Game(String name) {
        this.name = name;
        this.goals = 0;
        this.wins=0;
        this.number=0;
        this.nereseni=0;
        this.lost=0;
        this.points=0;
        this.primeni=0;
    }
    public String getName() {
        return name;
    }
    public int getGoals() {
        return goals;
    }
    int getPoints(){
        return wins*3+nereseni*1;
    }
    int getRazlika(){
        return goals-primeni;
    }
}

class FootballTable{
    HashMap<String, Game>footbal;

    public FootballTable() {
        this.footbal=new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        Game g1=new Game(homeTeam);
        Game g2=new Game(awayTeam);
        if(footbal.containsKey(homeTeam)){
            g1=footbal.get(homeTeam);
        }
        if(footbal.containsKey(awayTeam)){
            g2=footbal.get(awayTeam);
        }
        g1.number++;
        g2.number++;
        g1.goals+=homeGoals;
        g1.primeni+=awayGoals;
        g2.goals+=awayGoals;
        g2.primeni+=homeGoals;
        if(homeGoals>awayGoals){
            g1.wins++;
            g2.lost++;
        }
        else if(homeGoals<awayGoals){
            g2.wins++;
            g1.lost++;
        }
        else {
            g1.nereseni++;
            g2.nereseni++;
        }
        footbal.put(homeTeam, g1);
        footbal.put(awayTeam, g2);
    }
    public void printTable() {
        int i=1;
        List<Game>games=new ArrayList<>(footbal.values());
        games = games.stream().sorted(Comparator.comparing(Game::getPoints).thenComparing(Game::getRazlika).reversed().thenComparing(Game::getName)).collect(Collectors.toList());

        for(Game g:games){
            System.out.printf("%2s. %-15s%5d%5d%5d%5d%5d\n", i, g.name, g.number, g.wins, g.nereseni, g.lost, g.getPoints(), g.goals);
            i++;
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

