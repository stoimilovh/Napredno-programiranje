//Дадена е класата Applicant (кандидат за евалуација) со име, кредитен резултат
//        (credit score), работен стаж (experience) и дали има криминално досие
//        (criminal record). Исто така, даден е интерфејс за евалуација на кандидати Evaluator со
//        еден метод boolean evaluate(Applicant applicant) и енумерација со следните видови на
//        евалуатори:
//
//        Кандидатот ја поминува евалуацијата (методот враќа true) ако:
//
//        NO_CRIMINAL_RECORD - нема криминално досие
//        MORE_EXPERIENCE - има барем 10 години работен стаж
//        MORE_CREDIT_SCORE - има кредитен резултат од минимум 500
//        NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE - нема криминално досие и има барем 10 години работен стаж
//        MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE - има барем 10 години работен стаж и има кредитен резултат од минимум 500
//        NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE - нема криминално досие и има кредитен резултат од минимум 500.
//        Ваша задача е да го имплементирате методот build(Evaluator.TYPE type) во класата EvaluatorBuilder,
//        кој ќе враќа објект од соодветна имплементација на интерфејсот Evaluator за соодветниот тип на
//        евалуација. Ако типот на евалуација не е некој од наведените, методот треба да фрли исклучок од
//        тип InvalidEvaluation.
//
//        За добро дизајнирано решение (ќе биде објавено по завршувањето на испитот) ќе се добиваат
//        бонус 10 поени.

import java.util.Scanner;

class InvalidEvaluation extends Exception{

}
class Applicant {
    private String name;

    private int creditScore;
    private int employmentYears;
    private boolean hasCriminalRecord;

    public Applicant(String name, int creditScore, int employmentYears, boolean hasCriminalRecord) {
        this.name = name;
        this.creditScore = creditScore;
        this.employmentYears = employmentYears;
        this.hasCriminalRecord = hasCriminalRecord;
    }
    public String getName() {
        return name;
    }
    public int getCreditScore() {
        return creditScore;
    }
    public int getEmploymentYears() {
        return employmentYears;
    }
    public boolean hasCriminalRecord() {
        return hasCriminalRecord;
    }
    @Override
    public String toString() {
        return String.format("Name: %s\nCredit score: %d\nExperience: %d\nCriminal record: %s\n",
                name, creditScore, employmentYears, hasCriminalRecord ? "Yes" : "No");
    }
}

interface Evaluator {
    enum TYPE {
        NO_CRIMINAL_RECORD,
        MORE_EXPERIENCE,
        MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE,
        MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE,
        INVALID // should throw exception
    }
    boolean evaluate(Applicant applicant);
}

class EvaluatorBuilder {
    public static Evaluator build(Evaluator.TYPE type) throws InvalidEvaluation {
        // вашиот код овде
        if(type==Evaluator.TYPE.NO_CRIMINAL_RECORD){
            return applicant -> !applicant.hasCriminalRecord();
        }
        else if(type==Evaluator.TYPE.MORE_EXPERIENCE){
            return applicant -> applicant.getEmploymentYears()>=10;
        }
        else if(type==Evaluator.TYPE.MORE_CREDIT_SCORE){
            return applicant -> applicant.getCreditScore()>=500;
        }
        else if(type==Evaluator.TYPE.NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE){
            return applicant -> !applicant.hasCriminalRecord()&&applicant.getEmploymentYears()>=10;
        }
        else if(type==Evaluator.TYPE.MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE){
            return applicant -> applicant.getEmploymentYears()>=10&&applicant.getCreditScore()>=500;
        }
        else if(type==Evaluator.TYPE.NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE){
            return applicant -> !applicant.hasCriminalRecord()&&applicant.getCreditScore()>=500;
        }
        else{
            throw new InvalidEvaluation();
        }
    }
}


// имплементација на евалуатори овде



public class ApplicantEvaluationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        int creditScore = scanner.nextInt();
        int employmentYears = scanner.nextInt();
        boolean hasCriminalRecord = scanner.nextBoolean();
        int choice = scanner.nextInt();
        Applicant applicant = new Applicant(name, creditScore, employmentYears, hasCriminalRecord);
        Evaluator.TYPE type = Evaluator.TYPE.values()[choice];
        Evaluator evaluator = null;
        try {
            evaluator = EvaluatorBuilder.build(type);
            System.out.println("Applicant");
            System.out.println(applicant);
            System.out.println("Evaluation type: " + type.name());
            if (evaluator.evaluate(applicant)) {
                System.out.println("Applicant is ACCEPTED");
            } else {
                System.out.println("Applicant is REJECTED");
            }
        } catch (InvalidEvaluation invalidEvaluation) {
            System.out.println("Invalid evaluation");
        }
    }
}

