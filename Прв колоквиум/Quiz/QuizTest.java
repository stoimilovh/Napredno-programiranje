//Да се дефинира класа Quiz за репрезентација на еден квиз во кој може да се наоѓаат
//        повеќе прашања од 2 типа (true/false прашање или прашање со избор на еден точен одговор
//        од 5 понудени одговори A/B/C/D/E). За класата Quiz да се имплементираат следните методи:
//
//        конструктор
//        void addQuestion(String questionData) - метод за додавање на прашање во квизот.
//        Податоците за прашањето се дадени во текстуална форма и може да бидат во следните два формати
//        согласно типот на прашањето:
//        TF;text;points;answer (answer може да биде true или false)
//        MC;text;points;answer (answer е каракатер кој може да ја има вредноста A/B/C/D/E)
//        Со помош на исклучок од тип InvalidOperationException да се спречи додавање на прашање со
//        повеќе понудени одговори во кое како точен одговор се наоѓа некоја друга опција освен
//        опциите A/B/C/D/E.
//        void printQuiz(OutputStream os) - метод за печатење на квизот на излезен поток. Потребно е да
//        се испечатат сите прашања од квизот подредени според бројот на поени на прашањата во опаѓачки
//        редослед.
//        void answerQuiz (List<String> answers, OutputStream os) - метод којшто ќе ги одговори сите
//        прашања од квизот (во редоследот во којшто се внесени) со помош на одговорите од листата answers.
//        Методот треба да испечати извештај по колку поени се освоени од секое прашање и колку вкупно поени
//        се освоени од квизот (види тест пример). Да се фрли исклучок од тип InvalidOperationException
//        доколку бројот на одговорите во `answers е различен од број на прашања во квизот.
//        За точен одговор на true/false прашање се добиваат сите предвидени поени, а за неточен одговор
//        се добиваат 0 поени
//        За точен одговор на прашање со повеќе понудени одговори се добиваат сите предвидени поени,
//        а за неточен одговор се добиваат негативни поени (20% од предвидените поени).
//
//
//        Напомена: Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети.
//        Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети
//        со најмногу 80% од поените.

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}


abstract class Questions implements Comparable<Questions>{
    String quest;
    int points;

    public Questions(String quest, int points) {
        this.quest = quest;
        this.points = points;
    }
    abstract float answerPoints(String answer);
}

class Quiz{
    List<Questions> quests;

    public Quiz() {
        this.quests = new ArrayList<>();
    }

    void addQuestion(String questionData)throws InvalidOperationException{
        String []date=questionData.split(";");
        String type=date[0];
        if(type.equals("MC")){
            String question=date[1];
            int points=Integer.parseInt(date[2]);
            String answer=date[3];
            if(!answer.equals("A")&&!answer.equals("B")&&!answer.equals("C")&&!answer.equals("D")&&!answer.equals("E")){
                throw new InvalidOperationException(String.format("%s is not allowed option for this question", answer));
            }
            MultipleChoice tc=new MultipleChoice(question, points, answer);
            quests.add(tc);
        }
        else if (type.equals("TF")){
            String question=date[1];
            int points=Integer.parseInt(date[2]);
            boolean answer=Boolean.parseBoolean(date[3]);
            TwoChoice tc=new TwoChoice(question, points, answer);
            quests.add(tc);
        }
    }
    void printQuiz(OutputStream os){
        PrintWriter pr=new PrintWriter(os);
        quests.stream().sorted(Comparator.reverseOrder()).forEach(q -> pr.println(q));
        pr.flush();
    }
    void sortQuest(){
        quests.sort(Comparator.comparingInt(quest -> quest.points));
        Collections.reverse(quests);
    }
    void answerQuiz (List<String> answers, OutputStream os)throws InvalidOperationException{
        if(answers.size()!=quests.size()){
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }
        PrintWriter pr=new PrintWriter(os);
        float points, total=0;
        for(int i=0; i<quests.size(); i++){
            points=quests.get(i).answerPoints(answers.get(i));
            pr.println(String.format("%d. %.2f", i+1, points));
            total+=points;
        }
        pr.println(String.format("Total points: %.2f", total));
        pr.flush();
    }
}

class TwoChoice extends Questions{
    boolean answer;

    public TwoChoice(String quest, int points, boolean answer) {
        super(quest, points);
        this.answer = answer;
    }
    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %b", quest, points, answer);
    }

    @Override
    float answerPoints(String answerr) {
        if(answer==Boolean.parseBoolean(answerr)){
            return points;
        }
        return 0;
    }

    @Override
    public int compareTo(Questions o) {
        return Integer.compare(this.points, o.points);
    }
}

class MultipleChoice extends Questions{
    String answer;

    public MultipleChoice(String quest, int points, String answer) {
        super(quest, points);
        this.answer = answer;
    }
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s", quest, points, answer);
    }
    @Override
    float answerPoints(String answerr) {
        if(answer.equals(answerr)){
            return points;
        }
        return (float) (points*0.2)*(-1);
    }
    public int compareTo(Questions o) {
        return Integer.compare(this.points, o.points);
    }
}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}