//Да се имплементира класа QuizProcessor со единствен метод Map<String, Double> processAnswers(InputStream is).
//
//        Методот потребно е од влезниот поток is да ги прочита одговорите на студентите на еден квиз.
//        Информациите за квизовите се дадени во посебни редови и се во следниот формат:
//
//        ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An.
//        каде што ID е индексот на студентот, Ci е точниот одговор на i-то прашање, а Ai е одговорот на
//        студентот на i-то прашање. Студентот добива по 1 поен за точен одговор, а по -0.25 за секој
//        неточен одговор. Бројот на прашања n може да биде различен во секој квиз.
//
//        Со помош на исклучоци да се игнорира квиз во кој бројот на точни одговори е различен од бројот
//        на одговорите на студентот.
//
//        Во резултантната мапа, клучеви се индексите на студентите, а вредности се поените кои студентот
//        ги освоил. Пример ако студентот на квиз со 6 прашања, има точни 3 прашања, а неточни 3 прашања,
//        студентот ќе освои 3*1 - 3*0.25 = 2.25.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

class ExcaptionNumber extends Exception{
    public ExcaptionNumber() {
        super("A quiz must have same number of correct and selected answers");
    }
}

class Quiz{
    String id;
    List<String> correctAnswers;
    List<String> studentAnswers;
    double points=0;

    public static Quiz createQuiz(String line) throws ExcaptionNumber{
        String []parts=line.split(";");
        List<String>correct=new ArrayList<>(Arrays.asList(parts[1].split(",")));
        List<String>student=new ArrayList<>(Arrays.asList(parts[2].split(",")));
        return new Quiz(parts[0], correct, student);
    }

    public Quiz(String id, List<String> correctAnswers, List<String> studentAnswers) throws ExcaptionNumber {
        if(correctAnswers.size()!=studentAnswers.size()) {
            throw new ExcaptionNumber();
        }
        this.id = id;
        this.correctAnswers = correctAnswers;
        this.studentAnswers = studentAnswers;
        for(int i=0; i<correctAnswers.size(); i++){
            if(correctAnswers.get(i).equals(studentAnswers.get(i))){
                points+=1;
            }
            else{
                points-=0.25;
            }
        }
    }
}

class QuizProcessor{
    public static Map<String, Double> processAnswers(InputStream is)throws ExcaptionNumber {
        Map<String, Double>resultMap=new HashMap<>();
        Scanner sc=new Scanner(is);
        while(sc.hasNext()){
            String line= sc.nextLine();
            try{
                Quiz quiz=Quiz.createQuiz(line);
                resultMap.put(quiz.id, quiz.points);
            }
            catch (ExcaptionNumber e){
                System.out.println(e.getMessage());
            }
        }
        Map<String, Double>sorted=new TreeMap<>(resultMap);
        return sorted;
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        try {
            QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
        } catch (ExcaptionNumber e) {
            System.out.println(e.getMessage());;
        }
    }
}